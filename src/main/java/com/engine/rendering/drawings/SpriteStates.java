package com.engine.rendering.drawings;

import java.awt.Graphics;
import java.util.HashMap;

import com.engine.util.Tuple;

public class SpriteStates implements Drawable {
    private final Tuple<String, Drawable>[] states;
    private String currentState;

    public SpriteStates(Tuple<String, Drawable>... initialStates) {
        states = initialStates;
        currentState = null;
        if (initialStates.length > 0) {
            currentState = initialStates[0].getFirst();
        }
    }

    public void setCurrentState(String name) {
        for (Tuple<String, Drawable> state : states) {
            if (state.getFirst().equals(name)) {
                currentState = name;
                break;
            }
        }
    }

    public String getCurrentState() {
        return currentState;
    }

    public Drawable getCurrentSprite() {
        for (Tuple<String, Drawable> state : states) {
            if (state.getFirst().equals(currentState)) {
                return state.getSecond();
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics graphics) {
        Drawable currentSprite = getCurrentSprite();
        if (currentSprite != null) {
            currentSprite.draw(graphics);
        }
    }
}
