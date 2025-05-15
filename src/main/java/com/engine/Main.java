package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Animateable;

public class Main {
    public static void main(String[] args) {
        PlayerController player = new PlayerController();

        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        Renderer.addGameObjects(player);

        Renderer.addDrawables(new Animateable(400, 400, 300, Constants.PlayerConstants.PLAYER_FRONT_SPRITE, Constants.PlayerConstants.PLAYER_BACK_SPRITE));

        Renderer.start();
    }
}