package com.engine.rendering.io;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import com.engine.util.Point;

public class RenderListener {
    public interface KeyTypedListener {
        void keyTyped(KeyEvent e);
    }

    private static final MouseAdapter mouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (mouseEvents[e.getButton()] == null) {  
                mouseEvents[e.getButton()] = e;

                callBinding(EventCode.EventType.MOUSE_PRESSED, e.getButton());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseEvents[e.getButton()] = null;

            callBinding(EventCode.EventType.MOUSE_RELEASED, e.getButton());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousePos.setX(e.getPoint().getX());
            mousePos.setY(e.getPoint().getY());

            callBinding(EventCode.EventType.MOUSE_MOVED);
        }
    };

    private static final KeyAdapter keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (keyEvents[e.getKeyCode()] == null) {
                keyEvents[e.getKeyCode()] = e;
    
                callBinding(EventCode.EventType.KEY_PRESSED, e.getKeyCode());
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {

            
            // System.out.println("A");
            keyTypedListeners.forEach(listener -> listener.keyTyped(e));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyEvents[e.getKeyCode()] = null;
            
            callBinding(EventCode.EventType.KEY_RELEASED, e.getKeyCode());
        }
    };

    private static final WindowAdapter windowListener = new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
            callBinding(EventCode.EventType.WINDOW_CLOSED);
        }
        
        @Override
        public void windowClosing(WindowEvent e) {
            callBinding(EventCode.EventType.WINDOW_CLOSING);
        }

        @Override
        public void windowGainedFocus(WindowEvent e) {
            callBinding(EventCode.EventType.WINDOW_FOCUSED);

        }
    };
    
    public static MouseAdapter getMouseListener() {
        return mouseListener;
    }

    public static KeyAdapter getKeyListener() {
        return keyListener;
    }

    public static WindowAdapter getWindowListener() {
        return windowListener;
    }

    private static final List<Binding> bindings = new ArrayList<>();

    private static final MouseEvent[] mouseEvents = new MouseEvent[3];
    private static final KeyEvent[] keyEvents = new KeyEvent[256];

    private static final ArrayList<KeyTypedListener> keyTypedListeners = new ArrayList<>();

    private static final Point mousePos = new Point();

    public static Point getMousePos() {
        return mousePos;
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyEvents[keyCode] != null;
    }

    public static boolean isKeyPressed(EventCode code) {
        return isKeyPressed(code.getValue());
    }

    public static boolean isMousePressed(int button) {
        return mouseEvents[button] != null;
    }

    public static boolean isMousePressed(EventCode code) {
        return isMousePressed(code.getValue());
    }

    public static void addBinding(EventCode.EventType eventType, EventCode code, Runnable action) {
        bindings.add(new Binding(eventType, code, action));
    }

    public static void addBinding(EventCode.EventType eventType, Runnable action) {
        bindings.add(new Binding(eventType, action));
    }

    public static void addBinding(Binding binding) {
        bindings.add(binding);
    }

    private static void callBinding(EventCode.EventType eventType, int code) {
        for (Binding binding : bindings) {
            if (binding.getEventType() == eventType && binding.getCode().getValue() == code) {
                binding.run();
            }
        }
    }

    private static void callBinding(EventCode.EventType eventType) {
        for (Binding binding : bindings) {
            if (binding.getEventType() == eventType) {
                binding.run();
            }
        }
    }

    public static void addKeyTypedListener(KeyTypedListener listener) {
        keyTypedListeners.add(listener);
    }
}
