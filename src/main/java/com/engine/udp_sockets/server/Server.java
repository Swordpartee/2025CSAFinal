package com.engine.udp_sockets.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import com.engine.udp_sockets.encryption.Encryption;
import com.engine.udp_sockets.headers.BaseHeader;


public class Server {

  private DatagramSocket socket;
  
  private PublicKey publicKey;
  private PrivateKey privateKey;
  
  private byte[] sendBuffer;
  private byte[] recvBuffer;
  
  private DatagramPacket sendPacket;
  private DatagramPacket recvPacket;
  
  private RecvFunc recv;
  
	private ArrayList<String> rooms = new ArrayList<String>();
	private HashMap<SocketAddress, SessionInfo> sessions = new HashMap<SocketAddress, SessionInfo>();

  public Server(RecvFunc recv) throws Exception {
  		this.recv = recv;
  	
      socket = new DatagramSocket(4445);
      KeyPair keyPair = Encryption.generateRSAOAEPKeyPair();
      this.publicKey = keyPair.getPublic();
      this.privateKey = keyPair.getPrivate();
      
      System.out.println(Arrays.toString(getUsers("9*A#awjd893E*jf37ug$h", false).toArray()));
  }

  public void start() {
  	new ServerRecvThread().start();
  }

	private void sendPacket(byte[] buffer, InetAddress address, int port) throws Exception {
		sendBuffer = buffer;
		sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
		socket.send(sendPacket);
	}
	
	private void sendPacket(byte[] buffer, InetAddress address, int port, SecretKey aesKey) throws Exception {
		sendPacket(Encryption.encryptAES(buffer, aesKey), address, port);
	}
	
	private void sendPacket(byte[] header, byte[] buffer, InetAddress address, int port, SecretKey aesKey) throws Exception {
		sendPacket(Encryption.encryptAES(Encryption.concatBytes(header, buffer), aesKey), address, port);
	}
	
	private void sendPacket(byte[] header, byte[] buffer, InetAddress address, int port) throws Exception {
		sendPacket(Encryption.concatBytes(header, buffer), address, port);
	}
	
