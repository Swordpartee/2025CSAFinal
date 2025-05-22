package com.engine.game.objects;

import com.engine.rendering.drawings.DrawerText;
import com.engine.util.PointConfig;

public class Button extends GameRect {
    private final DrawerText text;
    private final Runnable action;

    public Button(PointConfig position, int width, int height, String text, Runnable action) {
        super(position, width, height, true);

        this.text = new DrawerText(position, text);
        this.action = action;
    }

    @Override
    public void draw(java.awt.Graphics graphic) {
        draw(graphic);
        text.draw(graphic);
    }
}
