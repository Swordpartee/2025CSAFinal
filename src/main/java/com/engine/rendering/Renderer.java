package com.engine.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;

public class Renderer extends Frame {
    private final Canvas canvas;

    private final ArrayList<Drawable> drawables;

    private final ArrayList<Runnable> processes;

    private final ScheduledExecutorService itterator = Executors.newScheduledThreadPool(4);

    /**
     * Creates a new Renderer instance.
     * Combines window elements, canvas drawing, and user inputs
     * @param listener takes in inputs from the user
     * @param ps processes you want to run iteratively
     */
    public Renderer(int width, int height, RenderListener listener) {
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height)); // Increased dimensions
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(false);
        add(canvas);
        pack(); // Adjust the frame size to fit the canvas

        setTitle("Game Engine Renderer");
        setResizable(false);
        setVisible(true);

        canvas.createBufferStrategy(2);

        drawables = new ArrayList<>();

        processes = new ArrayList<>();

        // basic binding of the window closing
        listener.addBinding(EventCode.EventType.WINDOW_CLOSING, EventCode.ESC, () -> {
            System.out.println("Window closed");
            System.exit(0);
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

    /**
     * Adds a process to be run
     * @param ps processes to add
     */
    public void addProcesses(Runnable... ps) {
        processes.addAll(Arrays.asList(ps));
    }

    /**
     * Adds a drawable element to be drawn
     * @param ds drawables to add
     */
    public void addDrawables(Drawable... ds) {
        drawables.addAll(Arrays.asList(ds));
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
        BufferStrategy buffer = canvas.getBufferStrategy();
        Graphics graphic = buffer.getDrawGraphics();

        // Clear the screen by creating a clear rectangle that's the size of the screen
        graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draws all the created drawable objects
        for(Drawable d : drawables) d.draw(graphic);

        // runs all created processes
        for(Runnable p : processes) p.run();

        graphic.dispose();
        buffer.show();
    }
}
