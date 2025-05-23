package com.engine.game.UI;

import java.awt.Graphics;

import com.engine.game.objects.GameObject;
import com.engine.util.Point;

public class Button implements UIElement {
    private final Runnable action;

    private final GameObject object;

    public Button(GameObject object, Runnable action) {
        this.action = action;
        this.object = object;
    }

    @Override
    public void draw(Graphics graphic) {
        object.draw(graphic);
    }

    @Override
    public void onClick(Point point) {
        if (object.colliding(point)) {
            action.run();
        }
    }
}
