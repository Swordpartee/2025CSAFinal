package com.engine;

import java.util.Scanner;

import com.engine.game.UI.Button;
import com.engine.game.UI.Textbox;
import com.engine.game.objects.GameRect;
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
import com.engine.util.Functions;
import com.engine.util.PointConfig;

public class MultiplayerExample {
    public static NetState<PlayerController> playerState;
    public static DrawerText text;
    public static double lastTime = 0;

    public static volatile String loginOptionChosen = "";
    //
    //  THIS IS THE ONLY FUNCTION THAT DOES NOT USE STATES, SO PLEASE ASK IF YOU HAVE QUESITIONS ABOUT IT
    //  dont worry about it for now, I can explain it on Monday, or whenever you want to start making the login screen
    //     
    public static void loginAndJoinRoom() throws Exception {

        // Let the user choose between login and signup
        GameRect buttonRect = new GameRect(25, 12.5, 75, 25, true);
        Button button = new Button(buttonRect, () -> {
            loginOptionChosen = "login";
        });
        DrawerText buttonText = new DrawerText(new PointConfig(5, 17), "Login", 10, "Arial");

        GameRect button2Rect = new GameRect(25, 50, 75, 25, true);
        Button button2 = new Button(button2Rect, () -> {
            loginOptionChosen = "signup";
        });
        DrawerText button2Text = new DrawerText(new PointConfig(5, 55), "Sign Up", 10, "Arial");

        Renderer.addUIElements(button, buttonText, button2, button2Text);

        while (loginOptionChosen.length() == 0) { }

        Renderer.removeUIElements(button, buttonText, button2, button2Text);
        
        // Create the login screen        
        DrawerText drawText = new DrawerText(new PointConfig(0, 10), "Please enter in your username and password below:", 10, "Arial");
        DrawerText drawText2 = new DrawerText(new PointConfig(0, 30), "Username:", 10, "Arial");
        Textbox textBox = new Textbox(new PointConfig(110, 25), 100, 20, (text) -> { return false; });
        DrawerText drawText3 = new DrawerText(new PointConfig(0, 50), "Password:", 10, "Arial");
        Textbox textBox2 = new Textbox(new PointConfig(110, 45), 100, 20, (text) -> {
            try {
                Network.client.sendSessionPacketAndWait(loginOptionChosen == "login" ? BaseHeader.AuthLogin.value() : BaseHeader.AuthSignup.value(), (textBox.getText() + ":" + text).getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthError.value()});
            } catch (Exception e) {
                return false;
            }
            return true; 
        });
        Renderer.addUIElements(drawText, drawText2, textBox, drawText3, textBox2);

        // Wait to go on until the client is connected
		while (!Network.client.loggedIn()) { }

        Renderer.removeUIElements(drawText, drawText2, textBox, drawText3, textBox2);

        Scanner scan = new Scanner(System.in);

		// Wait to go on until the client is in a room.
		while (!Network.client.roomSet()) {
			System.out.println("Do you want to create or join room? (c/j)");
			String choice = scan.nextLine();
			if (choice.equals("c")) {
				System.out.println("Enter the room you want to create: (room name:room password)");
				Network.client.sendSessionPacketAndWait(BaseHeader.CreateRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.CreateRoom.value(), BaseHeader.RoomError.value()});
			} else if (choice.equals("j")) {
				System.out.println("Enter the room you want to join: (room name:room password)");
				Network.client.sendSessionPacketAndWait(BaseHeader.JoinRoom.value(), scan.nextLine().getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.RoomError.value()});
			} else {
				System.out.println("Invalid choice. Please enter 'c' to create a room or 'j' to join a room.");
			}
		}

        Renderer.removeUIElements(textBox);
        // Renderer.removeDrawables(text, textBox);
        // Renderer.removeProcesses(loginRunnable);
        scan.close();
    }

    public static void main(String[] args) throws Exception {
        Network.connect();

        text = new DrawerText(10, 10, "Hello World", 10, "Arial");

        // Create the game window
        Renderer.start();

        // Login and join a room (admin:9*A#awjd893E*jf37ug$h)
        loginAndJoinRoom(); // Just using the placeholder login system I made for now.

        // Create a new PlayerController State (THis is what we transmit to the server, which gives it to the other clients)
        NetState<PlayerController> playerState = new NetState<>(Header.PlayerState, Network.stateManager, new PlayerController());
        // NetState<Projectile> projectileState = new NetState<>(Header.ProjectileState, Network.stateManager, new Projectile(Math.random() * 100, Math.random() * 100, 10, true));

        // Add the player to the Renderer (Nothing server related, just for the client)
        Renderer.addGameObjects(playerState.value);
        Renderer.addDrawables(text);

        lastTime = Functions.getTime();

        Network.addStateSender(Header.PlayerState, 0);
        Network.addStateSender(Header.ProjectileState, 1000);

        // THIS IS JUST FOR DEBUGGING, IT DOES NOT AFFECT THE GAME
        Renderer.addProcesses(() -> {
            INetState<?>[] projectiles = Network.stateManager.getStatesOfType(Projectile.class);
            text.setText(projectiles.length + " projectiles " + ClientStateManager.MessagesSent + " messages sent " + ClientStateManager.MessagesReceived + " messages received");
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
    }
}
