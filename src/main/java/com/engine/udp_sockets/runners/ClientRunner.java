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
				client.sendPacket(Header.IncreaseCookies.value(), "Gimme more cookies");
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
		
		boolean connected = true;
		while (connected) {
  		try {
				client.sendPacket(BaseHeader.BackForthMsg.value(), scan.nextLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	}
		scan.close();
	}
}
