package com.engine;

import com.engine.game.PlayerController;
import com.engine.rendering.Renderer;
public class Main {
    public static void main(String[] args) {
        Renderer.setSize(640, 480);    

        PlayerController player = new PlayerController("src/main/resources/front1.spr");

        Renderer.addDrawables(player);

        Renderer.addUpdateables(player);

        Renderer.start();
    }
}