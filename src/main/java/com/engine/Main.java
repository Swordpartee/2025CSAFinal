package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerSquare;
import com.engine.rendering.drawings.Background;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.rendering.drawings.DrawerOval;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.drawings.DrawerSquare;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();

        Renderer.addListener(listener);

        Renderer.setSize(640, 480);

        /* ADD DRAWABLES HERE */
        DrawerCircle circle = new DrawerCircle(200, 150, 50, false);

        Renderer.addDrawables(circle);

        Renderer.addDrawables(new Sprite(100, 100, "src/main/resources/cutiepiept2.spr", 7));
        
        Renderer.addDrawables(new Sprite(200, 100, "src/main/resources/left1.spr", 7));
        
        Renderer.addDrawables(new Sprite(300,100,"src/main/resources/right1.spr", 7));

        /* ADD BINDINGS HERE */
        Renderer.addProcesses(() -> {
            if (listener.isKeyPressed(EventCode.W)) {
                circle.moveY(-2);
            }
            if (listener.isKeyPressed(EventCode.S)) {
                circle.moveY(2);
            }
            if (listener.isKeyPressed(EventCode.A)) {
                circle.moveX(-2);
            }
            if (listener.isKeyPressed(EventCode.D)) {
                circle.moveX(2);
            }
        });

        Renderer.start();
    }
}