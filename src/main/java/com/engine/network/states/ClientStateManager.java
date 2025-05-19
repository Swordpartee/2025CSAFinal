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

    private final HashMap<Class<?>, StateF<?>> addFunctions = new HashMap<>(); // Allow for getting states by class
    private final HashMap<Class<?>, StateF<?>> deleteFunctions = new HashMap<>(); // Allow for getting states by class
    private final HashMap<String, INetState<?>> stateIDMap = new HashMap<>(); // Allow for getting states by ID
    private final Client client;

    /**
     * Adds a function to be called when a state is added of a certain class.
     * @param function : The function to be called.
     * @param clazz : The class of the state.
     */
    public <T extends INetObject> void addAddFunction(StateF<?> function, Class<?> clazz) {
        addFunctions.put(clazz, function);
    }

    /**
     * Adds a function to be called when a state is deleted of a certain class.
     * @param function : The function to be called.
     * @param clazz : The class of the state.
     */
    public <T extends INetObject> void addDeleteFunction(StateF<?> function, Class<?> clazz) {
        deleteFunctions.put(clazz, function);
    }

    /**
     * Gets all of the states with a certain header.
     * @param lookupHeader : The header to look up.
     * @return An array of states with the given header.
     */
    private INetState<?>[] getStatesOfHeader(Header lookupHeader) {
        INetState<?>[] states = Arrays.stream(stateIDMap.values().toArray(new INetState<?>[0]))
            .filter(state -> state.getHeader().compare(lookupHeader) && state.getControlMode() != ControlMode.SERVER).toArray(INetState<?>[]::new);
        return states;
    }

    /**
     * Gets all of the states with a certain class / type.
     * @param clazz : The class to look up.
     * @return An array of states with the given class.
     */
    public INetState<?>[] getStatesOfType(Class<?> clazz) {
        INetState<?>[] states = Arrays.stream(stateIDMap.values().toArray(new INetState<?>[0]))
            .filter(state -> ((INetState<?>) state).getValue().getClass().equals(clazz))
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
    public <T> boolean addState(INetState<T> state) {
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
    public <T> boolean removeState(INetState<T> state) {
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
    public <T> void sendState(INetState<T> state) throws Exception {
        ClientStateManager.MessagesSent++;
        client.sendSessionPacket(state.getHeader().value(), state.getSendData());
    }

    /**
     * Sends an array of states to the server with dense session packets.
     * @param states : The array of states to send.
     * @throws Exception
     */
    public <T> void multiSend(INetState<?>[] states) throws Exception {
        HashMap<Header, ArrayList<INetState<?>>> stateHeaderMap = new HashMap<>();

        for (INetState<?> state : states) {
            if (state == null) {
                continue;
            }
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

    /**
     * Sends an array of states to the server with dense session packets.
     * @param ids : The IDs of the states to send.
     * @throws Exception
     */
    public <T> void multiSend(String[] ids) throws Exception {
        INetState<?>[] states = Arrays.stream(ids)
                .map(stateIDMap::get)
                .filter(state -> state != null)
                .toArray(INetState<?>[]::new);

        multiSend(states);
    }

    /**
     * Sends an array of states to the server with dense session packets.
     * @param states : An ArrayList of states to send.
     * @throws Exception
     */
    public <T> void multiSend(ArrayList<INetState<?>> states) throws Exception {
        INetState<?>[] stateArr = states.toArray(new INetState<?>[0]);
        multiSend(stateArr);
    }

    /**
     * Sends all of the states with a certain header to the server.
     * @param header : The header of the states to send.
     * @throws Exception
     */
    public <T> void sendStatesWithHeader(Header header) throws Exception {
        INetState<?>[] states = getStatesOfHeader(header);
        if (states.length == 0) {
            return;
        }

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

        // System.out.println("Sending " + states.length + " states with header: " + header);

        client.sendDenseSessionPacket(header.value(), msgs);
    }

    /**
     * Sends a state to the server to be deleted.
     * @param state : The state to delete.
     * @throws Exception
     */
    public <T> void sendStateDelete(INetState<T> state) throws Exception {
        client.sendSessionPacket(Header.DeleteState.value(), state.getId());
        stateIDMap.remove(state.getId());
        deleteFunctions.get(state.getValue().getClass()).run(state);
    }

    /**
     * Sends a state to the server to be deleted by a value given
     * @param state : The state to delete.
     * @throws Exception
     */
    public <T> void deleteStateByValue(T value) throws Exception {
        INetState<?>[] states = getStatesOfType(value.getClass());
        for (INetState<?> s : states) {
            if (s.getValue().equals(value)) {
                sendStateDelete(s);
                return;
            }
        }
    }

    /**
     * Sends an array of states to the server to be deleted with dense session packets.
     * @param states : The array of states to delete.
     * @throws Exception
     */
    public <T> void multiSendStateDelete(INetState<?>[] states) throws Exception {
        HashMap<Header, ArrayList<INetState<?>>> stateHeaderMap = new HashMap<>();

        for (INetState<?> state : states) {
            if (state == null) {
                continue;
            }
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
                    .map(s -> ((INetState<?>) s).getId().getBytes())
                    .toArray(byte[][]::new);

            client.sendDenseSessionPacket(Header.DeleteState.value(), msgs);
        }
    }

    /**
     * Sends an array of states to the server to be deleted with dense session packets.
     * @param ids : The IDs of the states to deletes.
     * @throws Exception
     */
    public <T> void multiSendStateDelete(String[] ids) throws Exception {
        INetState<?>[] states = Arrays.stream(ids)
                .map(stateIDMap::get)
                .filter(state -> state != null)
                .toArray(INetState<?>[]::new);

        multiSendStateDelete(states);
    }

    /**
     * Sends an array of states to the server to be deleted with dense session packets.
     * @param states : An ArrayList of states to delete.
     * @throws Exception
     */
    public <T> void multiSendStateDelete(ArrayList<INetState<?>> states) throws Exception {
        INetState<?>[] stateArr = states.toArray(new INetState<?>[0]);
        multiSendStateDelete(stateArr);
    }

    /**
     * Sends all of the states with a certain header to the server to be deleted.
     * @param header : The header of the states to delete.
     * @throws Exception
     */
    public <T> void sendStateDeleteWithHeader(Header header) throws Exception {
        INetState<?>[] states = getStatesOfHeader(header);
        if (states.length == 0) {
            return;
        }

        byte[][] msgs = new byte[states.length][];
        msgs = Arrays.stream(states).map(s -> s.getId().getBytes()).toArray(byte[][]::new);

        client.sendDenseSessionPacket(Header.DeleteState.value(), msgs);
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
                addFunctions.get(newState.getValue().getClass()).run(newState); // Call the add function for this state
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
    private <T> void recvStateDelete(Header header, ClientPacketData data) {
        // System.out.println("Delete State: " + Convert.btos(data.msg));


        INetState<?> state = stateIDMap.get(Convert.btos(data.msg));

        stateIDMap.remove(Convert.btos(data.msg));

        if (state != null) {
            deleteFunctions.get(state.getValue().getClass()).run(state);
        } else {
            System.out.println("State not found: " + Convert.btos(data.msg));
        }
    }
}
