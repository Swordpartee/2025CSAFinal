package com.engine.udp_sockets.runners;

import java.util.Arrays;

import com.engine.udp_sockets.client.ClientPacketData;
import com.engine.udp_sockets.server.Server;
import com.engine.udp_sockets.server.ServerPacketData;
import com.engine.udp_sockets.headers.BaseHeader;
import com.engine.udp_sockets.headers.Header;

public class ServerRunner {
	
	public static int cookieCount = 0;
	
	public static Server server;
	
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
//		System.out.println(BaseHeader.AskPublicKey.compare(new byte[] {0, 0}));
		server = new Server(ServerRunner::processRecv);
		server.start();
	}
}
