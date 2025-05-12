package com.engine.udp_sockets.server;

import java.net.InetAddress;
import java.net.SocketAddress;

import javax.crypto.SecretKey;

public class SessionInfo {

  private String name;
  private boolean hasName = false;
  public String getName() { return name; }
  public void setName(String name) { this.name = name; this.hasName = true; }

  private String room;
  private boolean isInRoom = false;
  public String getRoom() { return room; }
  public void setRoom(String room) { this.room = room; this.isInRoom = true; }

  private SecretKey aesKey;
  public boolean isInAESSession = false;
  public SecretKey getAESKey() { return aesKey; }
  public void setAESKey(SecretKey aesKey) { this.aesKey = aesKey; this.isInAESSession = true; }

  private InetAddress address;
  public InetAddress getAddress() { return address; }

  private int port;
  public int getPort() { return port; }

  public SessionInfo(InetAddress address, int port) {
    this.address = address;
    this.port = port;
  }
}
