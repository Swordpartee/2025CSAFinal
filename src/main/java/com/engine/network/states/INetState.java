package com.engine.network.states;

import java.io.DataInputStream;

import com.engine.network.headers.Header;

public interface INetState<T extends INetObject> {
  /**
   * Get the value of the state
   * @return The value of the state
   */
  T getValue();

  /**
   * Set the value of the state
   * @param value : The value to set
   */
  void setValue(T value);

  /**
   * Get the sync mode of the state
   * @return The sync mode of the state
   */
  ControlMode getControlMode();

  /**
   * Set the sync mode of the state
   * @param controlMode : The sync mode to set
   */
  void setControlMode(ControlMode controlMode);

  /**
   * Get the state header
   * @return The state header
   */
  Header getHeader();
  
  /**
   * Get the ID of the state
   * @return The ID of the state
   */
  String getId();

  /**
   * Set the ID of the state. !NOT RECOMMENDED!
   * @param id : The ID to set
   */
  void setId(String id);

  /**
   * Check if the state is itself, and not from the server
   * @return True if the state is itself, false if it is from the server
   */
  boolean isSelf();

  /**
   * Get the data to send to the server
   * @return The byte[] of data to send to the server
   * @throws Exception
   */
  byte[] getSendData() throws Exception;

  /**
   * Deserialize the data from the server
   * @param data : The data to deserialize
   * @throws Exception
   */
  void recieveData(DataInputStream data) throws Exception;

  /**
   * Send the state to the server, without having to use the ClientStateManager
   * @throws Exception
   */
  void sendSelf() throws Exception;

  /**
   * Delete the state from the server, without having to use the ClientStateManager
   * @throws Exception
   */
  void deleteSelf() throws Exception;
}
