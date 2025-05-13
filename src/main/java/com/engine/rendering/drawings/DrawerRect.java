package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Point;

public class DrawerRect extends Point implements Drawable {
    private double width;
    private double height;

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
        super(x, y);

        this.width = width;
        this.height = height;

        this.filled = filled;
    }

    /**
     * gets the width of the rectangle
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * gets the height of the rectangle
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * gets whether the rectangle is filled or not
     * @return true if filled, false if not
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * sets a new width to the rectangle
     * @param newWidth the new width
     */
    public void setWidth(double newWidth) {
        width = newWidth;
    }

    /**
     * sets a new height to the rectangle
     * @param newHeight the new height
     */
    public void setHeight(double newHeight) {
        height = newHeight;
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
            graphic.fillRect((int) Math.round(this.getX()), (int) Math.round(this.getY()), (int) Math.round(width), (int) Math.round(height));
        } else {
            graphic.drawRect((int) Math.round(this.getX()), (int) Math.round(this.getY()), (int) Math.round(width), (int) Math.round(height));
        }
    }
}
