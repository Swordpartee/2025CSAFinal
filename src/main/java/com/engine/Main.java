package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerRect;

public class Main {
    public static void main(String[] args) {
        Renderer renderer = new Renderer();

        renderer.start();

        DrawerRect rect = new DrawerRect(0, 0, 100, 100, true);

        renderer.addDrawable(rect);
    }
}