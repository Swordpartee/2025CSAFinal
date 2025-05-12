package com.engine.udp_sockets.headers;

import java.util.Arrays;

public enum BaseHeader {
	AskPublicKey((byte[]) new byte[] { 0, 0 }),
	GiveAESKey((byte[]) new byte[] { 0, 1 }),
	
	AuthSignup((byte[]) new byte[] { 0, 2 }),
	AuthLogin((byte[]) new byte[] { 0, 3 }),
	AuthSignout((byte[]) new byte[] { 0, 4 }),
	
	BackForthMsg((byte[]) new byte[] { 0, 5 }),
	MsgFailed((byte[]) new byte[] { 0, 6 });

	private final byte[] value;

  BaseHeader(byte[] value) {
      this.value = value;
  }
  
  public byte[] value() {
  	return value;
  }
  
  public boolean compare(byte[] other) {
		return Arrays.equals(value, other);
  }
  
  public static BaseHeader from(byte[] value) {
    for (BaseHeader byteEnum : BaseHeader.values()) {
        if (byteEnum.compare(value)) {
            return byteEnum;
        }
    }
    
    throw new IllegalArgumentException("No enum constant with byte[] value " + value);
  }
  
  public static BaseHeader from(byte byte1, byte byte2) {
  	return from(new byte[] {byte1, byte2});
  }
}
