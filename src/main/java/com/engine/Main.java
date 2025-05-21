package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Sprite;
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

        Renderer.addDrawables(new Sprite(300, 400, new Image("src/main/resources/swing.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));

        Renderer.addDrawables(new CycleAnimateable(300, 300, 3, new Image("src/main/resources/swing1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/swing2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/swing3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/swing4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
                new Image("src/main/resources/swing5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE)));

        Renderer.start();
    }
}