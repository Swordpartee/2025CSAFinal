package com.engine;

import java.awt.Color;

import com.engine.game.UI.Button;
import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.util.Image;

public class Main {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.getRockSprite()));

        Renderer.addUIElements(new Button(new GameRect(500, 75, 50, 50, true, Color.BLACK),
            () -> System.out.println("Button Clicked!")));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false, Color.BLACK));

        PlayerController player = new PlayerController(Color.RED);

        Renderer.addGameObjects(player);

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

        Renderer.addDrawables(swing);
        
        Renderer.addUIElements(new Button(player, () -> {
            swing.run();
        }));

        Renderer.addDrawables(new CycleAnimateable(300, 100, 3, new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));
        
        Renderer.addDrawables(new CycleAnimateable(150, 100, 3, new Image("src/main/resources/rightswing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90),
            new Image("src/main/resources/rightswing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE, -90)));

        Renderer.start();
    }
}