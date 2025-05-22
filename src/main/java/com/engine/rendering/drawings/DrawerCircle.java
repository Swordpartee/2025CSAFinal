package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Circle;
import com.engine.util.PointConfig;

public class DrawerCircle extends Circle implements Drawable {
    private boolean filled;


    public DrawerCircle(PointConfig center, double radius, boolean filled) {
        super(center, radius);
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
