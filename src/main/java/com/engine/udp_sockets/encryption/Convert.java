package com.engine.udp_sockets.encryption;

import java.nio.ByteBuffer;

public class Convert {
  /**
   * Convert a byte array to a string.
   * @param bytes : the byte array to convert
   * @return The string representation of the byte array
   */
  public static String btos(byte[] bytes) {
    return new String(bytes, 0, bytes.length);
  }

  /**
   * Convert a string to a byte array.
   * @param str : the string to convert
   * @return The byte array representation of the string
   */
  public static byte[] stob(String str) {
    return str.getBytes();
  }

  /**
   * Convert a byte array to a int.
   * @param bytes : the byte array to convert
   * @return The int representation of the byte array
   */
  public static int btoi(byte[] bytes) {
    ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
    short num = wrapped.getShort(); // 1
    return num;
  }

  /**
   * Convert a int to a byte array.
   * @param num : the int to convert
   * @return The byte array representation of the int
   */
  public static byte[] itob(int num) {
    ByteBuffer buffer = ByteBuffer.allocate(2); // 2 bytes for short
    buffer.putShort((short) num);
    return buffer.array();
  }

  /**
   * Convert a byte array to a long.
   * @param bytes : the byte array to convert
   * @return The long representation of the byte array
   */
  public static long btol(byte[] bytes) {
    ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
    long num = wrapped.getLong(); // 1
    return num;
  }

  /**
   * Convert a long to a byte array.
   * @param num : the long to convert
   * @return The byte array representation of the long
   */
  public static byte[] ltob(long num) {
    ByteBuffer buffer = ByteBuffer.allocate(8); // 8 bytes for long
    buffer.putLong(num);
    return buffer.array();
  }
}
