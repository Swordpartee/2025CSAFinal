package com.engine.network.states;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface INetObject {
    /**
     * Deserializes the object from the given DataInputStream.
     * @param dataSegments : The DataInputStream to read the object from 
     * @throws Exception
     */
    public void deserialize(DataInputStream dataSegments) throws Exception;
    /**
     * Serializes the object to the given DataOutputStream.
     * @param dataSegments : The DataOutputStream to write the object to
     * @throws Exception
     */
    public void serialize(DataOutputStream dataSegments) throws Exception;
}
