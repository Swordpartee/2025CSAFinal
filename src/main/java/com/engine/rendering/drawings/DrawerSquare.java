package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawerSquare extends DrawerRect {
    /**
     * Creates a new drawable square
     * @param x position of the top left corner of the square
     * @param y position of the top left corner of the square
     * @param size of the square
     * @param filled whether to fill the square or not
     */
    public DrawerSquare(double x, double y, double size, boolean filled) {
        super(x, y, size, size, filled);
    }

    @Override
    public void draw(Graphics graphic) {
        if (isFilled()) {
            graphic.fillRect((int) Math.round(getX()), (int) Math.round(getY()),
                (int) Math.round(getWidth()), (int) Math.round(getHeight()));
        } else {
            graphic.drawRect((int) Math.round(getX()), (int) Math.round(getY()),
                (int) Math.round(getWidth()), (int) Math.round(getHeight()));
        }
    }
}
