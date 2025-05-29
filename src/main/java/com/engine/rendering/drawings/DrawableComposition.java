package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawableComposition implements Drawable {
    private Drawable[] drawables;

    public DrawableComposition(Drawable... drawables) {
        this.drawables = drawables;
    }

    @Override
    public void draw(Graphics graphics) {
        for (Drawable drawable : drawables) {
            drawable.draw(graphics);
        }
    }

}
