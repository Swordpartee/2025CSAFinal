package com.engine;

import java.awt.Color;
import java.util.ArrayList;

import com.engine.Constants.PlayerConstants;
import com.engine.game.UI.Button;
import com.engine.game.UI.Textbox;
import com.engine.game.objects.GameObject;
import com.engine.game.objects.GameRect;
import com.engine.game.objects.Player;
import com.engine.game.objects.Projectile;
import com.engine.network.Network;
import com.engine.network.headers.BaseHeader;
import com.engine.network.headers.Header;
import com.engine.network.states.ControlMode;
import com.engine.network.states.NetState;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.DrawerText;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Image;
import com.engine.util.PointConfig;

public class Game {
    public static volatile String loginOptionChosen = "";
    public static volatile String roomOptionChosen = "";

    public static void loginAndJoinRoom() {
        Background background = new Background(new Image("src/main/resources/tilebg.spr", PlayerConstants.PLAYER_SPRITE_SCALE));
        Renderer.addDrawables(background);

        // Let the user choose the server information
        DrawerText serverIPPortQuestionText = new DrawerText(new PointConfig(320, 100), "Please enter in your username\nand password below:", 40, "Arial", Color.DARK_GRAY);
        DrawerText ipText = new DrawerText(new PointConfig(240, 200), "IP:", 30, "Arial", Color.DARK_GRAY);
        Textbox ipTextbox = new Textbox(new PointConfig(400, 200), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, (text) -> { return false; });
        DrawerText portText = new DrawerText(new PointConfig(240, 280), "Port:", 30, "Arial", Color.DARK_GRAY);
        Textbox portTextbox = new Textbox(new PointConfig(400, 280), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, (text) -> {
            try {
                Network.connect(ipTextbox.getText(), Integer.parseInt(text));
            } catch (Exception e) {
                return false;
            }
            return true; 
        });

        Renderer.addUIElements(serverIPPortQuestionText, ipText, ipTextbox, portText, portTextbox);
        
        while (!Network.isConnected()) {}

        Renderer.removeUIElements(serverIPPortQuestionText, ipText, ipTextbox, portText, portTextbox);


        // Let the user choose between login and signup
        GameRect loginButtonRect = new GameRect(322, 196, 140, 56, true, Color.DARK_GRAY);
        Button loginButton = new Button(loginButtonRect, () -> {
            loginOptionChosen = "login";
        });
        DrawerText loginButtonText = new DrawerText(new PointConfig(322, 196), "Login", 32, "Arial", Color.WHITE);

        GameRect signupButtonRect = new GameRect(322, 280, 140, 56, true, Color.DARK_GRAY);
        Button signupButton = new Button(signupButtonRect, () -> {
            loginOptionChosen = "signup";
        });
        DrawerText signupButtonText = new DrawerText(new PointConfig(322, 280), "Sign Up", 32, "Arial", Color.WHITE);

        Renderer.addUIElements(loginButton, loginButtonText, signupButton, signupButtonText);

        while (loginOptionChosen.length() == 0) { }

        Renderer.removeUIElements(loginButton, loginButtonText, signupButton, signupButtonText);
        
        // Create the login screen        
        DrawerText loginQuestionText = new DrawerText(new PointConfig(320, 100), "Please enter in your username\nand password below:", 40, "Arial", Color.DARK_GRAY);
        DrawerText usernameText = new DrawerText(new PointConfig(240, 200), "Username:", 30, "Arial", Color.DARK_GRAY);
        Textbox usernameTextbox = new Textbox(new PointConfig(400, 200), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, "^[a-z0-9_]+$", (text) -> { return false; });
        DrawerText passwordText = new DrawerText(new PointConfig(240, 280), "Password:", 30, "Arial", Color.DARK_GRAY);
        Textbox passwordTextbox = new Textbox(new PointConfig(400, 280), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, "^[a-z0-9_]+$", (text) -> {
            try {
                Network.client.sendSessionPacketAndWait("login".equals(loginOptionChosen) ? BaseHeader.AuthLogin.value() : BaseHeader.AuthSignup.value(), (usernameTextbox.getText() + ":" + text).getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthSignup.value(), BaseHeader.AuthError.value()});
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
        GameRect createButtonRect = new GameRect(322, 196, 252, 56, true, Color.DARK_GRAY);
        Button createButton = new Button(createButtonRect, () -> {
            roomOptionChosen = "create";
        });
        DrawerText createButtonText = new DrawerText(new PointConfig(322, 196), "Create Room", 30, "Arial", Color.WHITE);

        GameRect joinButtonRect = new GameRect(322, 280, 252, 56, true, Color.DARK_GRAY);
        Button joinButton = new Button(joinButtonRect, () -> {
            roomOptionChosen = "join";
        });
        DrawerText joinButtonText = new DrawerText(new PointConfig(322, 280), "Join Room", 30, "Arial", Color.WHITE);

        Renderer.addUIElements(createButton, createButtonText, joinButton, joinButtonText);

        while (roomOptionChosen.length() == 0) { }

        Renderer.removeUIElements(createButton, createButtonText, joinButton, joinButtonText);
        
        // Create the room screen        
        DrawerText roomQuestionText = new DrawerText(new PointConfig(320, 100), "Please enter in the room's\nname and password:", 40, "Arial", Color.DARK_GRAY);
        DrawerText roomNameText = new DrawerText(new PointConfig(240, 200), "Name:", 30, "Arial", Color.DARK_GRAY);
        Textbox roomNameTextbox = new Textbox(new PointConfig(400, 200), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, (text) -> { return false; });
        DrawerText roomPasswordText = new DrawerText(new PointConfig(240, 280), "Password:", 30, "Arial", Color.DARK_GRAY);
        Textbox roomPasswordTextbox = new Textbox(new PointConfig(400, 280), 150, 50, Color.DARK_GRAY, Color.DARK_GRAY, 30, (text) -> {
        try {
                Network.client.sendSessionPacketAndWait("join".equals(roomOptionChosen) ? BaseHeader.JoinRoom.value() : BaseHeader.CreateRoom.value(), (roomNameTextbox.getText() + (text.length() > 0 ? ":" : "") + text).getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.CreateRoom.value() ,BaseHeader.RoomError.value()});
            } catch (Exception e) {
                return false;
            }
            return true; 
        });
        roomPasswordTextbox.setSecret(true);

        Renderer.addUIElements(roomQuestionText, roomNameText, roomNameTextbox, roomPasswordText, roomPasswordTextbox);

        while (!Network.client.roomSet()) { }

        Renderer.removeUIElements(roomQuestionText, roomNameText, roomNameTextbox, roomPasswordText, roomPasswordTextbox);
        Renderer.removeDrawables(background);
    }

    public static void main(String[] args) throws Exception {

        Background background = new Background(new Image("src/main/resources/solidbricks.spr", 5), new Image("src/main/resources/kindacrackedbricks.spr", 5));
        Renderer.addDrawables(background);

        /**
         * Initializing the game and its necessary components:
         *   - Sets up the network connection to the server.
         *   - Sets up the rendering engine and prepare it for drawing.
         *   - Shows a login menu to the user.
         */
        // Network.connect("10.168.106.254", 8888);
        Renderer.start();
        loginAndJoinRoom();

        /** 
         * Creating the states here:
         *   - The player state is a networked state that will be synchronized across clients.
         */
        NetState<Player> player = new NetState<>(Header.PlayerState, Network.stateManager, new Player());

        /**
         * Creating the drawables and non-networked game objects:
         *   - The background is a drawable grid that will render behind everything.
         */

        /**
         * Adding the drawables and game objects to the renderer:
         *   - Not much to say here
         */
        Renderer.addGameObjects(player.getValue());
        Renderer.addDamageable(player.getValue());

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.SPACE, () -> {
            player.getValue().swing();
         });
        
         RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.I, () -> {
            player.getValue().heal(10);
         });

