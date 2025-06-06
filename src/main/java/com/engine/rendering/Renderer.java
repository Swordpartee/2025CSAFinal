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
import com.engine.game.UI.Textbox;
import com.engine.game.UI.UIElement;
import com.engine.game.collision.Collider;
import com.engine.game.collision.Damageable;
import com.engine.game.objects.GameObject;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Clickable;
import com.engine.util.Updateable;

public class Renderer {
    private static final Frame frame = new Frame();
    private static final Canvas canvas = new Canvas();

    private static final ArrayList<Drawable> drawables = new ArrayList<>();

    private static final ArrayList<Collider> collidables = new ArrayList<>();

    private static final ArrayList<Updateable> updateables = new ArrayList<>();

    private static final ArrayList<Runnable> processes = new ArrayList<>();

    private static final ArrayList<Clickable> clickables = new ArrayList<>();

    private static final ArrayList<Damageable> damageables = new ArrayList<>();


    private static int width = Constants.GameConstants.GAME_WIDTH;
    private static int height = Constants.GameConstants.GAME_HEIGHT;

    private static final ScheduledExecutorService iterator = Executors.newScheduledThreadPool(Constants.RendererConstants.THREADS);
    
    private static Runnable onGameClose = () -> {};

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

        RenderListener.addBinding(EventCode.EventType.MOUSE_PRESSED, EventCode.LEFT_MOUSE, () -> {
            for (Clickable clickable : clickables) {
                clickable.onClick(RenderListener.getMousePos());
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onGameClose.run();
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
                System.out.println("Error in rendering loop: " + e.getMessage());
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

    public static void setOnGameClose(Runnable onGameClose) {
        Renderer.onGameClose = onGameClose;
    }

    public static void addListener() {
        RenderListener.addKeyTypedListener(Textbox.getKeyTypedListener());
        
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
     * @param runnable 
     * @param ps processes to add
     */
    public static void addProcesses(Runnable... ps) {
        processes.addAll(Arrays.asList(ps));
    }

    /**
     * Removes a process from the renderer
     * @param ps processes to remove
     */
    public static void removeProcesses(Runnable... ps) {
        processes.removeAll(Arrays.asList(ps));
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
     * Removes a collidable object to be checked for collisions
     * @param cs collidables to remove
     */
    public static void removeCollidables(Collider... cs) {
        collidables.addAll(Arrays.asList(cs));
    }

    /**
     * Adds a drawable element to be drawn
     * @param ds drawables to add
     */
    public static void addDrawables(Drawable... ds) {
        drawables.addAll(Arrays.asList(ds));
    }

    /**
     * Removes drawables from the renderer
     * @param ds : drawables to remove
     */
    public static void removeDrawables(Drawable... ds) {
        drawables.removeAll(Arrays.asList(ds));
    }

    public static void addClickables(Clickable... cs) {
        clickables.addAll(Arrays.asList(cs));
    }

    public static void addGameObjects(GameObject... gs) {
        addDrawables(gs);
        addUpdateables(gs);
        addCollidables(gs);
    }

    public static void addUIElements(UIElement... us) {
        addDrawables(us);
        addClickables(us);
    }

    public static void addDamageable(Damageable... ds) {
        damageables.addAll(Arrays.asList(ds));
    }
    public static void removeDamageable(Damageable... ds) {
        damageables.removeAll(Arrays.asList(ds));
    }

    public static ArrayList<Damageable> getDamageables() {
        return damageables;
    }

    /**
     * Removes ui elements from the renderer
     * @param us : ui elements to remove
     */
    public static void removeUIElements(UIElement... us) {
        removeDrawables(us);
        clickables.removeAll(Arrays.asList(us));
    }

    /**
     * Removes drawables from the renderer
     * @param ds : drawables to remove
     */
    public static void removeGameObjects(GameObject... ds) {
        try {
            if (ds == null || ds.length == 0) {
                return;
            }
            for (GameObject obj : ds) {
                if (!drawables.contains(obj) && !updateables.contains(obj) && !collidables.contains(obj)) {
                    // Object does not exist in any list, skip or log if needed
                    continue;
                }
                
                drawables.remove(obj);
                updateables.remove(obj);
                collidables.remove(obj);
            }
        } catch (Exception e) {
            System.out.println("Error removing game objects: " + e.getMessage());
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
        // Draw all drawable objects
        synchronized (drawables) {
            for (int i = 0, size = drawables.size(); i < size; i++) {
                if (i >= drawables.size()) { break; }
                if (drawables.get(i) == null) { drawables.remove(i); i--; continue; }
                drawables.get(i).draw(graphic);
            }
        }

        // Update all updateable objects
        synchronized (updateables) {
            for (int i = 0, size = updateables.size(); i < size; i++) {
                if (i >= updateables.size()) { break; }
                if (updateables.get(i) == null) { updateables.remove(i); i--; continue; }
                updateables.get(i).update();
            }
        }

        // Run all processes
        synchronized (processes) {
            for (int i = 0, size = processes.size(); i < size; i++) {
                if (i >= processes.size()) { break; }
                if (processes.get(i) == null) { processes.remove(i); i--; continue; }
                processes.get(i).run();
            }
        }

        graphic.dispose();
        buffer.show();
    }
}
