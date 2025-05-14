package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerSquare;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.rendering.drawings.DrawerOval;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        /* ADD DRAWABLES HERE */
        DrawerCircle circle = new DrawerCircle(200, 150, 50, false);
        
        renderer.addDrawables(new Background(5, 5, 4, "src/main/resources/boo.spr"));

        renderer.addDrawable(circle);


        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.A, () -> {
            circle.setXPos(circle.getXPos() - 5);
        });

        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.S, () -> {
            circle.setYPos(circle.getYPos() + 5);
        });

        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.D, () -> {
            circle.setXPos(circle.getXPos() + 5);
        });

        renderer.start();
    }
}