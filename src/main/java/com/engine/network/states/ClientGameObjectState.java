package com.engine.network.states;

import com.engine.network.headers.Header;
import com.engine.util.Point;

public class ClientGameObjectState extends ClientState<Point> {
    public ClientGameObjectState() {
        super(Header.IncreaseCookies);
    }

    @Override
    public Point getValue() {
        return value;
    }

    @Override
    public void setValue(Point value) {
        this.value = value;
    }

    @Override
    public SyncMode getSyncMode() {
        return SyncMode.CLIENT;
    }

    @Override
    public void setSyncMode(SyncMode syncMode) {
        // Not applicable for this class
    }

    @Override
    public void recieveData(Point data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recieveData'");
    }

    @Override
    public byte[] getSendData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSendData'");
    }
}
