package com.engine.udp_sockets.runners;

import com.engine.udp_sockets.server.Server;
import com.engine.udp_sockets.server.ServerPacketData;
import com.engine.udp_sockets.headers.Header;

public class ServerRunner {
	
	public static int cookieCount = 0;
	
	public static Server server;
	
	// This method is called when a packet is received from the client
	// It processes the packet and sends a response back to the client
	public static void processRecv(ServerPacketData data) {
		if (Header.IncreaseCookies.compare(data.header)) {
			cookieCount++;
			System.out.println(server);
		}
		
		if (Header.AskInitialStateBundle.compare(data.header)) {
			System.out.println("Initialize States!");
		}
	}
	
	public static void main(String[] args) throws Exception {
		server = new Server(ServerRunner::processRecv);
		server.start();
	}
}
