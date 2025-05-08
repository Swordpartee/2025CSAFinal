package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerSquare;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.rendering.drawings.DrawerOval;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        /* ADD DRAWABLES HERE */
        DrawerCircle circle = new DrawerCircle(200, 50, 50, false);

        renderer.addDrawable(circle);

        renderer.start();
    }
}