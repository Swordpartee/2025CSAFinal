package com.engine.rendering;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.engine.Constants;
import com.engine.game.collision.Collider;
import com.engine.game.objects.GameObject;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Updateable;

public class Renderer {
    private static final Frame frame = new Frame();
    private static final Canvas canvas = new Canvas();

    private static final ArrayList<Drawable> drawables = new ArrayList<>();

    private static final ArrayList<Collider> collidables = new ArrayList<>();

    private static final ArrayList<Updateable> updateables = new ArrayList<>();

    private static final ArrayList<Runnable> processes = new ArrayList<>();

    private static int width = Constants.GameConstants.GAME_WIDTH;
    private static int height = Constants.GameConstants.GAME_HEIGHT;

    private static final ScheduledExecutorService iterator = Executors.newScheduledThreadPool(Constants.RendererConstants.THREADS);

    private static void init() {
        canvas.setPreferredSize(new Dimension(Constants.GameConstants.GAME_WIDTH, Constants.GameConstants.GAME_HEIGHT));
        canvas.setIgnoreRepaint(true);
        canvas.setFocusable(true);
        frame.add(canvas);
        frame.pack(); // Adjust the frame size to fit the canvas

        frame.setTitle("Game Engine Renderer");
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(Constants.RendererConstants.BUFFERS);

        addListener();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                iterator.shutdown();
                System.exit(0);
            }
        });
    }

    /**
     * Starts the rendering loop.
     * Redraws the screen every 16 milliseconds.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void start() {
        init();

        iterator.scheduleAtFixedRate(() -> {
            try {
                redraw();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, Constants.RendererConstants.FRAME_DELAY, TimeUnit.MILLISECONDS);
    }
    
    public static void setSize(int newWidth, int newHeight) {
        width = newWidth;
        height = newHeight;
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void addListener() {
        frame.addKeyListener(RenderListener.getKeyListener());
        frame.addMouseListener(RenderListener.getMouseListener());
        frame.addMouseMotionListener(RenderListener.getMouseListener());
        frame.addWindowListener(RenderListener.getWindowListener());

        canvas.addKeyListener(RenderListener.getKeyListener());
        canvas.addMouseListener(RenderListener.getMouseListener());
        canvas.addMouseMotionListener(RenderListener.getMouseListener());
    }

    /**
     * Adds a process to be run
     * @param ps processes to add
     */
    public static void addProcesses(Runnable... ps) {
        processes.addAll(Arrays.asList(ps));
    }

    /**
     * Adds an updateable object to be updated
     * @param us updateables to add
     */
    public static void addUpdateables(Updateable... us) {
        updateables.addAll(Arrays.asList(us));
    }

    /**
     * Adds a collidable object to be checked for collisions
     * @param cs collidables to add
     */
    public static void addCollidables(Collider... cs) {
        collidables.addAll(Arrays.asList(cs));
    }

    /**
     * Adds a drawable element to be drawn
     * @param ds drawables to add
     */
    public static void addDrawables(Drawable... ds) {
        drawables.addAll(Arrays.asList(ds));
    }

    public static void addGameObjects(GameObject... gs) {
        for (GameObject g : gs) {
            addDrawables(g);
            addUpdateables(g);
            addCollidables(g);
        }
    }

    public static Collider[] getCollidables() {
        return collidables.toArray(Collider[]::new);
    }
    
    /**
     * Redraws the screen.
     * Define your drawing logic here.
     */
    private static void redraw() { 
        BufferStrategy buffer = canvas.getBufferStrategy();
        Graphics graphic = buffer.getDrawGraphics();

        // Clear the screen by creating a clear rectangle that's the size of the screen
        graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draws all the created drawable objects
        for (Drawable d : drawables)
            d.draw(graphic);

        // updates all created updateable objects
        for (Updateable u : updateables)
            u.update();

        // runs all created processes
        for (Runnable p : processes)
            p.run();

        graphic.dispose();
        buffer.show();
    }
}
