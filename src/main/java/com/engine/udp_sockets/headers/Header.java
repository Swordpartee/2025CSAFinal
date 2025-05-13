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
  
  /**
   * @return the byte[] value of the enum
   */
  public byte[] value() {
  	return value;
  }
  
  /**
   * Compares the byte[] value of the enum to the given byte[] value.
   * @param other : the byte[] value to compare to
   * @return true if the byte[] values are equal, false otherwise
   */
  public boolean compare(byte[] other) {
		return Arrays.equals(value, other);
  }
  
  /**
   * Converts the given byte[] value to the corresponding enum constant.
   * @param value : the byte[] value to convert
   * @return the enum constant that matches the given byte[] value
   */
  public static Header from(byte[] value) {
    for (Header byteEnum : Header.values()) {
        if (byteEnum.compare(value)) {
            return byteEnum;
        }
    }
    
    throw new IllegalArgumentException("No enum constant with byte[] value " + value);
  }
  
  /**
   * Converts the given byte values to the corresponding enum constant.
   * @param byte1 : the first byte value
   * @param byte2 : the second byte value
   * @return the enum constant that matches the given byte values
   */
  public static Header from(byte byte1, byte byte2) {
  	return from(new byte[] {byte1, byte2});
  }
}
