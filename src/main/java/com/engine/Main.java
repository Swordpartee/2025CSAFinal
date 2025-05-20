package com.engine;

import com.engine.game.objects.GameRect;
import com.engine.game.objects.PlayerController;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Animateable;
import com.engine.rendering.drawings.Background;
import com.engine.util.Color;

public class Main {
    public static void main(String[] args) {
        Renderer.addDrawables(new Background(Constants.GameConstants.getRockSprite()));
        
        Renderer.addGameObjects(new GameRect(200, 200, 50, 50, false));

        Renderer.addGameObjects(new PlayerController(Color.YELLOW));

        Renderer.addDrawables(new Animateable(400, 400, 12, Constants.PlayerConstants.getPlayerBackSprite(), Constants.PlayerConstants.getPlayerRightSprite(), Constants.PlayerConstants.getPlayerFrontSprite(), Constants.PlayerConstants.getPlayerLeftSprite()));

        Renderer.start();
    }
}