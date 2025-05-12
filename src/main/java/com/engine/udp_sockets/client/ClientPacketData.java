package client;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.Arrays;

import javax.crypto.SecretKey;

import encryption.Encryption;

public class ClientPacketData {
	public byte[] rawPktData;
	
	public byte[] header;
	
	public byte[] msg;
	public String msgStr;
	
	public DatagramPacket pkt;
	public SocketAddress address;
	
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
		msg = Arrays.copyOfRange(rawPktData, 2, rawPktData.length);
		msgStr = new String(msg, 0, msg.length);
	}
}

