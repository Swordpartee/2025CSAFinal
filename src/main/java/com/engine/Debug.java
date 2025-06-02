package com.engine;

import java.awt.Color;

import com.engine.game.UI.Button;
import com.engine.game.objects.GameRect;
import com.engine.game.objects.HealthDisplay;
import com.engine.game.objects.Player;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Animateable;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Image;

public class Debug {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.getRockSprite()));

        Renderer.addUIElements(new Button(new GameRect(500, 75, 50, 50, true, Color.BLACK),
            () -> System.out.println("Button Clicked!")));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false, Color.BLACK));

        Player player = new Player();

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.U, () -> {
            player.damage(1);
        });
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.I, () -> {
            player.heal(1);
        });

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.SPACE, () -> {
            player.swing();
        });

        Renderer.addGameObjects(player);

        Player player1 = new Player();

        player1.setPosition(300, 300);

        Renderer.addDrawables(player1);
        Renderer.addDamageable(player1);

        Renderer.addDrawables(new CycleAnimateable(400, 400, 12, Constants.PlayerConstants.getPlayerUpSprite(),
            Constants.PlayerConstants.getPlayerRightSprite(), Constants.PlayerConstants.getPlayerDownSprite(),
                Constants.PlayerConstants.getPlayerLeftSprite()));

        HealthDisplay healthDisplay = new HealthDisplay(300, 300, 10);
            
        Renderer.addDrawables(healthDisplay);

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.J, () -> {
            healthDisplay.damage(1);
        });
        
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.K, () -> {
            healthDisplay.heal(1);
        });

        InstanceAnimateable swing = new InstanceAnimateable(200, 400, 5,
            new Image(),
            new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        CycleAnimateable fireball = new CycleAnimateable(100, 100, 5,
            new Image("src/main/resources/fireball/fireball1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball6.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball7.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball8.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball9.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball10.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        CycleAnimateable arrow = new CycleAnimateable(200, 200, 10,
            new Image("src/main/resources/arrow1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/arrow3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        Renderer.addDrawables(swing);
        Renderer.addDrawables(fireball);
        Renderer.addDrawables(arrow);
        
        // Renderer.addUIElements(new Button(player, () -> {
        //     swing.run();
        // }));

        
        Renderer.addDrawables(new CycleAnimateable(150, 100, 3, new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90)));

        Renderer.start();
    }
}