package com.engine.network.server;

import java.net.InetAddress;
import javax.crypto.SecretKey;

public class SessionInfo {

  private String username;
  private boolean hasUsername = false;
  public String getUsername() { return username; }
  public boolean hasUsername() { return hasUsername; }
  public void setUsername(String username) { this.username = username; this.hasUsername = true; }

  private String sessionKey;
  private boolean hasSessionKey = false;
  public String getSessionKey() { return sessionKey; }
  public boolean hasSessionKey() { return hasSessionKey; }
  public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; this.hasSessionKey = true; this.loggedIn = true; }

  private boolean loggedIn = false;
  public boolean isLoggedIn() { return loggedIn; }

  private String room;
  private boolean isInRoom = false;
  public String getRoom() { return room; }
  public boolean isInRoom() { return isInRoom; }
  public void setRoom(String room) { this.room = room; this.isInRoom = true; }

  private SecretKey aesKey;
  public boolean isInAESSession = false;
  public SecretKey getAESKey() { return aesKey; }
  public boolean isInAESSession() { return isInAESSession; }
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
