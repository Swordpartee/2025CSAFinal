package com.engine.udp_sockets.server;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.SecretKey;

import com.engine.udp_sockets.encryption.Encryption;

public class ServerPacketData {
	public byte[] rawPktData;
	
	public byte[] header;
	
	public byte[] msg;
	public String msgStr;
	
	public DatagramPacket pkt;
	public SocketAddress address;

	public SessionInfo sessionInfo;
	
	public ServerPacketData(DatagramPacket pkt, SessionInfo sessionInfo, PrivateKey privateKey) throws Exception {
		// Store the actual packet just in case.
		this.pkt = pkt;
		this.address = pkt.getSocketAddress();
		
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
		msg = Arrays.copyOfRange(rawPktData, 2, rawPktData.length);
		msgStr = new String(msg, 0, msg.length);
	}
}
