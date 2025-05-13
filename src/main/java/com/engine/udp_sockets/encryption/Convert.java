package com.engine.udp_sockets.encryption;

import java.nio.ByteBuffer;

public class Convert {
  public static String btos(byte[] bytes) {
    return new String(bytes, 0, bytes.length);
  }

  public static byte[] stob(String str) {
    return str.getBytes();
  }

  public static int btoi(byte[] bytes) {
    ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
    short num = wrapped.getShort(); // 1
    return num;
  }

  public static byte[] itob(int num) {
    ByteBuffer buffer = ByteBuffer.allocate(2); // 2 bytes for short
    buffer.putShort((short) num);
    return buffer.array();
  }

  public static long btol(byte[] bytes) {
    ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
    long num = wrapped.getLong(); // 1
    return num;
  }
  
  public static byte[] ltob(long num) {
    ByteBuffer buffer = ByteBuffer.allocate(8); // 8 bytes for long
    buffer.putLong(num);
    return buffer.array();
  }
}