	private void sendPacketToAll(byte[] buffer) throws Exception {
		sendBuffer = buffer;
		
		for (SessionInfo info : sessions.values()) {
			sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, info.getAddress(), info.getPort());
			socket.send(sendPacket);
		}
	}

	public void sendSessionPacketToRoom(byte[] header, byte[] buffer, String room) throws Exception {
		ArrayList<SessionInfo> sessionInfos = getSessionsInRoom(room);
		for (SessionInfo info : sessionInfos) {
			sendPacket(header, buffer, info.getAddress(), info.getPort(), info.getAESKey());
		}
	}

	public void sendSessionPacket(byte[] header, byte[] buffer, SessionInfo sessionInfo) throws Exception {
		sendPacket(header, buffer, sessionInfo.getAddress(), sessionInfo.getPort(), sessionInfo.getAESKey());
	}

	private ArrayList<SessionInfo> getSessionsInRoom(String room) {
		ArrayList<SessionInfo> sessionInfos = new ArrayList<SessionInfo>();
		
		for (SessionInfo info : sessions.values()) {
			if (info.getRoom().equals(room)) {
				sessionInfos.add(info);
			}
		}
		
		return sessionInfos;
	}
	
	private void processPacketData(ServerPacketData data) throws Exception {
		// PUBLIC KEY REQUEST
		if (BaseHeader.AskPublicKey.compare(data.header)) {
			System.out.println("Received request. Sending public key... " + recvPacket.getSocketAddress());
			
			String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			sendPacket(publicKeyBase64.getBytes(), data.pkt.getAddress(), data.pkt.getPort());
			return;
		}
		
		// AES KEY CONFIRMATION
		if (BaseHeader.GiveAESKey.compare(data.header)) {
			SecretKey aesKey = Encryption.bytesToAESKey(data.msg);
			
			// Store our AES Session with that client
			sessions.get(data.address).setAESKey(aesKey);
			
			// Send a confirmation with our AES Key
			System.out.println("Got AES Key For " + data.address + ". Sending Confirmation");
			sendPacket("AES Confirmed".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), aesKey);
			return;
		}
		
		// if (BaseHeader.AuthLogin.compare(data.header)) {
		// 	String username = data.msgStr.split(" ")[0];
		// 	String password = data.msgStr.split(" ")[1];
		// 	if (authenticateUser(username, password)) {
		// 		sendPacket(BaseHeader.AuthLogin.value(), "Success! You \"logged\" in!".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
		// 	} else {
		// 		sendPacket(BaseHeader.AuthLogin.value(), "Something went wrong logging in :(".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
		// 	}
		// 	return;
		// }
		
		// if (BaseHeader.AuthSignup.compare(data.header)) {
		// 	String username = data.msgStr.split(" ")[0];
		// 	String password = data.msgStr.split(" ")[1];
		// 	if (addUser(username, password)) {
		// 		sendPacket(BaseHeader.AuthSignup.value(), "Success! Your account has been made".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
		// 	} else {
		// 		sendPacket(BaseHeader.AuthSignup.value(), "Something went wrong making your account :(".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
		// 	}
		// 	return;
		// }

		if (BaseHeader.AuthLogin.compare(data.header)) {
			String username = data.msgStr;
			
			if (!username.matches("^[a-z]+$")) {
				sendSessionPacket(BaseHeader.AuthError.value(), "Invalid username. Must be all lowercase letters with no special characters or spaces.".getBytes(), data.sessionInfo);
				return;
			}

			sessions.get(data.address).setName(username);
			sendSessionPacket(BaseHeader.AuthLogin.value(), ("You logged in as \"" + username + "\"!").getBytes(), data.sessionInfo);
			return;
		}

		if (!data.sessionInfo.hasName()) {
			return;
		}

		if (BaseHeader.CreateRoom.compare(data.header)) {
			String roomName = data.msgStr;
			
			if (!roomName.matches("^[a-z]+$")) {
				sendSessionPacket(BaseHeader.RoomError.value(), "Invalid room name. Must be all lowercase letters with no special characters or spaces.".getBytes(), data.sessionInfo);
				return;
			}
			
			if (rooms.contains(roomName)) {
				sendSessionPacket(BaseHeader.RoomError.value(), "Room already exists!".getBytes(), data.sessionInfo);
				return;
			}
			
			rooms.add(roomName);

			sessions.get(data.address).setRoom(roomName);
			sendSessionPacket(BaseHeader.CreateRoom.value(), ("You created the room \"" + roomName + "\"!").getBytes(), data.sessionInfo);
			return;
		}

		if (BaseHeader.JoinRoom.compare(data.header)) {
			String roomName = data.msgStr;
			
			if (!rooms.contains(roomName)) {
				sendSessionPacket(BaseHeader.RoomError.value(), "Room doesn't exist!".getBytes(), data.sessionInfo);
				return;
			}
			
			sessions.get(data.address).setRoom(roomName);
			sendSessionPacket(BaseHeader.JoinRoom.value(), ("You joined the room \"" + roomName + "\"!").getBytes(), data.sessionInfo);
			return;
		}

		if (rooms.contains(data.sessionInfo.getRoom())) {
			return;
		}

		
