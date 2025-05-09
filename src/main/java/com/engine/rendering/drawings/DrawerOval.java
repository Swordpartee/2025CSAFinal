package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Point;

public class DrawerOval extends Point implements Drawable{

    private double width;
    private double height;

    private boolean filled;

    /**
     * Creates a new drawable oval
     * @param x position of the middle of the oval
     * @param y position of the middle of the oval
     * @param width of the oval
     * @param height of the oval
     * @param filled whether to fill the oval or not
     */
    public DrawerOval(double x, double y, double width, double height, boolean filled) {
        super(x, y);

        this.width = width;
        this.height = height;

        this.filled = filled;
    }

    /**
     * gets the width of the oval
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * gets the height of the oval
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * gets whether the oval is filled or not
     * @return true if filled, false otherwise
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * sets a new width to the oval
     * @param newWidth the new width
     */
    public void setWidth(double newWidth) {
        width = newWidth;
    }

    /**
     * sets a new height to the oval
     * @param newHeight the new height
     */
    public void setHeight(double newHeight) {
        height = newHeight;
    }

    /**
     * sets whether the oval is filled or not
     * @param newFilled true if filled, false otherwise
     */
    public void setFilled(boolean newFilled) {
        filled = newFilled;
    }

    @Override
    public void draw(Graphics graphic) {
        if(filled) {
            graphic.fillOval((int) Math.round(this.getX()), (int) Math.round(this.getY()), (int) Math.round(width), (int) Math.round(height));
        } else {
            graphic.drawOval((int) Math.round(this.getX()), (int) Math.round(this.getY()), (int) Math.round(width), (int) Math.round(height));
        }
    }
}
