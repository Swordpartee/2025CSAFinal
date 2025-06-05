// package com.engine;

// import java.util.ArrayList;

// import com.engine.game.objects.GameObject;
// import com.engine.game.objects.Player;
// import com.engine.game.objects.Projectile;
// import com.engine.network.Network;
// import com.engine.network.headers.BaseHeader;
// import com.engine.network.headers.Header;
// import com.engine.network.states.ControlMode;
// import com.engine.network.states.NetState;
// import com.engine.rendering.Renderer;
// import com.engine.rendering.drawings.Background;
// import com.engine.rendering.io.EventCode;
// import com.engine.rendering.io.RenderListener;
// import com.engine.util.Image;

// public class AutoLoginGame {

//     public static void loginAndJoinRoom() {
//         try {
//             Network.client.sendSessionPacketAndWait(BaseHeader.AuthLogin.value(), ("a:bbbbbb").getBytes(), new byte[][] {BaseHeader.AuthLogin.value(), BaseHeader.AuthError.value()});
//             Network.client.sendSessionPacketAndWait(BaseHeader.JoinRoom.value(), ("a:a").getBytes(), new byte[][] {BaseHeader.JoinRoom.value(), BaseHeader.RoomError.value()});
//         } catch (Exception e) {
//             System.out.println("Error while logging in or joining room:");
//         }
//     }

//     public static void main(String[] args) throws Exception {

//         Background background = new Background(new Image("src/main/resources/solidbricks.spr", 5), new Image("src/main/resources/kindacrackedbricks.spr", 5));
//         Renderer.addDrawables(background);

//         /**
//          * Initializing the game and its necessary components:
//          *   - Sets up the network connection to the server.
//          *   - Sets up the rendering engine and prepare it for drawing.
//          *   - Shows a login menu to the user.
//          */
//         Network.connect("localhost", 8888);
//         Renderer.start();
//         loginAndJoinRoom();

//         /** 
//          * Creating the states here:
//          *   - The player state is a networked state that will be synchronized across clients.
//          */
//         NetState<Player> player = new NetState<>(Header.PlayerState, Network.stateManager, new Player());

//         /**
//          * Creating the drawables and non-networked game objects:
//          *   - The background is a drawable grid that will render behind everything.
//          */

//         /**
//          * Adding the drawables and game objects to the renderer:
//          *   - Not much to say here
//          */
//         Renderer.addGameObjects(player.getValue());
//         Renderer.addDamageable(player.getValue());

//         RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.SPACE, () -> {
//             player.getValue().swing();
//         });

//         RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.I, () -> {
//             player.getValue().heal(1);
//         });

//         // TEMP SHOOT PROJECTILES - put it somewhere else later, idk, just make it more organized
//         RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.R, () -> {
//             ArrayList<Projectile> projectiles = new ArrayList<>();

//             int numProjectiles = 100; // Number of projectiles to fire
//             for (int i = 0; i < numProjectiles; i++) {
//                 NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager,
//                     new Projectile(player.getValue().getPoint().copy(), player.getValue()));
//                 projectile.setControlMode(ControlMode.BOTH); // Allow both server and client to control the projectile (Aka. Move + Delete it)
//                 projectile.getValue().getVelocity().setX(5);
//                 // Evenly space projectiles around the player's Y center
//                 double spacing = 1; // pixels between projectiles
//                 double centerY = player.getValue().getPoint().getY();
//                 double offset = (i - (numProjectiles - 1) / 2.0) * spacing;
//                 projectile.getValue().getPosition().setY(centerY + offset);
//                 try {
//                     projectile.sendSelf();
//                     projectiles.add(projectile.getValue());
//                 } catch (Exception e) {
//                     e.printStackTrace();
//                 }
//             } 

//             Renderer.addGameObjects(projectiles.toArray(GameObject[]::new));
//         });


//         /**
//          * Adding the state senders:
//          *   - These are the states that will be sent to the server at regular intervals (of your choice).
//          */
//         Network.addStateSender(Header.PlayerState, 0);
//         Network.addStateSender(Header.ProjectileState, 1000);
        
//         /**
//          * On game close callback:
//          *   - This will ensure that the client disconnects from the server when the game is closed.
//          */
//         Renderer.setOnGameClose(() -> {
//             try {
//                 Network.disconnect(); // Disconnect from the server
//                 // System.out.println("OIAJWJDAOIJWDJAOIWJDOIAJWIODJIOAJWDOIJAOIWJDOIJ");
//             } catch (Exception e) {
//                 System.out.println("Error while disconnecting from the server:\n\n\n\n\n");
//                 e.printStackTrace();
//             }
//         });
//     }
// }
