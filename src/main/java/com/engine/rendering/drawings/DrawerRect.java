package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Rect;

public class DrawerRect extends Rect implements Drawable {
    private boolean filled;

    /**
     * Creates a new drawable rectangle
     * @param x position of the top left corner of the rectangle
     * @param y position of the top left corner of the rectangle
     * @param width of the rectangle
     * @param height of the rectangle
     * @param filled whether to fill the rectangle or not
     */
    public DrawerRect(double x, double y, double width, double height, boolean filled) {
        super(x, y, width, height);
        this.filled = filled;
    }

    /**
     * gets whether the rectangle is filled or not
     * @return true if filled, false if not
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * sets whether the rectangle is filled or not
     * @param filled true if filled, false if not
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public void draw(Graphics graphic) {
        if(filled) {
            graphic.fillRect((int) Math.round(this.getX()), (int) Math.round(this.getY()), 
                            (int) Math.round(this.getWidth()), (int) Math.round(this.getHeight()));
        } else {
            graphic.drawRect((int) Math.round(this.getX()), (int) Math.round(this.getY()), 
                            (int) Math.round(this.getWidth()), (int) Math.round(this.getHeight()));
        }
    }
}
