package com.engine.network.states;

import com.engine.network.headers.Header;

public interface IClientState<T> {
  T getValue();
  void setValue(T value);

  SyncMode getSyncMode();
  void setSyncMode(SyncMode syncMode);

  Header getHeader();

  void recieveData(T data);

  int getId();

  byte[] getSendData();
}
