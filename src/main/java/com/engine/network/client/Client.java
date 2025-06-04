package com.engine.network.client;

import java.net.*;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.SecretKey;

import com.engine.network.encryption.Convert;
import com.engine.network.encryption.Encryption;
import com.engine.network.encryption.HMACAuthenticator;
import com.engine.network.headers.BaseHeader;
import com.engine.util.Functions;

public class Client {
    public interface RecvFunc {
        void run(ClientPacketData data) throws Exception;
    }

    private DatagramSocket socket;

    private InetAddress address;
    private int port;

    private PublicKey publicKey;
    private SecretKey aesKey;

    private byte[] sendBuffer;
    private byte[] recvBuffer;

    private DatagramPacket sendPacket;
    private DatagramPacket recvPacket;

    private boolean aesSessionStarted = false;
    private volatile boolean loggedIn = false;
    private volatile boolean roomSet = false;

    private ExecutorService executor;

    /**
     * @return whether the client is logged in or not
     */
    public boolean loggedIn() {
        return loggedIn;
    }

    /**
     * @return whether the client is in a room or not
     */
    public boolean roomSet() {
        return roomSet;
    }

    private RecvFunc recv;

    private String sessionKey;
    private String username;

    private volatile boolean recievedWaitForHeader = false;
    private volatile byte[][] waitForHeaders;

    /**
     * The Client class is used to create a client that can connect to a server using UDP sockets.
     * @param recv
     * @param scan
     * @throws Exception
     */
    public Client(RecvFunc recv) {
        // Setup client
        this.recv = recv;
    }

    /**
     * Requests the public key from the server.
     * @throws Exception
     */
    private void requestPublicKey() throws Exception {
        System.out.println("Asking For Public Key...");
        // Send a request to get the public key
        sendBuffer = Encryption.concatBytes(BaseHeader.AskPublicKey.value(), "Request public key".getBytes());
        sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, this.port);
        socket.send(sendPacket);

        // Receive the public key
        recvBuffer = new byte[1024];
        recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
        socket.receive(recvPacket);