//  		// Check if the user that sent the packet is authenticated.
//  		// (For now, I am just going to assume that the client is who it says it is.)
//  		// (Until I figure out how to do initial certificates)
//  		if (user not in auth users...) {
//  			sendPacket(Headers.MsgFailed, "Not Authorized Yet!".getBytes(), data.pkt.getAddress(), data.pkt.getPort(), data.aesKeyUsed);
//  		}
//  		
		
		if (BaseHeader.BackForthMsg.compare(data.header)) {
			sendPacket(BaseHeader.BackForthMsg.value(), data.msg, data.pkt.getAddress(), data.pkt.getPort(), data.sessionInfo.getAESKey());
		}
		
		recv.run(data);
	}
  
  class ServerRecvThread extends Thread {
  	public void run() {
  		try {
    		
        boolean running = true;

        while (running) {
		    	recvBuffer = new byte[1024];
		      recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);
		      socket.receive(recvPacket);

					// Find or create the user's session
					SessionInfo sessionInfo = sessions.get(recvPacket.getSocketAddress());
					if (sessionInfo == null) {
						sessionInfo = new SessionInfo(recvPacket.getAddress(), recvPacket.getPort());
						sessions.put(recvPacket.getSocketAddress(), sessionInfo);
					}

		      ServerPacketData data = new ServerPacketData(recvPacket, sessionInfo, privateKey);

		      processPacketData(data);
        }
        socket.close();
    	} catch (IOException e) {
    		System.out.println("Server crash. :(");
    		socket.close();
    	} catch (Exception e) {
  			e.printStackTrace();
  			socket.close();
  		}
  	}
  }
  
  private final String CONFIG_PATH = System.getenv("APPDATA") + "\\Java Server\\config.json";
  private final SecretKey CONFIG_KEY = new SecretKeySpec(Base64.getDecoder().decode(System.getenv("AES_ECLIPSE_SECRET_KEY")), "AES");
  
  private ArrayList<String> getUsers(String password, boolean sorted) throws Exception {
  	if (authenticateUser("admin", password)) {
	  	String content = new String(Encryption.decryptAES(Files.readAllBytes(Paths.get(CONFIG_PATH)), CONFIG_KEY));
	  	JSONObject json = new JSONObject(content);
	  	JSONObject users = json.getJSONObject("users");
	  	
	  	@SuppressWarnings("unchecked")
			Iterator<String> keys = sorted ? users.sortedKeys() : users.keys();
	  	
	  	ArrayList<String> userList = new ArrayList<String>();
	  	while (keys.hasNext()) {
        userList.add(keys.next());
	  	}
	  	
	  	return userList;
  	}
  	System.out.println("Not Authorized!");
  	return new ArrayList<String>();
  }
  
  private boolean deleteUser(String username, String password) throws Exception {
  	if (username.equals("admin")) {
  		System.out.println("You cannot delete this user!");
  	}
  	
  	if (authenticateUser("admin", password) || authenticateUser(username, password)) {
	  	String content = new String(Encryption.decryptAES(Files.readAllBytes(Paths.get(CONFIG_PATH)), CONFIG_KEY));
	  	JSONObject json = new JSONObject(content);
	  	JSONObject users = json.getJSONObject("users");
  	
  		users.remove(username);
  		Files.write(Paths.get(CONFIG_PATH), Encryption.encryptAES(json.toString(4).getBytes(), CONFIG_KEY), StandardOpenOption.TRUNCATE_EXISTING);
  		System.out.println("User deleted!");
  		return true;
  	}
  	
  	System.out.println("You aren't authorized to do that!");
  	return false;
  }
  
  /** @description Auth users */
  private boolean authenticateUser(String username, String password) throws Exception {
  	String content = new String(Encryption.decryptAES(Files.readAllBytes(Paths.get(CONFIG_PATH)), CONFIG_KEY));
  	JSONObject json = new JSONObject(content);
  	JSONObject users = json.getJSONObject("users");
  	
  	if (!users.has(username)) {
  		System.out.println("User doesn't exist!");
  		return false;
  	}
  	
  	String storedHash = users.getString(username);
  	Argon2 argon2 = Argon2Factory.create();
  	
  	if (argon2.verify(storedHash, password.getBytes())) {
  		return true;
  	}
  	
  	return false;
  }
  
  private boolean addUser(String username, String password) throws Exception {
  	try {
  		String content = new String(Encryption.decryptAES(Files.readAllBytes(Paths.get(CONFIG_PATH)), CONFIG_KEY));
	  	JSONObject json = new JSONObject(content);
	  	JSONObject users = json.getJSONObject("users");
	  	
	  	// Hash the password
	  	Argon2 argon2 = Argon2Factory.create();
	    String hashedPassword = argon2.hash(2, 65536, 1, password.getBytes());
	  	
	    if (!users.has(username)) {
	    	users.put(username, hashedPassword);
	    	
	    	Files.write(Paths.get(CONFIG_PATH), Encryption.encryptAES(json.toString(4).getBytes(), CONFIG_KEY), StandardOpenOption.TRUNCATE_EXISTING);
	    	
	    	System.out.println("User " + username + " added!");
	    	return true;
	    }
	    System.out.println("User already exists!");
	    return false;
  	} catch (Exception e) {
  		e.printStackTrace();
  		System.out.println("Something went wrong trying to add a user!");
  		return false;
  	}
  }
}
