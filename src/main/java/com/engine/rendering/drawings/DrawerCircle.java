package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Circle;

public class DrawerCircle extends Circle implements Drawable {
    private boolean filled;

    /**
     * Creates a new drawable circle
     * @param x position of the center of the circle
     * @param y position of the center of the circle
     * @param radius of the circle
     * @param filled whether to fill the circle or not
     */
    public DrawerCircle(double x, double y, double radius, boolean filled) {
            super(x, y, radius);
            this.filled = filled;
    }

    /**
     * gets whether the circle is filled or not
     * @return true if filled, false if not
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * sets whether the circle is filled or not
     * @param filled true if filled, false if not
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public void draw(Graphics graphic) {
        if(filled) {
            graphic.fillOval((int) Math.round(this.getX() - this.getRadius()), (int) Math.round(this.getY() - this.getRadius()), 
                            (int) Math.round(this.getRadius() * 2), (int) Math.round(this.getRadius() * 2));
        } else {
            graphic.drawOval((int) Math.round(this.getX() - this.getRadius()), (int) Math.round(this.getY() - this.getRadius()), 
                            (int) Math.round(this.getRadius() * 2), (int) Math.round(this.getRadius() * 2));
        }
    }
}
