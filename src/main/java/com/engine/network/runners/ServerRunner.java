package com.engine.network.runners;

import com.engine.network.headers.Header;
import com.engine.network.server.Server;
import com.engine.network.server.ServerPacketData;
import com.engine.network.states.ClientStateManager;
import com.engine.network.states.NetState;
import com.engine.network.states.TestNetObject;

public class ServerRunner {
	
	public static int cookieCount = 0;
	
	public static Server server;

	public static ClientStateManager stateManager;
	
	// This method is called when a packet is received from the client
	// It processes the packet and sends a response back to the client
	public static void processRecv(ServerPacketData data) {
		try {
			if (Header.IncreaseCookies.compare(data.header)) {
				cookieCount++;
				System.out.println(server);
			}
			
			if (Header.InitialStateBundle.compare(data.header)) {
				System.out.println("Initialize States!");
			}

			if (Header.from(data.header).type() == Header.HeaderType.StateChange) {
				System.out.println("Received state change!");
				NetState<TestNetObject> state = NetState.fromSerializedData(Header.from(data.header), data.msg, stateManager, TestNetObject.class);

				server.sendSessionPacketToRoom(data.header, state.getSendData(), data.sessionInfo.getRoom(), data.address);
			}
		}
		catch (Exception e) { System.out.println(e); }
	}
	
	public static void main(String[] args) throws Exception {
		server = new Server(ServerRunner::processRecv);
		server.start();

		stateManager = new ClientStateManager(null);
	}
}
