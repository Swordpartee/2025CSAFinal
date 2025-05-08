package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawerOval implements Drawable{
    private int x;
    private int y;

    private int width;
    private int height;

    private boolean filled;

    /**
     * Creates a new drawable oval
     * @param x position of the middle of the oval
     * @param y position of the middle of the oval
     * @param width of the oval
     * @param height of the oval
     * @param filled whether to fill the oval or not
     */
    public DrawerOval(int x, int y, int width, int height, boolean filled) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.filled = filled;
    }

    /**
     * gets the x position of the oval
     * @return x position
     */
    public int getXPos() {
        return x;
    }

    /**
     * gets the y position of the oval
     * @return y position
     */
    public int getYPos() {
        return y;
    }

    /**
     * gets the width of the oval
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * gets the height of the oval
     * @return height
     */
    public int getHeight() {
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
     * sets a new x position to the oval
     * @param newX the new x position
     */
    public void setXPos(int newX) {
        x = newX;
    }

    /**
     * sets a new y position to the oval
     * @param newY the new y position
     */
    public void setYPos(int newY) {
        y = newY;
    }

    /**
     * sets a new width to the oval
     * @param newWidth the new width
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
     * sets a new height to the oval
     * @param newHeight the new height
     */
    public void setHeight(int newHeight) {
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
            graphic.fillOval(x, y, width, height);
        } else {
            graphic.drawOval(x, y, width, height);
        }
    }
}
