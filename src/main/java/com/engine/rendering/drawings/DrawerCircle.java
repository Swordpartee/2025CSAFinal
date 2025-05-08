package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawerCircle extends DrawerOval {
    /**
     * Creates a new drawable circle
     * @param x position of the middle of the circle
     * @param y position of the middle of the circle
     * @param radius of the circle
     * @param filled whether to fill the circle or not
     */
    public DrawerCircle(int x, int y, int radius, boolean filled) {
        super(x, y, radius, radius, filled);
    }

    /**
     * gets the radius of the circle
     * @return radius
     */
    public int getRadius() {
        return getWidth() / 2;
    }

    @Override
    public void draw(Graphics graphic) {
        if(isFilled()) {
            graphic.fillOval(getXPos(), getYPos(), getWidth(), getHeight());
        } else {
            graphic.drawOval(getXPos(), getYPos(), getWidth(), getHeight());
        }
    }

}
