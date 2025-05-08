package com.engine.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.net.http.WebSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;

public class Renderer extends Frame {
    private final Canvas canvas;
    private final RenderListener listener;

    private int ha = 0;

    private final ScheduledExecutorService itterator = Executors.newScheduledThreadPool(4);

    public Renderer(RenderListener listener) {
        canvas = new Canvas();
        this.listener = listener;
        canvas.setPreferredSize(new Dimension(400, 300)); // Increased dimensions
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(false);
        add(canvas);
        pack(); // Adjust the frame size to fit the canvas

        setTitle("Game Engine Renderer");
        setResizable(false);
        setVisible(true);

        canvas.createBufferStrategy(2);

        listener.addBinding(EventCode.EventType.WINDOW_CLOSING, EventCode.ESC, () -> {
            System.exit(0);
            System.out.println("Window closed");
        });
        
        addListener(listener);
    }
    
    private void addListener(RenderListener listener) {
        addKeyListener(listener.getKeyListener());
        addMouseListener(listener.getMouseListener());
        addMouseMotionListener(listener.getMouseListener());
        addWindowListener(listener.getWindowListener());

        canvas.addKeyListener(listener.getKeyListener());
        canvas.addMouseListener(listener.getMouseListener());
        canvas.addMouseMotionListener(listener.getMouseListener());
    }

    public void start() {
        itterator.scheduleAtFixedRate(() -> {
            redraw();
        }, 0, 16, TimeUnit.MILLISECONDS);
    }
    
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
