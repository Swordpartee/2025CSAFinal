package com.engine.network.states;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.security.SecureRandom;
import java.util.Random;

import com.engine.network.headers.Header;

public class NetState<T extends INetObject> implements INetState<T> {
    private Header stateHeader;
    private String uuid;
    private ControlMode controlMode = ControlMode.CLIENT;
    private ClientStateManager stateManager;

    public T value;

    public static class NanoId {
        private static final Random random = new SecureRandom();
        private static final char[] alphabet = 
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        
        public static String generate(int size) {
            char[] id = new char[size];
            for (int i = 0; i < size; i++) {
                id[i] = alphabet[random.nextInt(alphabet.length)];
            }
            return new String(id);
        }
    }

    private static String generateTimeBasedId() {
        // Current time millis in custom base
        long now = System.currentTimeMillis();
        String timeComponent = Long.toString(now, 36); // Base36 encoding

        // Add 3 random chars for collision prevention
        String randomChars = NanoId.generate(3);

        return timeComponent + randomChars;
    }

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
        ControlMode syncMode = ControlMode.from(dataInputStream.read());

        Class<?> clazz = Class.forName(className);
        INetObject netObject;
        netObject = (INetObject) clazz.getDeclaredConstructor().newInstance();

        netObject.deserialize(dataInputStream);

        NetState<?> netState = new NetState<>(header, csm, netObject, uuid);
        netState.setControlMode(syncMode == ControlMode.CLIENT ? ControlMode.SERVER : ControlMode.BOTH);
        
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
        ControlMode syncMode = ControlMode.valueOf(dataInputStream.readUTF());

        if (!clazz.getName().equals(className)) {
            throw new ClassCastException("Expected class " + clazz.getName() + " but got " + className);
        }

        T netObject = clazz.getDeclaredConstructor().newInstance();
        netObject.deserialize(dataInputStream);

        NetState<T> netState = new NetState<>(header, csm, netObject, uuid);
        netState.setControlMode(syncMode == ControlMode.CLIENT ? ControlMode.SERVER : ControlMode.BOTH);

        return netState;
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
        this(stateHeader, stateManager, initialValue, generateTimeBasedId());
    }
    
    public Header getHeader() {
        return stateHeader;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "stateHeader=" + stateHeader +
            ", syncMode=" + controlMode +
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

    public ControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }

    @Override
    public byte[] getSendData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(uuid);
        dos.writeUTF(value.getClass().getName());
        dos.write(controlMode.value());

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
        if (controlMode == ControlMode.SERVER) { // If it is being controlled by the server (or just a different client), don't send it.
            return;
        }

        stateManager.sendState(this);
    }

    public void deleteSelf() throws Exception {
        if (controlMode == ControlMode.SERVER) { // If it is being controlled by the server (or just a different client), don't delete it.
            System.out.println("Tried to delete a server controlled state. This is not allowed.");
            return;
        }

        stateManager.sendStateDelete(this);
    }
}
