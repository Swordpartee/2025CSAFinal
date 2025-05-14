package com.engine.network.server;

import java.net.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.SecretKey;

import com.engine.network.encryption.Encryption;
import com.engine.network.encryption.HMACAuthenticator;
import com.engine.network.headers.BaseHeader;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Server {

    private DatagramSocket socket;

    private UserManager userManager = new UserManager();

    private PublicKey publicKey;
    private PrivateKey privateKey;

    private byte[] sendBuffer;
    private byte[] recvBuffer;

    private RecvFunc recv;

    private HashMap<String, String> rooms = new HashMap<String, String>();
    private HashMap<SocketAddress, SessionInfo> sessions = new HashMap<SocketAddress, SessionInfo>();

    /**
     * Creates a new server instance, and starts listening for incoming packets.
     * @param recv
     * @throws Exception
     */
    public Server(RecvFunc recv) throws Exception {
        this.recv = recv;
        
        socket = new DatagramSocket(4445);
        KeyPair keyPair = Encryption.generateRSAOAEPKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        System.out.println(Arrays.toString(userManager.getUsers("9*A#awjd893E*jf37ug$h", false)));
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(1);

    /**
     * Starts a new thread to handle the incoming packets.
     * @param localPacket
     */
    private void startRecvThread(DatagramPacket localPacket) {
        executor.submit(() -> {
            // Find or create the user's session
            SessionInfo sessionInfo = sessions.get(localPacket.getSocketAddress());
            if (sessionInfo == null) {
                sessionInfo = new SessionInfo(localPacket.getAddress(), localPacket.getPort());
                sessions.put(localPacket.getSocketAddress(), sessionInfo);
            }

            ServerPacketData data;
            
            try {
                data = new ServerPacketData(localPacket, sessionInfo, privateKey);
                if (!processPacketData(data)) { recv.run(data); }
            } catch (Exception e) { }
        });
    } 

    /**
     * Starts the server and begins listening for incoming packets.
     * @throws Exception
     */
    public void start() throws Exception {
        while (true) {
            recvBuffer = new byte[1024];
            DatagramPacket localPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
            socket.receive(localPacket);

            startRecvThread(localPacket);
        }
    }

    /**
     * Sends a packet to the specified address and port.
     * @param buffer
     * @param address
     * @param port
     * @throws Exception
     */
    private void sendPacket(byte[] buffer, InetAddress address, int port) throws Exception {
        sendBuffer = buffer;
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
        socket.send(sendPacket);
    }

    /**
     * Sends a packet to the specified address and port, encrypted with AES.
     * @param buffer
     * @param address
     * @param port
     * @param aesKey
     * @throws Exception
     */
    private void sendPacket(byte[] buffer, InetAddress address, int port, SecretKey aesKey) throws Exception {
        sendPacket(Encryption.encryptAES(Encryption.concatBytes(new byte[] {(byte) buffer.length}, buffer), aesKey), address, port);
    }

    /**
     * Sends a packet to the specified address and port, with a header, encrypted with AES.
     * @param header
     * @param buffer
     * @param address
     * @param port
     * @param aesKey
     * @throws Exception
     */
    private void sendPacket(byte[] header, byte[] buffer, InetAddress address, int port, SecretKey aesKey) throws Exception {
        sendPacket(Encryption.encryptAES(Encryption.concatBytes(header, new byte[] { (byte) buffer.length }, buffer), aesKey), address, port);
    }

    /**
     * Sends the packet with the header to each session in the room, encrypted with AES.
     * @param header
     * @param buffer
     * @param address
     * @param port
     * @throws Exception
     */
    public void sendSessionPacketToRoom(byte[] header, byte[] buffer, String room) throws Exception {
        ArrayList<SessionInfo> sessionInfos = getSessionsInRoom(room);
        for (SessionInfo info : sessionInfos) {
            sendPacket(header, buffer, info.getAddress(), info.getPort(), info.getAESKey());
        }
    }

    /**
     * Sends the packet with the header to the session, encrypted with AES.
     * @param header
     * @param buffer
     * @param sessionInfo
     * @throws Exception
     */
    public void sendSessionPacket(byte[] header, byte[] buffer, SessionInfo sessionInfo) throws Exception {
        sendPacket(header, buffer, sessionInfo.getAddress(), sessionInfo.getPort(), sessionInfo.getAESKey());
    }

    /**
     * Get all of the sessions in a room.
     * @param room
     * @return a list of session info objects
     */
    private ArrayList<SessionInfo> getSessionsInRoom(String room) {
        ArrayList<SessionInfo> sessionInfos = new ArrayList<SessionInfo>();

        for (SessionInfo info : sessions.values()) {
            if (info.getRoom().equals(room)) {
                sessionInfos.add(info);
            }
        }

        return sessionInfos;
    }

    /**
     * Process the incoming packet data, allowing for a recv thread made by the maker of this class to also be used.
     * @param data
     * @return true if the packet was fully processed, false otherwise
     * @throws Exception
     */
    private boolean processPacketData(ServerPacketData data) throws Exception {
        // PUBLIC KEY REQUEST
        if (BaseHeader.AskPublicKey.compare(data.header)) {
            System.out.println("Received request. Sending public key... " + data.address);

            String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            sendPacket(publicKeyBase64.getBytes(), data.pkt.getAddress(), data.pkt.getPort());
            return true;
        }

        // AES KEY CONFIRMATION
        if (BaseHeader.GiveAESKey.compare(data.header)) {
            SecretKey aesKey = Encryption.bytesToAESKey(data.msg);

            // Store our AES Session with that client
            sessions.get(data.address).setAESKey(aesKey);

            // Send a confirmation with our AES Key
            System.out.println("Got AES Key For " + data.address + ". Sending Confirmation");
            sendPacket("AES Confirmed".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), aesKey);
            return true;
        }

        if (BaseHeader.AuthLogin.compare(data.header)) {
            String username = data.msgStr.split(":", 2)[0];
            String password = data.msgStr.split(":", 2)[1];
            if (userManager.authenticateUser(username, password)) {
                sessions.get(data.address).setUsername(username);
                sessions.get(data.address).setSessionKey(HMACAuthenticator.generateSessionKey());
                sendPacket(BaseHeader.AuthLogin.value(),
                        (sessions.get(data.address).getSessionKey() + ":" + username + ":Success! You \"logged\" in!").getBytes(),
                        data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
            } else {
                sendPacket(BaseHeader.AuthError.value(), "Something went wrong logging in :(".getBytes(),
                        data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
            }
            return true;
        }

        if (BaseHeader.AuthSignup.compare(data.header)) {
            String username = data.msgStr.split(":", 2)[0];
            String password = data.msgStr.split(":", 2)[1];

            if (!username.matches("^[a-z0-9_]+$")) {
                sendSessionPacket(BaseHeader.AuthError.value(),
                        "Invalid username. Must contain only lowercase letters, numbers, or underscores.".getBytes(),
                        data.sessionInfo);
                return true;
            }

            if (password.length() < 6 || password.indexOf(" ") != -1) {
                sendSessionPacket(BaseHeader.AuthError.value(), "Invalid password. Must not contain spaces.".getBytes(),
                        data.sessionInfo);
                return true;
            }

            if (userManager.addUser(username, password)) {
                sessions.get(data.address).setUsername(username);
                sessions.get(data.address).setSessionKey(HMACAuthenticator.generateSessionKey());
                sendPacket(BaseHeader.AuthSignup.value(),
                        (sessions.get(data.address).getSessionKey() + ":" + username + ":Success! Your account has been made").getBytes(),
                        data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
            } else {
                sendPacket(BaseHeader.AuthError.value(), "Something went wrong making your account :(".getBytes(),
                        data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
            }
            return true;
        }

        if (!data.sessionInfo.hasUsername()) {
            return true;
        }

        if (!HMACAuthenticator.validateHMACToken(data.sessionInfo.getSessionKey(), data.sessionInfo.getUsername(), data.clientTime, data.clientHMAC, 300)) {
            return true;
        }

        if (BaseHeader.CreateRoom.compare(data.header)) {
            String[] roomData = data.msgStr.split(":", 2);
            String roomName = roomData[0];
            String password = roomData.length > 1 ? roomData[1] : null;

            if (password != null && password.indexOf(" ") != -1) {
                sendSessionPacket(BaseHeader.RoomError.value(), "Invalid room password. Must not contain spaces.".getBytes(), data.sessionInfo);
                return true;
            }

            if (!roomName.matches("^[a-z0-9_]+$")) {
                sendSessionPacket(BaseHeader.RoomError.value(), "Invalid room name. Must contain only lowercase letters, numbers, or underscores.".getBytes(), data.sessionInfo);
                return true;
            }

            if (rooms.keySet().contains(roomName)) {
                sendSessionPacket(BaseHeader.RoomError.value(), "Room already exists!".getBytes(), data.sessionInfo);
                return true;
            }

            rooms.put(roomName, password == null ? password : Encryption.hashString(password));

            sessions.get(data.address).setRoom(roomName);
            sendSessionPacket(BaseHeader.CreateRoom.value(), ("You created the room \"" + roomName + "\"!").getBytes(),
                    data.sessionInfo);
            return true;
        }

        if (BaseHeader.JoinRoom.compare(data.header)) {
            String[] roomData = data.msgStr.split(":");
            String roomName = roomData[0];
            String password = roomData.length > 1 ? roomData[1] : "";

            if (!rooms.keySet().contains(roomName)) {
                sendSessionPacket(BaseHeader.RoomError.value(), "Room doesn't exist!".getBytes(), data.sessionInfo);
                return true;
            }

            Argon2 argon2 = Argon2Factory.create();

            if (rooms.get(roomName) != null && !argon2.verify(rooms.get(roomName), password.getBytes())) {
                sendSessionPacket(BaseHeader.RoomError.value(), "Invalid Room password!".getBytes(), data.sessionInfo);
                return true;
            }

            sessions.get(data.address).setRoom(roomName);
            sendSessionPacket(BaseHeader.JoinRoom.value(), ("You joined the room \"" + roomName + "\"!").getBytes(),
                    data.sessionInfo);
            return true;
        }

        if (!data.sessionInfo.isInRoom()) {
            return true;
        }

        // // Check if the user that sent the packet is authenticated.
        // // (For now, I am just going to assume that the client is who it says it is.)
        // // (Until I figure out how to do initial certificates)
        // if (user not in auth users...) {
        // sendPacket(Headers.MsgFailed, "Not Authorized Yet!".getBytes(),
        // data.pkt.getAddress(), data.pkt.getPort(), data.aesKeyUsed);
        // }
        //

        if (BaseHeader.BackForthMsg.compare(data.header)) {
            sendPacket(BaseHeader.BackForthMsg.value(), data.msgStr.getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
            return true;
        }

        return false;
    }

    /**
     * Closes the server and shuts down the socket and executor.
     */
    public void close() {
        socket.close();

        if (executor != null) {
            executor.shutdown();
        }
    }
}
