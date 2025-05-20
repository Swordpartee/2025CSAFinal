package com.engine.network.headers;

import java.util.Arrays;

public enum Header {
  InitialStateBundle((byte[]) new byte[] { 1, 1 }, HeaderType.StateAdd),
  StateChangeBundle((byte[]) new byte[] { 1, 1 }, HeaderType.StateChange),
  IncreaseCookies((byte[]) new byte[] { 1, 2 }, HeaderType.Other),
  GameObjectState((byte[]) new byte[] { 1, 3 }, HeaderType.StateChange),
  PlayerState((byte[]) new byte[] { 1, 4 }, HeaderType.StateChange),
  DeleteState((byte[]) new byte[] { 1, 5 }, HeaderType.StateDelete),
  ProjectileState((byte[]) new byte[] { 1, 6 }, HeaderType.StateChange);

  private final byte[] value;
  private final HeaderType type;

  Header(byte[] value, HeaderType type) {
    this.value = value;
    this.type = type;
  }

  /**
   * @return the byte[] value of the enum
   */
  public byte[] value() {
    return value;
  }

  /**
   * @return the HeaderType of the enum
   */
  public HeaderType type() {
    return type;
  }

  /**
   * Compares the byte[] value of the enum to the given byte[] value.
   * @param other : the byte[] value to compare to
   * @return true if the byte[] values are equal, false otherwise
   */
  public boolean compare(byte[] other) {
    return Arrays.equals(value, other);
  }

  public boolean compare(Header other) {
    return this.compare(other.value);
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

    return null;

    // throw new IllegalArgumentException("No enum constant with byte[] value " + value);
  }

  /**
   * Converts the given byte values to the corresponding enum constant.
   * @param byte1 : the first byte value
   * @param byte2 : the second byte value
   * @return the enum constant that matches the given byte values
   */
  public static Header from(byte byte1, byte byte2) {
    return from(new byte[] { byte1, byte2 });
  }

  public enum HeaderType {
    StateChange,
    StateAdd,
    StateDelete,
    Other
  }
}
