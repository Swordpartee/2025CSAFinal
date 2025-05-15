package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;

public class Main {
    public static void main(String[] args) {
        PlayerController player = new PlayerController();

        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        Renderer.addGameObjects(player);

        Renderer.start();
    }
}