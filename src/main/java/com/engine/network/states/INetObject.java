package com.engine.network.states;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public interface INetObject {
    public void deserialize(DataInputStream dataSegments) throws Exception;
    public void serialize(DataOutputStream dataSegments) throws Exception;
}
