package com.engine;

import java.awt.Color;
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
import com.engine.util.PointConfig;

public class MultiplayerExample {
    public static NetState<PlayerController> playerState;
    public static DrawerText text;
    public static double lastTime = 0;

    public static volatile String loginOptionChosen = "";
    public static volatile String roomOptionChosen = "";
    //
    //  THIS IS THE ONLY FUNCTION THAT DOES NOT USE STATES, SO PLEASE ASK IF YOU HAVE QUESITIONS ABOUT IT
    //  dont worry about it for now, I can explain it on Monday, or whenever you want to start making the login screen
    //     
    public static void loginAndJoinRoom() throws Exception {

        // Let the user choose between login and signup
        GameRect loginButtonRect = new GameRect(320, 200, 150, 50, true, Color.BLACK);
        Button loginButton = new Button(loginButtonRect, () -> {
            loginOptionChosen = "login";
        });
        DrawerText loginButtonText = new DrawerText(new PointConfig(320, 200), "Login", 30, "Arial", Color.WHITE);

        GameRect signupButtonRect = new GameRect(320, 280, 150, 50, true, Color.BLACK);
        Button signupButton = new Button(signupButtonRect, () -> {
            loginOptionChosen = "signup";
        });
        DrawerText signupButtonText = new DrawerText(new PointConfig(320, 280), "Sign Up", 30, "Arial", Color.WHITE);

        Renderer.addUIElements(loginButton, loginButtonText, signupButton, signupButtonText);

        while (loginOptionChosen.length() == 0) { }

        Renderer.removeUIElements(loginButton, loginButtonText, signupButton, signupButtonText);
        
        // Create the login screen        
        DrawerText loginQuestionText = new DrawerText(new PointConfig(320, 100), "Please enter in your username\nand password below:", 40, "Arial", Color.BLACK);
        DrawerText usernameText = new DrawerText(new PointConfig(240, 200), "Username:", 30, "Arial", Color.BLACK);
        Textbox usernameTextbox = new Textbox(new PointConfig(400, 200), 150, 50, Color.BLACK, Color.WHITE, 30, "^[a-z0-9_]+$", (text) -> { return false; });
        DrawerText passwordText = new DrawerText(new PointConfig(240, 280), "Password:", 30, "Arial", Color.BLACK);
        Textbox passwordTextbox = new Textbox(new PointConfig(400, 280), 150, 50, Color.BLACK, Color.WHITE, 30, "^[a-z0-9_]+$", (text) -> {
            try {
                Network.client.sendSessionPacketAndWait(loginOptionChosen == "login" ? BaseHeader.AuthLogin.value() : BaseHeader.AuthSignup.value(), (usernameTextbox.getText() + ":" + text).getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthSignup.value(), BaseHeader.AuthError.value()});
            } catch (Exception e) {
                return false;
            }
            return true; 
        });
        passwordTextbox.setSecret(true);

        Renderer.addUIElements(loginQuestionText, usernameText, usernameTextbox, passwordText, passwordTextbox);

        // Wait to go on until the client is connected
		while (!Network.client.loggedIn()) { }

        Renderer.removeUIElements(loginQuestionText, usernameText, usernameTextbox, passwordText, passwordTextbox);

        // Create the room screen
        GameRect createButtonRect = new GameRect(320, 200, 220, 50, true, Color.BLACK);
        Button createButton = new Button(createButtonRect, () -> {
            roomOptionChosen = "create";
        });
        DrawerText createButtonText = new DrawerText(new PointConfig(320, 200), "Create Room", 30, "Arial", Color.WHITE);

        GameRect joinButtonRect = new GameRect(320, 280, 220, 50, true, Color.BLACK);
        Button joinButton = new Button(joinButtonRect, () -> {
            roomOptionChosen = "join";
        });
        DrawerText joinButtonText = new DrawerText(new PointConfig(320, 280), "Join Room", 30, "Arial", Color.WHITE);

        Renderer.addUIElements(createButton, createButtonText, joinButton, joinButtonText);

        while (roomOptionChosen.length() == 0) { }

        Renderer.removeUIElements(createButton, createButtonText, joinButton, joinButtonText);
        
        // Create the room screen        
        DrawerText roomQuestionText = new DrawerText(new PointConfig(320, 100), "Please enter in the room's\nname and password:", 40, "Arial", Color.BLACK);
        DrawerText roomNameText = new DrawerText(new PointConfig(240, 200), "Name:", 30, "Arial", Color.BLACK);
        Textbox roomNameTextbox = new Textbox(new PointConfig(400, 200), 150, 50, Color.BLACK, Color.WHITE, 30, (text) -> { return false; });
        DrawerText roomPasswordText = new DrawerText(new PointConfig(240, 280), "Password:", 30, "Arial", Color.BLACK);
        Textbox roomPasswordTextbox = new Textbox(new PointConfig(400, 280), 150, 50, Color.BLACK, Color.WHITE, 30, (text) -> {
            try {
                Network.client.sendSessionPacketAndWait(roomOptionChosen == "join" ? BaseHeader.JoinRoom.value() : BaseHeader.CreateRoom.value(), (roomNameTextbox.getText() + (text.length() > 0 ? ":" : "") + text).getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.CreateRoom.value() ,BaseHeader.RoomError.value()});
            } catch (Exception e) {
                return false;
            }
            return true; 
        });
        roomPasswordTextbox.setSecret(true);

        Renderer.addUIElements(roomQuestionText, roomNameText, roomNameTextbox, roomPasswordText, roomPasswordTextbox);

        while (!Network.client.roomSet()) { }

        Renderer.removeUIElements(roomQuestionText, roomNameText, roomNameTextbox, roomPasswordText, roomPasswordTextbox);
    }

    public static void main(String[] args) throws Exception {
        Network.connect();

        text = new DrawerText(320, 10, "Hello World", 10, "Arial", Color.RED);

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
