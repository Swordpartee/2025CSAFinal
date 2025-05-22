package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Color;
import com.engine.util.Image;

public class Main {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.getRockSprite()));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        Renderer.addGameObjects(new PlayerController(Color.YELLOW));

        Renderer.addDrawables(new CycleAnimateable(400, 400, 12, Constants.PlayerConstants.getPlayerBackSprite(),
                Constants.PlayerConstants.getPlayerRightSprite(), Constants.PlayerConstants.getPlayerFrontSprite(),
                Constants.PlayerConstants.getPlayerLeftSprite()));

        InstanceAnimateable swing = new InstanceAnimateable(200, 400, 5,
                new Image(),
                new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        Renderer.addDrawables(new Sprite(100, 400, new Image("src/main/resources/leftswing.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));
        Renderer.addDrawables(swing);
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.E, () -> {
            swing.run();
        });

        Renderer.addDrawables(new CycleAnimateable(300, 100, 3, new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));

        Renderer.start();
    }
}