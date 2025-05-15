package com.engine.network.states;

import java.io.DataInputStream;

import com.engine.network.headers.Header;

public interface INetState<T> {
  T getValue();
  void setValue(T value);

  SyncMode getSyncMode();
  void setSyncMode(SyncMode syncMode);

  Header getHeader();
  String getId();

  byte[] getSendData() throws Exception;
  void recieveData(DataInputStream data) throws Exception;

  void sendSelf() throws Exception;
}
