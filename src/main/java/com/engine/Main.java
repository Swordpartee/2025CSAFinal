package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        DrawerRect rect = new DrawerRect(0, 0, 100, 100, true);

        renderer.addDrawable(rect);

        renderer.start();
    }
}