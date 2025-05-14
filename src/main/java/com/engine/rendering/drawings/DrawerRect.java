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
        // Convert center coordinates to top-left for drawing
        int x = (int) Math.round(this.getX() - this.getWidth()/2);
        int y = (int) Math.round(this.getY() - this.getHeight()/2);
        int width = (int) Math.round(this.getWidth());
        int height = (int) Math.round(this.getHeight());
        
        if(filled) {
            graphic.fillRect(x, y, width, height);
        } else {
            graphic.drawRect(x, y, width, height);
        }
    }
}
