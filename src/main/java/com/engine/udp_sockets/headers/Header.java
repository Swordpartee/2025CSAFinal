package com.engine.udp_sockets.headers;

import java.util.Arrays;

public enum Header {
	AskInitialStateBundle((byte[]) new byte[] { 1, 0 }),
	InitialStateBundle((byte[]) new byte[] { 1, 1 }),
	StateChangeBundle((byte[]) new byte[] { 1, 1 }),
	
	IncreaseCookies((byte[]) new byte[] {1, 2});

	private final byte[] value;

  Header(byte[] value) {
      this.value = value;
  }
  
  public byte[] value() {
  	return value;
  }
  
  public boolean compare(byte[] other) {
		return Arrays.equals(value, other);
  }
  
  public static Header from(byte[] value) {
    for (Header byteEnum : Header.values()) {
        if (byteEnum.compare(value)) {
            return byteEnum;
        }
    }
    
    throw new IllegalArgumentException("No enum constant with byte[] value " + value);
  }
  
  public static Header from(byte byte1, byte byte2) {
  	return from(new byte[] {byte1, byte2});
  }
}
