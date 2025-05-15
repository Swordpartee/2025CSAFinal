package com.engine.network.server;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.Arrays;

import com.engine.network.encryption.Convert;
import com.engine.network.encryption.Encryption;

public class ServerPacketData {
	public byte[] rawPktData;
	
	public byte[] header;
	
	public byte[] msg;
	public String msgStr;
	public byte[][] msgs;
	
	public DatagramPacket pkt;
	public SocketAddress address;

	public String clientHMAC;
	public long clientTime;

	public SessionInfo sessionInfo;
	
	/**
	 * Sets up all of the data for the packet, and what may or may not be needed in the processing of the packet.
	 * @param pkt
	 * @param sessionInfo
	 * @param privateKey
	 * @throws Exception
	 */
	public ServerPacketData(DatagramPacket pkt, SessionInfo sessionInfo, PrivateKey privateKey) throws Exception {
		// Store the actual packet just in case.
		this.pkt = pkt;
		this.address = pkt.getSocketAddress();
		this.sessionInfo = sessionInfo;
		
		// If the user is in an AES Session with the server
		if (sessionInfo.isInAESSession) {
			rawPktData = Encryption.decryptAES(Encryption.getPacketBytes(pkt), sessionInfo.getAESKey());
		} else {
  		try {
  			// If not in an AES Session, first try to decrypt it if its RSA encrypted
  			rawPktData = Encryption.decryptRSA(Encryption.getPacketBytes(pkt), privateKey);
  		} catch (Exception e) {
  			// Otherwise, just try to get the packet data as bytes.
  			rawPktData = Encryption.getPacketBytes(pkt);
  		}
		}

      
		header = Arrays.copyOfRange(rawPktData, 0, 2);
		msg = Arrays.copyOfRange(rawPktData, 3, rawPktData.length);
		msgStr = new String(msg, 0, msg.length);
		msgs = Encryption.desegmentPacket(rawPktData);

		if (sessionInfo.isLoggedIn()) {
			clientTime = Convert.btol(msgs[0]);
			clientHMAC = Convert.btos(msgs[1]);
			msgStr = Convert.btos(msgs[2]);
			msg = msgs[2];
			msgs = Arrays.copyOfRange(msgs, 2, msgs.length);
		}

		// for (int i = 0; i < msgs.length; i++) {
		// 	System.out.println("msg " + i + ": " + Convert.btos(msgs[i]));
		// }
	}
}
