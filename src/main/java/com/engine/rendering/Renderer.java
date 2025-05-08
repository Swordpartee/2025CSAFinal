package com.engine.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferStrategy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Renderer extends Frame {
    private final Canvas canvas;

    private int ha = 0;

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
     * Redraws the screen.
     * Define your drawing logic here.
     */
    private void redraw() { 
        BufferStrategy bs = canvas.getBufferStrategy();
        Graphics g = bs.getDrawGraphics();

        // Clear the screen
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // For example, draw a simple rectangle
        ha += 3;
        ha %= canvas.getHeight();
        g.fillRect(50, ha, 100, 100);
        g.fillRect(50, ha - canvas.getHeight(), 100, 100);

        g.dispose();
        bs.show();
    }
}
