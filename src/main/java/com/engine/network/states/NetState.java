package com.engine.network.states;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import com.engine.network.headers.Header;

public class NetState<T extends INetObject> implements INetState<T> {
    private Header stateHeader;
    private String uuid;
    private SyncMode syncMode = SyncMode.SERVER;
    private ClientStateManager stateManager;

    public T value;

    /**
     * Creates a new NetState object from serialized data from the server.
     * @param header : The header of the state
     * @param data : The serialized data from the server
     * @param csm : The ClientStateManager to use for this state
     * @return The deserialized state
     * @throws Exception
     */
    public static NetState<?> fromSerializedData(Header header, byte[] data, ClientStateManager csm) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));

        String uuid = dataInputStream.readUTF();
        String className = dataInputStream.readUTF();

        Class<?> clazz = Class.forName(className);
        INetObject netObject = (INetObject) clazz.getDeclaredConstructor().newInstance();
        netObject.deserialize(dataInputStream);

        NetState<?> netState = new NetState<>(header, csm, netObject, uuid);
        
        return netState;
    }

    /**
     * Creates a new NetState object from serialized data from the server.
     * @param header : The header of the state
     * @param data : The serialized data from the server
     * @param csm : The ClientStateManager to use for this state
     * @param clazz : The class of the state
     * @return The deserialized state
     * @throws Exception
     */
    public static <T extends INetObject> NetState<T> fromSerializedData(Header header, byte[] data, ClientStateManager csm, Class<T> clazz) throws Exception {
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data));

        String uuid = dataInputStream.readUTF();
        String className = dataInputStream.readUTF();

        if (!clazz.getName().equals(className)) {
            throw new ClassCastException("Expected class " + clazz.getName() + " but got " + className);
        }

        T netObject = clazz.getDeclaredConstructor().newInstance();
        netObject.deserialize(dataInputStream);

        return new NetState<>(header, csm, netObject, uuid);
    }

    /**
     * Creates a new NetState object.
     * @param stateHeader : The header of the state
     * @param stateManager : The ClientStateManager to use for this state
     * @param initialValue : The initial value of the state
     * @param id : The ID of the state
     */
    public NetState(Header stateHeader, ClientStateManager stateManager, T initialValue, String id) {
        this.stateHeader = stateHeader;
        this.uuid = id;
        this.stateManager = stateManager;

        // System.out.println("State CREATED: " + this.uuid);
        
        this.value = initialValue;

        // Just for convenience.
        if (stateManager != null) {
            stateManager.addState(this);
        }
    } 

    /**
     * Creates a new NetState object.
     * @param stateHeader : The header of the state
     * @param stateManager : The ClientStateManager to use for this state
     * @param initialValue : The initial value of the state
     */
    public NetState(Header stateHeader, ClientStateManager stateManager, T initialValue) {
        this(stateHeader, stateManager, initialValue, UUID.randomUUID().toString());
    }
    
    public Header getHeader() {
        return stateHeader;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "stateHeader=" + stateHeader +
            ", syncMode=" + syncMode +
            ", value=" + value +
            '}';
    }

    public String getId() {
        return uuid;
    }

    public void setId(String id) {
        this.uuid = id;
    }

    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }

    public SyncMode getSyncMode() {
        return syncMode;
    }

    public void setSyncMode(SyncMode syncMode) {
        this.syncMode = syncMode;
    }

    @Override
    public byte[] getSendData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(uuid);
        dos.writeUTF(value.getClass().getName());

        value.serialize(dos);

        dos.flush();

        byte[] sendData = baos.toByteArray();

        return sendData;
    }

    @Override
    public void recieveData(DataInputStream data) throws Exception {
        value.deserialize(data);
        // System.out.println("New value: " + this.value);
    }

    public void sendSelf() throws Exception {
        stateManager.sendState(this);
    }

    public void deleteSelf() throws Exception {
        stateManager.sendStateDelete(this);
    }
}
