package com.engine.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.engine.rendering.drawings.Drawable;

public class Renderer extends Frame {
    private final Canvas canvas;

    private ArrayList<Drawable> drawables;

    private final ScheduledExecutorService itterator = Executors.newScheduledThreadPool(4);

    /**
     * Creates a new Renderer instance.
     * Combines a Frame which handles the window and a Canvas which handles drawing.
     */
    public Renderer() {
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(400, 300)); // Increased dimensions
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(false);
        add(canvas);
        pack(); // Adjust the frame size to fit the canvas
        
        setTitle("Game Engine Renderer");
        setResizable(false);
        setVisible(true);

        canvas.createBufferStrategy(2);

        drawables = new ArrayList<Drawable>();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    /**
     * Starts the rendering loop.
     * Redraws the screen every 16 milliseconds.
     */
    public void start() {
        itterator.scheduleAtFixedRate(() -> {
            redraw();
        }, 0, 16, TimeUnit.MILLISECONDS);
    }

    /**
     * Adds a drawable element to be drawn
     * @param d drawable to add
     */
    public void addDrawable(Drawable d) {
        drawables.add(d);
    }
    
    /**
     * Redraws the screen.
     * Define your drawing logic here.
     */
    private void redraw() { 
        BufferStrategy buffer = canvas.getBufferStrategy();
        Graphics graphic = buffer.getDrawGraphics();

        // Clear the screen by creating a clear rectangle that's the size of the screen
        graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for(Drawable d : drawables) {
            d.draw(graphic);
        }

        graphic.dispose();
        buffer.show();
    }
}
