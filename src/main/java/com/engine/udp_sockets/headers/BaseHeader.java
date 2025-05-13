package com.engine.udp_sockets.headers;

import java.util.Arrays;

public enum BaseHeader {
	AskPublicKey((byte[]) new byte[] { 0, 0 }),
	GiveAESKey((byte[]) new byte[] { 0, 1 }),
	
	AuthSignup((byte[]) new byte[] { 0, 2 }),
	AuthLogin((byte[]) new byte[] { 0, 3 }),
	AuthSignout((byte[]) new byte[] { 0, 4 }),
  AuthError((byte[]) new byte[] { 0, 5 }),
	
  JoinRoom((byte[]) new byte[] { 0, 6 }),
  CreateRoom((byte[]) new byte[] { 0, 7 }),
  LeaveRoom((byte[]) new byte[] { 0, 8 }),
  AskRoomList((byte[]) new byte[] { 0, 9 }),
  RoomError((byte[]) new byte[] { 0, 10 }),

	BackForthMsg((byte[]) new byte[] { 0, 11 }),
	MsgFailed((byte[]) new byte[] { 0, 12 });

	private final byte[] value;

  BaseHeader(byte[] value) {
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
  public static BaseHeader from(byte[] value) {
    for (BaseHeader byteEnum : BaseHeader.values()) {
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
  public static BaseHeader from(byte byte1, byte byte2) {
  	return from(new byte[] {byte1, byte2});
  }
}
