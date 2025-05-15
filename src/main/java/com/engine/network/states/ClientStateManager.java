package com.engine.network.states;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.encryption.Convert;
import com.engine.network.headers.Header;

public class ClientStateManager {
  public static final String SEGMENT_DELIMITER = "::";

  private final HashMap<String, INetState<?>> stateIDMap = new HashMap<>(); // Allow for getting states by ID
  private final Client client;

  private INetState<?>[] getAllStatesWithHeader(Header lookupHeader) {
    INetState<?>[] states = Arrays.stream(stateIDMap.values().toArray(new INetState<?>[0]))
      .filter(state -> state.getHeader().compare(lookupHeader)).toArray(INetState<?>[]::new);
    return states;
  }

  public ClientStateManager(Client client) {
    this.client = client;
  }

  public <T> boolean addState(INetState<T> state) {
    if (stateIDMap.containsKey(state.getId())) { return false; }
    stateIDMap.put(state.getId(), state);

    return true;
  } 

  public <T> boolean removeState(INetState<T> state) {
    if (!stateIDMap.containsKey(state.getId())) { return false; }
    stateIDMap.remove(state.getId());
    return true;
  }

  public <T> void sendState(INetState<T> state) throws Exception {
    client.sendSessionPacket(state.getHeader().value(), state.getSendData());
  }

  public <T> void multiSend(INetState<T>[] states) throws Exception {
    HashMap<Header, ArrayList<INetState<?>>> stateHeaderMap = new HashMap<>();

    for (INetState<?> state : states) {
      if (state == null) { continue; }
      Header stateHeader = state.getHeader();
      if (!stateHeaderMap.containsKey(stateHeader)) {
        stateHeaderMap.put(stateHeader, new ArrayList<INetState<?>>());
      }
      stateHeaderMap.get(stateHeader).add(state);
    }

    for (Header stateHeader : stateHeaderMap.keySet()) {
      ArrayList<INetState<?>> stateArr = stateHeaderMap.get(stateHeader);

      byte[][] msgs = new byte[stateArr.size()][];
      msgs = Arrays.stream(stateArr.toArray())
        .map(s -> {
          try {
            return ((INetState<?>) s).getSendData();
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return null;
        })
        .toArray(byte[][]::new);
      
      client.sendDenseSessionPacket(stateHeader.value(), msgs);
    }
  }

  public <T> void multiSend(String[] ids) throws Exception {
    INetState<?>[] states = Arrays.stream(ids)
      .map(stateIDMap::get)
      .filter(state -> state != null)
      .toArray(INetState<?>[]::new);

    multiSend(states);
  }

  public <T> void multiSend(ArrayList<INetState<T>> states) throws Exception {
    INetState<?>[] stateArr = states.toArray(new INetState<?>[0]);
    multiSend(stateArr);
  }

  public <T> void sendStatesWithHeader(Header header) throws Exception {
    INetState<?>[] states = getAllStatesWithHeader(header);
    if (states.length == 0) { return; }

    byte[][] msgs = new byte[states.length][];
    msgs = Arrays.stream(states).map(s -> {
      try {
        return s.getSendData();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return null;
    }).toArray(byte[][]::new);
    
    client.sendDenseSessionPacket(header.value(), msgs);
  }

  public <T> void tryReceiveStates(ClientPacketData data) throws Exception {
    Header header = Header.from(data.header);

    if (header.type() == Header.HeaderType.StateChange) { recvStateChange(header, data); }
    if (header.type() == Header.HeaderType.StateAdd) { recvStateAdd(header, data); }
    if (header.type() == Header.HeaderType.StateDelete) { recvStateDelete(header, data); }
  }

  private <T> void recvStateChange(Header header, ClientPacketData data) throws Exception {
    for (byte[] msg : data.msgs) {
      ByteArrayInputStream bais = new ByteArrayInputStream(msg);
      DataInputStream dis = new DataInputStream(bais);

      String id = dis.readUTF();
      dis.readUTF(); // Read the class name, but ignore it

      INetState<?> state = stateIDMap.get(id);

      if (state == null) { // No state exists with this ID
        NetState.fromSerializedData(header, msg, this); // Create a new state in the State Manager
        continue; 
      } 
      if (!state.getHeader().compare(header)) { continue; } // Header mismatch
      if (state.getSyncMode().equals(SyncMode.CLIENT)) { continue; } // SyncMode is for CLIENT, not SERVER

      state.recieveData(dis);
    }
  }

  private <T> void recvStateAdd(Header header, ClientPacketData data) {
    
  }

  private <T> void recvStateDelete(Header header, ClientPacketData data) {
    
  }
}
