package com.engine.network.runners;

import com.engine.network.headers.Header;
import com.engine.network.server.Server;
import com.engine.network.server.ServerPacketData;

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
