package com.engine.network;

import java.util.HashMap;

import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.headers.Header;
import com.engine.network.states.ClientStateManager;
import com.engine.network.states.INetState;
import com.engine.rendering.Renderer;
import com.engine.util.Functions;

public class Network {
    public static final int PORT = 8888;
    public static final String SERVER_ADDRESS = "10.168.106.254";
    public static ClientStateManager stateManager;
    public static Client client;
    private static final HashMap<TimeKeeper, Header> stateSenders = new HashMap<>();

    private static volatile boolean connected = false;
    public static boolean isConnected() {
        return connected;
    }

    public static void processRecv(ClientPacketData data) throws Exception {
        stateManager.tryReceiveStates(data);
    }
    
    public static void connect(String addr, int port) throws Exception {
        client = new Client(Network::processRecv);
        client.connect(addr, port);

        stateManager = new ClientStateManager(client);

        connected = true;

        Renderer.addProcesses(() -> {
            for (HashMap.Entry<TimeKeeper, Header> entry : stateSenders.entrySet()) {
                if (!entry.getKey().hasPassed()) { continue; }
                
                INetState<?>[] states = stateManager.getStatesOfHeader(entry.getValue());
                
                for (INetState<?> state : states) {
                    if (state == null) { continue; }

                    try {
                        if (state.isSelf()) {
                            state.sendSelf();
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void disconnect() throws Exception {
        INetState<?>[] playerStates = stateManager.getStatesOfHeader(Header.PlayerState);
        for (INetState<?> state : playerStates) {
            state.deleteSelf();
        }
        INetState<?>[] projectiles = stateManager.getStatesOfHeader(Header.ProjectileState);
        for (INetState<?> state : projectiles) {
            state.deleteSelf();
        }

        stateSenders.clear();
        client.close();
    }

    public static void addStateSender(Header headerToSend, int delayms) {
        TimeKeeper timeKeeper = new TimeKeeper(delayms);
        stateSenders.put(timeKeeper, headerToSend);
    }

    public static class TimeKeeper {
        private double startTime;
        private int delayms;
        public TimeKeeper(int delay) {
            this.delayms = delay;
            this.startTime = Functions.getTime();
        }
        public boolean hasPassed() {
            double deltaTime = Functions.getTime() - startTime;
            if (deltaTime > delayms) {
                this.reset();
                return true;
            }
            return false;
        }
        public void reset() {
            this.startTime = Functions.getTime();
        }
    }
}
