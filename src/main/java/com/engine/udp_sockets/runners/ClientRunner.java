package com.engine.udp_sockets.runners;

import java.util.Arrays;
import java.util.Scanner;

import com.engine.udp_sockets.client.Client;
import com.engine.udp_sockets.client.ClientPacketData;
import com.engine.udp_sockets.headers.BaseHeader;
import com.engine.udp_sockets.headers.Header;

public class ClientRunner {
	
	
	
	public static void processRecv(ClientPacketData data) {
		try {
			if (Header.InitialStateBundle.compare(data.header)) {
				System.out.println("Initialize States!");
				client.sendSessionPacket(Header.IncreaseCookies.value(), "Gimme more cookies".getBytes());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final Scanner scan = new Scanner(System.in);
	public static Client client = null;
	
	public static void main(String[] args) throws Exception {
		client = new Client(ClientRunner::processRecv, scan);
		client.connect("localhost", 4445);


		while (!client.nameSet()) {
			System.out.println("Enter your username:");
			client.sendSessionPacketAndWait(BaseHeader.AuthLogin.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthError.value()});
		}

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
				client.sendSessionPacket(BaseHeader.BackForthMsg.value(), scan.nextLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	}
		scan.close();
	}
}