        // Decode the Base64-encoded public key
        String publicKeyBase64 = new String(recvPacket.getData(), 0, recvPacket.getLength());

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);

        // Reconstruct and save the public key
        this.publicKey = Encryption.bytesToRSAPublicKey(publicKeyBytes);
        System.out.println("Received Public Key!");
    }

    /**
     * Generates an AES key and sends it to the server, for fast symmetric encryption.
     * The server will send back a confirmation message to start the AES session.
     * @return true if the AES session started successfully, false otherwise
     * @throws Exception
     */
    private boolean genAndSendAESKey() throws Exception {
        System.out.println("Sending AES Key...");

        this.aesKey = Encryption.generateAESKey();

        byte[] encodedAESKey = aesKey.getEncoded();

        byte[] msg = Encryption.concatBytes(BaseHeader.GiveAESKey.value(), new byte[] { (byte) encodedAESKey.length }, encodedAESKey);

        byte[] encryptedAESKey = Encryption.encryptRSA(msg, publicKey);

        DatagramPacket packet = new DatagramPacket(encryptedAESKey, encryptedAESKey.length, address, this.port);
        socket.send(packet);

        byte[] confirmMsg = new byte[256];
        packet = new DatagramPacket(confirmMsg, confirmMsg.length);
        socket.receive(packet);

        byte[] pktData = Arrays.copyOfRange(packet.getData(), 0, packet.getLength());
        byte[] decryptedBytes = Encryption.decryptAES(pktData, aesKey);
        String decryptedMsg = new String(decryptedBytes, 1, decryptedBytes.length - 1);

        if (decryptedMsg.equals("AES Confirmed")) {
            System.out.println("AES Session Started Successfully!");
            aesSessionStarted = true;
            socket.setSoTimeout(0);
            return true;
        }
        System.out.println("Unsuccessful AES Session Start. Trying Again...");
        return false;
    }

    /**
     * Connects to the server using the given address and port.
     * This method also handles the initial handshake for the public key and AES session.
     * @param address : address of the server
     * @param port : port of the server
     * @throws Exception
     */
    public void connect(String address, int port) throws Exception {
        this.socket = new DatagramSocket();
        socket.setSoTimeout(1000);
        this.address = InetAddress.getByName(address);
        this.port = port;

        System.out.println("Client Starting...");

        // Do the cert handshake and send symmetric keys to each other, starting an AES Session.
        requestPublicKey();
        if (!genAndSendAESKey()) {
            throw new Exception("Failed to start AES Session");
        }

        // Create executor and start the client's receiving thread
        executor = Executors.newSingleThreadExecutor();
        startRecvThread();
    }
    
    /**
     * Gets the packet data from the recieved packet, and makes sure it is used correctly.
     * @throws Exception
     */
    private void recvPacket() throws Exception {
        if (socket == null || socket.isClosed()) {
            return; // If the socket is closed, do not attempt to receive packets
        }

        try {
            recvBuffer = new byte[1024];
            recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
            socket.receive(recvPacket);

            ClientPacketData data = new ClientPacketData(recvPacket, aesSessionStarted, aesKey);

            processPacketData(data);
            recv.run(data);

            // If the header a request was wait for is received, set the flag to true
            if (waitForHeaders != null) {
                recievedWaitForHeader = Arrays.stream(waitForHeaders)
                    .anyMatch(header -> Arrays.equals(header, data.header));
                if (recievedWaitForHeader) {
                    waitForHeaders = null;
                }
            }
        } catch (Exception e) {
            if (socket.isClosed()) {
                return; // If the socket is closed, do not attempt to receive packets
            }
            System.err.println("Error receiving packet: " + e.getMessage());
        }
    }

    /**
     * Starts the thread that will receive packets from the server.
     * This thread will run indefinitely until the client is closed.
     */
    private void startRecvThread() {
        executor.submit(() -> {
            while (true) { 
                try {
                    recvPacket();
                } catch (Exception e) {
                    System.err.println("Error in receive thread: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sends a session packet to the server.
     * This packet is encrypted with AES, and uses an HMAC token if you have already logged in to ensure the server still knows it's you.
     * @param header : the header of the packet
     * @param msg : the message to send
     * @throws Exception
     */
    public void sendSessionPacket(byte[] header, String msg) throws Exception {
        sendSessionPacket(header, msg.getBytes());
    }

    /**
     * Sends a session packet to the server.
     * This packet is encrypted with AES, and uses an HMAC token if you have already logged in to ensure the server still knows it's you.
     * @param header : the header of the packet
     * @param msg : the message to send
     * @throws Exception
     */
    public void sendSessionPacket(byte[] header, byte[] msg) throws Exception {
        if (socket == null || socket.isClosed()) {
            return; // If the socket is closed, do not attempt to send packets
        }

        if (loggedIn && sessionKey != null) {
            long time = (long) (Functions.getTime() / 1000);
            byte[] hmac = HMACAuthenticator.generateHMACToken(sessionKey, username, time);
            byte[] timeBytes = Convert.ltob(time);

            // System.out.println("Sending HMAC: " + hmac.length + " time: " + timeBytes.length + " msg: " + msg.length);

            msg = Encryption.concatBytes(new byte[] { (byte) timeBytes.length }, timeBytes,
                    new byte[] { (byte) hmac.length }, hmac, new byte[] { (byte) msg.length }, msg);
        } else {
            msg = Encryption.concatBytes(new byte[] { (byte) msg.length }, msg);
        }

        sendBuffer = Encryption.encryptAES(Encryption.concatBytes(header, msg), aesKey);
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, this.port);
        socket.send(sendPacket);
    }

    /**
     * Sends a session packet to the server and waits for a response.
     * This packet is encrypted with AES, and uses an HMAC token if you have already logged in to ensure the server still knows it's you.
     * This 
     * @param header : the header of the packet
     * @param msg : the message to send
     * @param waitForHeaders : the headers to wait for
     * @throws Exception
     */
    public void sendSessionPacketAndWait(byte[] header, byte[] msg, byte[][] waitForHeaders) throws Exception {
        sendSessionPacket(header, msg);
        recievedWaitForHeader = false;
        this.waitForHeaders = waitForHeaders;

        while (!recievedWaitForHeader) {
            Thread.sleep(100);
        }
    }

    /**
     * Processes the packet data received from the server, using the header to determine what to do with it.
     * Also, uses the function passed in to the Client to process the data, so the options are endless.
     * @param data : the packet data to process
     * @throws Exception
     */
    private void processPacketData(ClientPacketData data) throws Exception {
        if (Arrays.equals(BaseHeader.BackForthMsg.value(), data.header)) {
            System.out.println("Back Forth Msg: " + data.msgStr);
            return;
        }

        if (BaseHeader.AuthLogin.compare(data.header)) {
            String[] msgData = data.msgStr.split(":");
            System.out.println("Login Success: " + msgData[2]);
            sessionKey = msgData[0];
            username = msgData[1];
            loggedIn = true;
            return;
        }

        if (BaseHeader.AuthSignup.compare(data.header)) {
            String[] msgData = data.msgStr.split(":");
            System.out.println("Signup Success: " + msgData[2]);
            sessionKey = msgData[0];
            username = msgData[1];
            loggedIn = true;
            return;
        }

        if (BaseHeader.AuthError.compare(data.header)) {
            System.out.println("Auth Error: " + data.msgStr);
            return;
        }

        if (BaseHeader.CreateRoom.compare(data.header)) {
            System.out.println(data.msgStr);
            roomSet = true;
            return;
        }

        if (BaseHeader.JoinRoom.compare(data.header)) {
            System.out.println(data.msgStr);
            roomSet = true;
            return;
        }

        if (BaseHeader.RoomError.compare(data.header)) {
            System.out.println("Room Error: " + data.msgStr);
            return;
        }
    }

    /**
     * Closes the client and shuts down the socket.
     * This method should be called when the client is no longer needed.
     */
    public void close() throws Exception {
        sendSessionPacket(BaseHeader.Disconnect.value(), "Disconnecting...");

        // Shutdown the executor in addition to closing the socket
        if (executor != null) {
            executor.shutdown();
        }

        while (!executor.isShutdown()) {
            // Wait for the executor to finish
            Thread.sleep(100);
        }

        socket.close();
    }
}