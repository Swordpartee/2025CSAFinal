package com.engine.udp_sockets.client;

import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKey;

import com.engine.udp_sockets.encryption.Encryption;
import com.engine.udp_sockets.headers.BaseHeader;

import java.io.*;

public class Client {
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
	private volatile boolean connected;
	private volatile boolean nameSet = false;
	private volatile boolean roomSet = false;
	public boolean nameSet() { return nameSet; }
	public boolean roomSet() { return roomSet; }
  
  private RecvFunc recv;

	private volatile boolean recievedWaitForHeader = false;
	private volatile byte[][] waitForHeaders;
  
  private void requestPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
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
  
  private boolean genAndSendAESKey() throws Exception {
  	System.out.println("Sending AES Key...");
  	
  	this.aesKey = Encryption.generateAESKey();
  	
  	byte[] msg = Encryption.concatBytes(BaseHeader.GiveAESKey.value(), aesKey.getEncoded());
  	byte[] encryptedAESKey = Encryption.encryptRSA(msg, publicKey);
  	DatagramPacket packet = new DatagramPacket(encryptedAESKey, encryptedAESKey.length, address, this.port);
  	socket.send(packet);

    byte[] confirmMsg = new byte[256];
    packet = new DatagramPacket(confirmMsg, confirmMsg.length);
    socket.receive(packet);
    
    byte[] pktData = Encryption.getPacketBytes(packet);
    byte[] decryptedBytes = Encryption.decryptAES(pktData, aesKey);
    String decryptedMsg = new String(decryptedBytes, 0, decryptedBytes.length);
    
    if (decryptedMsg.equals("AES Confirmed")) {
    	System.out.println("AES Session Started Successfully!");
    	aesSessionStarted = true;
    	socket.setSoTimeout(0);
    	return true;
    }
    System.out.println("Unsuccessful AES Session Start. Trying Again...");
    return false;
  }
  
  private void tryGetUser(Scanner scan) throws Exception {
  	System.out.println("Do you wish to login or signup?");
  	
   	while (true) {  		
	   	String choice = scan.nextLine();
	   	if (choice.equals("login")) {
	   			System.out.println("Please Enter Your Username: ");
	   			String username = scan.nextLine();
	   			System.out.println("Please Enter Your Password: ");
	   			String password = scan.nextLine();
	   			this.sendSessionPacket(BaseHeader.AuthLogin.value(), username + " " + password);
	   			break;
	   	}
	   	else if (choice.equals("signup")) {
	   			System.out.println("Please Enter Your Username: ");
	   			String username = scan.nextLine();
	   			System.out.println("Please Enter Your Password: ");
	   			String password = scan.nextLine();
	   			this.sendSessionPacket(BaseHeader.AuthSignup.value(), username + " " + password);
	   			break;
	   	} else {
	   		System.out.println("Not a valid choice, try again: (login, signup)");
	   	}
   	}
  }
  
  public Client(RecvFunc recv, Scanner scan) throws Exception {
  	// Setup server
    this.recv = recv;
  }
  
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
    
    // Start the client's receiving thread
    new ClientRecvThread().start();

    // tryGetUser(scan);
    
    // // Wait until connected
    // while (!connected) {
    	
    // }
  }

	private void sendPacket(byte[] buffer, InetAddress address, int port) throws Exception {  		
		sendBuffer = buffer;
		sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
		socket.send(sendPacket);
	}

	public void sendSessionPacket(byte[] header, byte[] msg) throws Exception {
		sendBuffer = Encryption.encryptAES(Encryption.concatBytes(header, msg), aesKey);
		DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, this.port);
		socket.send(sendPacket);
	}
	
	public void sendSessionPacketAndWait(byte[] header, byte[] msg, byte[][] waitForHeaders) throws Exception {
		sendSessionPacket(header, msg);
		recievedWaitForHeader = false;
		this.waitForHeaders = waitForHeaders;

		while (!recievedWaitForHeader) { Thread.sleep(100); }
	}
	
	private void processPacketData(ClientPacketData data) throws Exception {
		if (waitForHeaders != null) {
			for (byte[] header : waitForHeaders) {
				if (Arrays.equals(header, data.header)) {
					recievedWaitForHeader = true;
					waitForHeaders = null;
					break;
				}
			}
		}

		if (Arrays.equals(BaseHeader.BackForthMsg.value(), data.header)) {
			System.out.println("Back Forth Msg: " + data.msgStr);
		}
		
		// if (Arrays.equals(BaseHeader.AuthLogin.value(), data.header)) {
		// 	System.out.println("Login Msg: " + data.msgStr);
		// 	// Ask again for the user when something goes wrong
		// 	if (data.msgStr.equals("Something went wrong logging in :(")) {
		// 		tryGetUser(threadScan);
		// 		return;
		// 	}
		// 	connected = true;
		// }
		
		// if (Arrays.equals(BaseHeader.AuthSignup.value(), data.header)) {
		// 	System.out.println("Signup Msg: " + data.msgStr);
		// 	// Ask again for the user when something goes wrong
		// 	if (data.msgStr.equals("Something went wrong making your account :(")) {
		// 		tryGetUser(threadScan);
		// 		return;
		// 	}
		// 	connected = true;
		// }

		if (BaseHeader.AuthLogin.compare(data.header)) {
			System.out.println(data.msgStr);
			nameSet = true;
		}

		if (BaseHeader.AuthError.compare(data.header)) {
			System.out.println("Auth Error: " + data.msgStr);
		}

		if (BaseHeader.CreateRoom.compare(data.header)) {
			System.out.println(data.msgStr);
			roomSet = true;
		}

		if (BaseHeader.JoinRoom.compare(data.header)) {
			System.out.println(data.msgStr);
			roomSet = true;
		}

		if (BaseHeader.RoomError.compare(data.header)) {
			System.out.println("Room Error: " + data.msgStr);
		}
		
		recv.run(data);
	}
  
  class ClientRecvThread extends Thread {
  	private Scanner threadScan = new Scanner(System.in);
  	
  	public void run() {
  		try {
    		
        boolean running = true;

        while (running) {
      		// Receive Public key request
      	
		    	recvBuffer = new byte[1024];
		      recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
		      socket.receive(recvPacket);
		      
		      ClientPacketData data = new ClientPacketData(recvPacket, aesSessionStarted, aesKey);
//  		      System.out.println("Bytes: " + Arrays.toString(data.msg));
		      
//		      for (Field f : Headers.class.getFields()) {
//		      	byte[] fieldBytes = new byte[2];
//		      	if (Arrays.equals((byte[]) f.get(fieldBytes), data.header)) {
//		      		System.out.println("Header: " + f.getName());
//		      	}
//		      }
//		      System.out.println("Msg: " + data.msgStr);
		      
		      processPacketData(data);
        }
        socket.close();
        
    	} catch (Exception e) {
    		threadScan.close();
    		System.err.println(e);
    	}
  		threadScan.close();
  	}
  }

  public void sendSessionPacket(byte[] header, String msg) throws Exception {
  	sendBuffer = Encryption.encryptAES(Encryption.concatBytes(header, msg.getBytes()), aesKey);
//  	System.out.println("Decrypt: " + Arrays.toString(Encryption.decryptAES(sendBuffer, aesKey)));
//  	System.out.println("MSG: " + Arrays.toString(sendBuffer) + " LEN: " + sendBuffer.length);
  	DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, this.port);
    socket.send(sendPacket);
  }

  public void close() {
  	socket.close();
  }
}


