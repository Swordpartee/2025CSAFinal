package com.engine.udp_sockets.encryption;

import java.nio.ByteBuffer;

public class Convert {
  public String btos(byte[] bytes) {
    return new String(bytes, 0, bytes.length);
  }

  public byte[] stob(String str) {
    return str.getBytes();
  }

  public int btoi(byte[] bytes) {
    ByteBuffer wrapped = ByteBuffer.wrap(bytes); // big-endian by default
    short num = wrapped.getShort(); // 1
    return num;
  }

  public byte[] itob(int num) {
    ByteBuffer buffer = ByteBuffer.allocate(2); // 2 bytes for short
    buffer.putShort((short) num);
    return buffer.array();
  }
}
