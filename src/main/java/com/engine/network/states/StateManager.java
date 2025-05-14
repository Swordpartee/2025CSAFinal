package com.engine.network.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.encryption.Encryption;
import com.engine.network.headers.Header;
import com.engine.network.server.Server;

public class StateManager {
  private final HashMap<Header, ArrayList<IClientState<?>>> stateMap = new HashMap<Header, ArrayList<IClientState<?>>>();
  private final Client client;

  public StateManager(Client client) {
    this.client = client;
  }

  public <T> void addState(IClientState<T> state) {
    if (!stateMap.containsKey(state.getHeader())) {
      stateMap.put(state.getHeader(), new ArrayList<IClientState<?>>());
    }
    stateMap.get(state.getHeader()).add(state);
  } 

  public <T> void removeState(IClientState<T> state) {
    // Remove the state from the list if it exists
    if (!stateMap.containsKey(state.getHeader())) { return; }
    stateMap.get(state.getHeader()).remove(state);

    // If the list is empty, remove the header from the map
    if (!stateMap.get(state.getHeader()).isEmpty()) { return; }
    stateMap.remove(state.getHeader());
  }

  public <T> void sendState(IClientState<T> state) throws Exception {
    client.sendSessionPacket(state.getHeader().value(), state.getSendData());
  }

  public <T> void sendAll(Header stateHeader) throws Exception {
    if (!stateMap.containsKey(stateHeader)) { return; }
    ArrayList<IClientState<?>> states = stateMap.get(stateHeader);

    byte[][] msgs = new byte[states.size()][];
    msgs = Arrays.stream(states.toArray())
                 .map(s -> ((IClientState<?>) s).getSendData())
                 .toArray(byte[][]::new);

    client.sendDenseSessionPacket(stateHeader.value(), msgs);
  }

  public <T> void tryReceiveState(ClientPacketData data) {
    if (!stateMap.containsKey(Header.from(data.header))) { return; }
  }
}
