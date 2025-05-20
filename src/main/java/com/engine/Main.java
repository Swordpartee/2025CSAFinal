package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Animateable;
import com.engine.rendering.drawings.Background;

public class Main {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.ROCK_SPRITE));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        Renderer.addGameObjects(new PlayerController());

        Renderer.addDrawables(new Animateable(400, 400, 12, Constants.PlayerConstants.PLAYER_FRONT_SPRITE, Constants.PlayerConstants.PLAYER_RIGHT_SPRITE, Constants.PlayerConstants.PLAYER_BACK_SPRITE, Constants.PlayerConstants.PLAYER_LEFT_SPRITE));

        Renderer.start();
    }
}