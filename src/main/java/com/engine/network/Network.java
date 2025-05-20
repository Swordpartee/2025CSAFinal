package com.engine.network;

import com.engine.game.objects.PlayerController;
import com.engine.game.objects.Projectile;
import com.engine.network.client.Client;
import com.engine.network.client.ClientPacketData;
import com.engine.network.states.ClientStateManager;
import com.engine.network.states.INetState;

public class Network {
    public static void processRecv(ClientPacketData data) throws Exception {
        stateManager.tryReceiveStates(data);
    }
    public static void connect() throws Exception {
        client = new Client(Network::processRecv);
        client.connect(SERVER_ADDRESS, PORT);

        stateManager = new ClientStateManager(client);
    }
    public static void disconnect() throws Exception {
        INetState<?>[] playerStates = stateManager.getStatesOfType(PlayerController.class);
        for (INetState<?> state : playerStates) {
            state.deleteSelf();
        }
        INetState<?>[] projectiles = stateManager.getStatesOfType(Projectile.class);
        for (INetState<?> state : projectiles) {
            state.deleteSelf();
        }

        client.close();
    }

    public static final int PORT = 4445;
    public static final String SERVER_ADDRESS = "localhost";
    public static ClientStateManager stateManager;
    public static Client client;
}
