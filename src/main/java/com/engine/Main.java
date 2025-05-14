package com.engine;

import com.engine.game.PlayerController;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerRect;
public class Main {
    public static void main(String[] args) {
        Renderer.setSize(640, 480);    

        PlayerController player = new PlayerController("src/main/resources/front1.spr");

        Renderer.addCollidables(new RectCollider(200, 200, 7 * 16, 7 * 16));
        Renderer.addDrawables(new DrawerRect(200, 200, 7 * 16, 7 * 16, true));

        Renderer.addDrawables(player);

        Renderer.addUpdateables(player);

        Renderer.start();
    }
}