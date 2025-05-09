package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        DrawerRect rect = new DrawerRect(0, 0, 100, 100, true);

        renderer.addDrawable(rect);

        renderer.addDrawable(new Sprite(100, 100, "src/main/resources/cutiepiept2.spr", 7));
        
        renderer.addDrawable(new Sprite(200, 100, "src/main/resources/left1.spr", 7));
        
        renderer.addDrawable(new Sprite(300,100,"src/main/resources/right1.spr", 7));

        renderer.start();
    }
}