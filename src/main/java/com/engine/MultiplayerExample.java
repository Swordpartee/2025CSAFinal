package com.engine;

import java.util.Scanner;

import com.engine.game.objects.PlayerController;
import com.engine.game.objects.Projectile;
import com.engine.network.Network;
import com.engine.network.headers.BaseHeader;
import com.engine.network.headers.Header;
import com.engine.network.states.ClientStateManager;
import com.engine.network.states.INetState;
import com.engine.network.states.NetState;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerText;

public class MultiplayerExample {
    public static NetState<PlayerController> playerState;
    public static DrawerText text;
    //
    //  THIS IS THE ONLY FUNCTION THAT DOES NOT USE STATES, SO PLEASE ASK IF YOU HAVE QUESITIONS ABOUT IT
    //  dont worry about it for now, I can explain it on Monday, or whenever you want to start making the login screen
    //     
    public static void loginAndJoinRoom() throws Exception {
        Scanner scan = new Scanner(System.in);

        // Wait to go on until the client is connected
		while (!Network.client.loggedIn()) {
			System.out.println("Enter your username and password: (username:password)");
			// Send the username and password to the server (username:password)
			Network.client.sendSessionPacketAndWait(BaseHeader.AuthLogin.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthError.value()});
		}

		// Wait to go on until the client is in a room.
		while (!Network.client.roomSet()) {
			System.out.println("Do you want to create or join room? (c/j)");
			String choice = scan.nextLine();
			if (choice.equals("c")) {
				System.out.println("Enter the room you want to create: (room name:room password)");
				Network.client.sendSessionPacketAndWait(BaseHeader.CreateRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.CreateRoom.value(), BaseHeader.RoomError.value()});
				continue;
			} else if (choice.equals("j")) {
				System.out.println("Enter the room you want to join: (room name:room password)");
				Network.client.sendSessionPacketAndWait(BaseHeader.JoinRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.RoomError.value()});
				continue;
			} else {
				System.out.println("Invalid choice. Please enter 'c' to create a room or 'j' to join a room.");
				continue;
			}
		}
        scan.close();
    }

    public static void main(String[] args) throws Exception {
        Network.connect();

        text = new DrawerText("Hello World", 10, 10, 10, "Arial");


        // Whenever you have a TYPE of state that you want to be added to the game, you need to add and addFunction to the stateManager for that type.
        Network.stateManager.addAddFunction((INetState<?> a) -> {
            PlayerController player = (PlayerController) a.getValue();
            Renderer.addDrawables(player); // Not taking in input, just drawing from the other client
        }, PlayerController.class);

        // Same thing for the delete function, but this is for when the state is deleted.
        Network.stateManager.addDeleteFunction((INetState<?> a) -> {
            PlayerController player = (PlayerController) a.getValue();
            Renderer.removeDrawables(player);
        }, PlayerController.class);

        Network.stateManager.addAddFunction((INetState<?> a) -> {
            Projectile projectile = (Projectile) a.getValue();
            Renderer.addDrawables(projectile);
            System.out.println("Projectile added: " + projectile);
        }, Projectile.class);

        Network.stateManager.addDeleteFunction((INetState<?> a) -> {
            Projectile projectile = (Projectile) a.getValue();
            Renderer.removeDrawables(projectile);
        }, Projectile.class);

        // Login and join a room (admin:9*A#awjd893E*jf37ug$h)
        loginAndJoinRoom(); // Just using the placeholder login system I made for now.

        // Create a new PlayerController State (THis is what we transmit to the server, which gives it to the other clients)
        NetState<PlayerController> playerState = new NetState<>(Header.PlayerState, Network.stateManager, new PlayerController(true));
        // NetState<Projectile> projectileState = new NetState<>(Header.ProjectileState, Network.stateManager, new Projectile(Math.random() * 100, Math.random() * 100, 10, true));

        // Add the player to the Renderer (Nothing server related, just for the client)
        Renderer.addGameObjects(playerState.value);
        Renderer.addDrawables(text);
        

        // Make sure that the player state is sent to the server every frame (There are better ways to do this, but this is the easiest for now)
        // Ex: better way = wait for player to update (position) and then send that to the server
        Renderer.addProcesses(() -> {
            // Send the player state to the server
            try {
                playerState.sendSelf();
                // projectileState.sendSelf();
                INetState<?>[] projectiles = Network.stateManager.getStatesOfType(Projectile.class);
                text.setText(projectiles.length + " projectiles " + ClientStateManager.MessagesSent + " messages sent " + ClientStateManager.MessagesReceived + " messages received");
                // System.out.println("Projectile Amount: " + projectiles.length);
                // Network.stateManager.sendStatesWithHeader(Header.ProjectileState);
                for (INetState<?> a : projectiles) {
                    a.sendSelf();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Set the player state to be deleted when the game is closed (No effect on THIS client, but the server will tell the other clients to delete the player)
        Renderer.setOnGameClose(() -> {
            try {
                Network.disconnect();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        // Start the Renderer
        Renderer.start();
    }
}
