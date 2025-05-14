package com.engine.network.states;

import com.engine.network.headers.Header;

public abstract class ClientState<T> implements IClientState<T> {
    private Header stateHeader;
    protected SyncMode syncMode = SyncMode.CLIENT;
    protected T value;
    private int stateId;

    public ClientState(Header stateHeader) {
        this.stateHeader = stateHeader;
    } 

    public Header getHeader() {
        return stateHeader;
    }

    public String toString() {
        return "ClientState{" +
            "stateHeader=" + stateHeader +
            ", syncMode=" + syncMode +
            ", value=" + value +
            '}';
    }

    public int getId() {
        return stateId;
    }
}
