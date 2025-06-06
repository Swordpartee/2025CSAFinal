package com.engine.network.states;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.encryption.Convert;
import com.engine.network.headers.Header;

public class ClientStateManager {
    public interface StateF<T extends INetObject> {
        public void run(INetState<?> state);
    }


    public static final String SEGMENT_DELIMITER = "::";
    public static int MessagesSent = 0;
    public static int MessagesReceived = 0;

    private final HashMap<String, INetState<?>> stateIDMap = new HashMap<>(); // Allow for getting states by ID
    private final Client client;

    /**
     * Gets all of the states with a certain header.
     * @param lookupHeader : The header to look up.
     * @return An array of states with the given header.
     */
    public INetState<?>[] getStatesOfHeader(Header lookupHeader) {
        ArrayList<INetState<?>> statesCopy;
        synchronized (stateIDMap) {
            statesCopy = new ArrayList<>(stateIDMap.values());
        }
        return statesCopy.stream()
            .filter(state -> state != null && state.getHeader().compare(lookupHeader) && state.getControlMode() != ControlMode.SERVER)
            .toArray(INetState<?>[]::new);
    }

    /**
     * Gets all of the states with a certain class / type.
     * @param clazz : The class to look up.
     * @return An array of states with the given class.
     */
    public INetState<?>[] getStatesOfType(Class<?> clazz) {
        INetState<?>[] states = Arrays.stream(stateIDMap.values().toArray(new INetState<?>[0]))
            .filter(state -> state != null && ((INetState<?>) state).getValue().getClass().equals(clazz))
            .toArray(INetState<?>[]::new);
        return states;
    }

    /**
     * Constructor for the ClientStateManager.
     * @param client : The client to manage states for.
     */
    public ClientStateManager(Client client) {
        this.client = client;
    }

    /**
     * Adds a state to the state manager. (Does not send it to the server)
     * @param state : The state to add.
     * @return True if the state was added, false if it already exists.
     */
    public <T extends INetObject> boolean addState(INetState<T> state) {
        if (stateIDMap.containsKey(state.getId())) {
            return false;
        }
        stateIDMap.put(state.getId(), state);

        return true;
    }

    /**
     * Removes a state from the state manager. (Does not send it to the server)
     * @param state : The state to remove.
     * @return True if the state was removed, false if it does not exist.
     */
    public <T extends INetObject> boolean removeState(INetState<T> state) {
        if (!stateIDMap.containsKey(state.getId())) {
            return false;
        }
        stateIDMap.remove(state.getId());
        return true;
    }

    /**
     * Sends a state to the server.
     * @param state : The state to send.
     * @throws Exception
     */
    public <T extends INetObject> void sendState(INetState<T> state) throws Exception {
        ClientStateManager.MessagesSent++;
        client.sendSessionPacket(state.getHeader().value(), state.getSendData());
    }

    /**
     * Sends a state to the server to be deleted.
     * @param state : The state to delete.
     * @throws Exception
     */
    public <T extends INetObject> void sendStateDelete(INetState<T> state) throws Exception {
        ((INetObject) state.getValue()).onNetworkDestroy();
        client.sendSessionPacket(Header.DeleteState.value(), state.getId());
        stateIDMap.remove(state.getId());
    }

    /**
     * Sends a state to the server to be deleted by a value given
     * @param state : The state to delete.
     * @throws Exception
     */
    public <T> void deleteStateByValue(T value) throws Exception {
        INetState<?>[] states = getStatesOfType(value.getClass());
        for (INetState<?> s : states) {
            if (s.getControlMode() != ControlMode.SERVER && s.getValue().equals(value)) {
                sendStateDelete(s);
                return;
            }
        }
    }
    
    /**
     * Sends a state to the server to be deleted by a value given
     * @param state : The state to delete.
     * @throws Exception
     */
    public <T> void sendStateByValue(T value) throws Exception {
        INetState<?>[] states = getStatesOfType(value.getClass());
        for (INetState<?> s : states) {
            if (s.getControlMode() != ControlMode.SERVER && s.getValue().equals(value)) {
                sendState(s);
                return;
            }
        }
    }

    /**
     * Tries to receive states from the server.
     * @param data : The data received from the server.
     * @throws Exception
     */
    public <T> void tryReceiveStates(ClientPacketData data) throws Exception {
        Header header = Header.from(data.header);

        ClientStateManager.MessagesReceived++;

        if (header == null) {
            return;
        }

        if (header.type() == Header.HeaderType.StateChange) {
            recvStateChange(header, data);
        }
        if (header.type() == Header.HeaderType.StateDelete) {
            recvStateDelete(header, data);
        }
    }

    /**
     * Receives a state change from the server. (Will either update an existing state or create a new one)
     * @param header : The header of the state change.
     * @param data : The data received from the server.
     * @throws Exception
     */
    private <T> void recvStateChange(Header header, ClientPacketData data) throws Exception {
        for (byte[] msg : data.msgs) {
            ByteArrayInputStream bais = new ByteArrayInputStream(msg);
            DataInputStream dis = new DataInputStream(bais);

            String id = dis.readUTF();
            dis.readUTF(); // Read the class name, but ignore it
            dis.read(); // Read the SyncMode, but ignore it

            INetState<?> state = stateIDMap.get(id);

            if (state == null) { // No state exists with this ID
                // System.out.println("State not found: " + id);
                INetState<?> newState = NetState.fromSerializedData(header, msg, this); // Create a new state in the State Manager
                ((INetObject) newState.getValue()).onNetworkCreate(); // Call the onNetworkCreate function for this state
                continue;
            }
            if (!state.getHeader().compare(header)) {
                continue;
            } // Header mismatch
            if (state.getControlMode().equals(ControlMode.CLIENT)) {
                continue;
            } // SyncMode is for CLIENT, not SERVER

            state.recieveData(dis);
        }
    }

    /**
     * Receives a state delete from the server.
     * @param header : The header of the state delete.
     * @param data : The data received from the server.
     */
    private <T> void recvStateDelete(Header header, ClientPacketData data) throws Exception {
        // System.out.println("Delete State: " + Convert.btos(data.msg));

        if (header.compare(Header.PlayerState)) {
            System.out.println("Player state deleted: " + Convert.btos(data.msg));
        }

        INetState<?> state = stateIDMap.get(Convert.btos(data.msg));

        stateIDMap.remove(Convert.btos(data.msg));

        if (state != null) {
            ((INetObject) state.getValue()).onNetworkDestroy(); // Call the onNetworkDestroy function for this state
        } //else {
            // System.out.println("State not found: " + Convert.btos(data.msg));
        // }
    }

    public void close() {
        stateIDMap.clear();
    }
}
