package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerSquare;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.rendering.drawings.DrawerOval;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        /* ADD DRAWABLES HERE */
        DrawerCircle circle = new DrawerCircle(200, 150, 50, false);

        renderer.addDrawable(circle);

        /* BUTTON BINDINGS HERE */
        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.W, () -> {
            circle.setYPos(circle.getYPos() - 5);
        });

        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.A, () -> {
            circle.setXPos(circle.getXPos() - 5);
        });

        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.S, () -> {
            circle.setYPos(circle.getYPos() + 5);
        });

        listener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.D, () -> {
            circle.setXPos(circle.getXPos() + 5);
        });

        renderer.addDrawable(new Sprite(100, 100, "src/main/resources/cutiepiept2.spr", 7));
        
        renderer.addDrawable(new Sprite(200, 100, "src/main/resources/left1.spr", 7));
        
        renderer.addDrawable(new Sprite(300,100,"src/main/resources/right1.spr", 7));

        renderer.start();
    }
}