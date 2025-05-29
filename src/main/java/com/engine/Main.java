package com.engine;

import com.engine.game.UI.Button;
import com.engine.game.objects.GameRect;
import com.engine.game.objects.HealthDisplay;
import com.engine.game.objects.Player;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Image;

public class Main {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.getRockSprite()));

        Renderer.addUIElements(new Button(new GameRect(500, 75, 50, 50, true),
            () -> System.out.println("Button Clicked!")));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        // PlayerController player = new PlayerController(Color.RED);

        // Renderer.addGameObjects(player);

        // RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.U, () -> {
        //     player.damage();
        // });
        // RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.I, () -> {
        //     player.heal();
        // });

        Renderer.addGameObjects(new Player());

        Renderer.addDrawables(new CycleAnimateable(400, 400, 12, Constants.PlayerConstants.getPlayerBackSprite(),
            Constants.PlayerConstants.getPlayerRightSprite(), Constants.PlayerConstants.getPlayerFrontSprite(),
                Constants.PlayerConstants.getPlayerLeftSprite()));

        HealthDisplay healthDisplay = new HealthDisplay(300, 300, 10);
            
        Renderer.addDrawables(healthDisplay);

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.J, () -> {
            healthDisplay.damage(1);
        });
        
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.K, () -> {
            healthDisplay.heal();
        });

        InstanceAnimateable swing = new InstanceAnimateable(200, 400, 5,
            new Image(),
            new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        Renderer.addDrawables(swing);
        
        // Renderer.addUIElements(new Button(player, () -> {
        //     swing.run();
        // }));

        Renderer.addDrawables(new Sprite(50, 400, new Image("src/main/resources/leftswing.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));

        Renderer.addDrawables(new CycleAnimateable(300, 100, 3, new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));

        Renderer.start();
    }
}