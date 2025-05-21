package com.engine.network.runners;

import java.util.Scanner;

import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.headers.BaseHeader;
import com.engine.network.headers.Header;
import com.engine.network.states.ClientStateManager;

/**
 * This class is an example of how to use the Client class.
 * It connects to the server and sends packets to it, to show the process of logging into the server and creating or joining a room.
 * It also shows how to process the packets received from the server, with the processRecv method. @see #ClientRunner.processRecv
 * Finally, it shows how to make sure to stay in a sending loop with the server, to send messages back and forth.
 */
public class ClientRunner {
	public static void processRecv(ClientPacketData data) {
		try {
			// Example custom Header to process
			if (Header.InitialStateBundle.compare(data.header)) {
				System.out.println("Initialize States!");
				client.sendSessionPacket(Header.IncreaseCookies.value(), "Gimme more cookies".getBytes());
			}
			
			stateManager.tryReceiveStates(data);			
		} catch (Exception e) { }
	}
	
	public static final Scanner scan = new Scanner(System.in);
	public static Client client = null;
	public static ClientStateManager stateManager = null;
	// public static NetState<TestNetObject> intState = null;

	public static void main(String[] args) throws Exception {
		// Create a new client instance and connect to the server at "localhost:4445"
		client = new Client(ClientRunner::processRecv);
		client.connect("localhost", 4445);
		stateManager = new ClientStateManager(client);

		// intState = new NetState<TestNetObject>(Header.GameObjectState, stateManager, new TestNetObject("Cheese", 1000000));

		// Wait to go on until the client is connected
		while (!client.loggedIn()) {
			System.out.println("Enter your username and password: (username:password)");
			// Send the username and password to the server (username:password)
			client.sendSessionPacketAndWait(BaseHeader.AuthLogin.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthError.value()});
		}

		// Wait to go on until the client is in a room.
		while (!client.roomSet()) {
			System.out.println("Do you want to create or join room? (c/j)");
			String choice = scan.nextLine();
			if (choice.equals("c")) {
				System.out.println("Enter the room you want to create: (room name:room password)");
				client.sendSessionPacketAndWait(BaseHeader.CreateRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.CreateRoom.value(), BaseHeader.RoomError.value()});
				continue;
			} else if (choice.equals("j")) {
				System.out.println("Enter the room you want to join: (room name:room password)");
				client.sendSessionPacketAndWait(BaseHeader.JoinRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.RoomError.value()});
				continue;
			} else {
				System.out.println("Invalid choice. Please enter 'c' to create a room or 'j' to join a room.");
				continue;
			}
		}
		
		boolean connected = true;
		while (connected) {
  		try {
				// intState.sendSelf();
				client.sendDenseSessionPacket(BaseHeader.BackForthMsg.value(), new byte[][] { scan.nextLine().getBytes(), "Gimme more cookies".getBytes(), "Hi".getBytes() });
			} catch (Exception e) { System.out.println("Error: " + e); }
  	}
		scan.close();
	}
}
