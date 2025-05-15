package com.engine.network.client;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.Arrays;

import javax.crypto.SecretKey;

import com.engine.network.encryption.Encryption;

public class ClientPacketData {
	public byte[] rawPktData;
	
	public byte[] header;
	
	public byte[] msg;
	public String msgStr;
	public byte[][] msgs;
	
	public DatagramPacket pkt;
	public SocketAddress address;
	
	/**
	 * Sets up all of the data for the packet, and what may or may not be needed in the processing of the packet.
	 * @param pkt
	 * @param aesSessionStarted
	 * @param aesKey
	 * @throws Exception
	 */
	public ClientPacketData(DatagramPacket pkt, boolean aesSessionStarted, SecretKey aesKey) throws Exception {
		// Store the actual packet just in case.
		this.pkt = pkt;
		this.address = pkt.getSocketAddress();
		
		// If the user is in an AES Session with the server
		if (aesSessionStarted) {
			rawPktData = Encryption.decryptAES(Encryption.getPacketBytes(pkt), aesKey);
		} else {
			// Otherwise, just try to get the packet data as bytes.
			rawPktData = Encryption.getPacketBytes(pkt);
		}
		
		header = Arrays.copyOfRange(rawPktData, 0, 2);
		msg = Arrays.copyOfRange(rawPktData, 3, rawPktData.length);
		msgs = Encryption.desegmentPacket(rawPktData);
		msgStr = new String(msg, 0, msg.length);
	}
}