        // TEMP SHOOT PROJECTILES - put it somewhere else later, idk, just make it more organized
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.R, () -> {
            ArrayList<Projectile> projectiles = new ArrayList<>();

            int numProjectiles = 1; // Number of projectiles to fire
            for (int i = 0; i < numProjectiles; i++) {
                double rotation = 0;
                if (player.getValue().getSprite().getCurrentState().contains("Right")) {
                    rotation = 0;
                } else if (player.getValue().getSprite().getCurrentState().contains("Left")) {
                    rotation = 180;
                } else if (player.getValue().getSprite().getCurrentState().contains("Up")) {
                    rotation = 270;
                } else if (player.getValue().getSprite().getCurrentState().contains("Down")) {
                    rotation = 90;
                }
                NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager,
                    new Projectile(player.getValue().getPoint().copy(), player.getValue(), rotation));
                projectile.setControlMode(ControlMode.BOTH); // Allow both server and client to control the projectile (Aka. Move + Delete it)
                // Set velocity based on player's direction
                String direction = player.getValue().getSprite().getCurrentState();
                double speed = 5.0; // Adjust speed as needed

                if (direction.contains("Right")) {
                    projectile.getValue().getVelocity().setPos(speed, 0);
                } else if (direction.contains("Left")) {
                    projectile.getValue().getVelocity().setPos(-speed, 0);
                } else if (direction.contains("Up")) {
                    projectile.getValue().getVelocity().setPos(0, -speed);
                } else if (direction.contains("Down")) {
                    projectile.getValue().getVelocity().setPos(0, speed);
                } else {
                    // Default direction (right) if no specific direction
                    projectile.getValue().getVelocity().setPos(speed, 0);
                }
                // Evenly space projectiles around the player's Y center
                double spacing = 2; // pixels between projectiles
                double centerY = player.getValue().getPoint().getY();
                double offset = (i - (numProjectiles - 1) / 2.0) * spacing;
                projectile.getValue().getPosition().setY(centerY + offset);
                try {
                    projectile.sendSelf();
                    projectiles.add(projectile.getValue());
                } catch (Exception e) {
                }
            } 

            Renderer.addGameObjects(projectiles.toArray(GameObject[]::new));
        });


        /**
         * Adding the state senders:
         *   - These are the states that will be sent to the server at regular intervals (of your choice).
         */
        Network.addStateSender(Header.PlayerState, 0);
        Network.addStateSender(Header.ProjectileState, 1000);
        
        /**
         * On game close callback:
         *   - This will ensure that the client disconnects from the server when the game is closed.
         */
        Renderer.setOnGameClose(() -> {
            try {
                Network.disconnect(); // Disconnect from the server
                // System.out.println("OIAJWJDAOIJWDJAOIWJDOIAJWIODJIOAJWDOIJAOIWJDOIJ");
            } catch (Exception e) {
                System.out.println("Error while disconnecting from the server:\n\n\n\n\n");
            }
        });
    }
}
