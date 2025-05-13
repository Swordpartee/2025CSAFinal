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
    public DrawerCircle(double x, double y, double radius, boolean filled) {
        super(x, y, radius * 2, radius * 2, filled);
    }

    /**
     * gets the radius of the circle
     * @return radius
     */
    public double getRadius() {
        return getWidth() / 2;
    }

    @Override
    public void draw(Graphics graphic) {
        if(isFilled()) {
            graphic.fillOval((int) Math.round(this.getX()), (int) Math.round(this.getY()),
                (int) Math.round(getWidth()), (int) Math.round(getHeight()));
        } else {
            graphic.drawOval((int) Math.round(this.getX()), (int) Math.round(this.getY()),
                (int) Math.round(getWidth()), (int) Math.round(getHeight()));
        }
    }

}
