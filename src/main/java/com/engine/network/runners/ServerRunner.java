// package com.engine.network.runners;

// import com.engine.network.headers.Header;
// import com.engine.network.server.Server;
// import com.engine.network.server.ServerPacketData;

// public class ServerRunner {
	
// 	public static int cookieCount = 0;
	
// 	public static Server server;
	
// 	public static int msgs = 0;

// 	// This method is called when a packet is received from the client
// 	// It processes the packet and sends a response back to the client
// 	public static void processRecv(ServerPacketData data) {
// 		msgs++;
// 		if (msgs % 100000 == 0) {
// 			System.out.println("Received " + msgs + " packets");
// 		}
// 		try {
// 			if (Header.IncreaseCookies.compare(data.header)) {
// 				cookieCount++;
// 				System.out.println(server);
// 			}
			
// 			if (Header.InitialStateBundle.compare(data.header)) {
// 				System.out.println("Initialize States!");
// 			}

// 			if (Header.from(data.header).type() == Header.HeaderType.StateChange) {
// 				if (data.msgs.length > 1) {
// 					server.sendDenseSessionPacketToRoom(data.header, data.msgs, data.sessionInfo.getRoom(), data.address);
// 					return;
// 				}



// 				server.sendSessionPacketToRoom(data.header, data.msg, data.sessionInfo.getRoom(), data.address);
// 			}

// 			if (Header.from(data.header).type() == Header.HeaderType.StateDelete) {

// 				if (data.msgs.length > 1) {
// 					server.sendDenseSessionPacketToRoom(data.header, data.msgs, data.sessionInfo.getRoom(), data.address);
// 					return;
// 				}
// 				// System.out.println("Received state delete!");
// 				server.sendSessionPacketToRoom(data.header, data.msg, data.sessionInfo.getRoom(), data.address);
// 			}
// 		}
// 		catch (Exception e) { System.out.println("Server Error: " + e); }
// 	}
	
// 	public static void main(String[] args) throws Exception {
// 		server = new Server(ServerRunner::processRecv);
// 		server.start();
// 	}
// }
