package com.engine;

import com.engine.rendering.Renderer;
import com.engine.rendering.io.RenderListener;

public class Main {
    public static void main(String[] args) {
        RenderListener listener = new RenderListener();
        Renderer renderer = new Renderer(listener);

        renderer.start();
    }
}