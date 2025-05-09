package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawerRect implements Drawable {
    private int x;
    private int y;

    private int width;
    private int height;

    private boolean filled;

    public DrawerRect(int x, int y, int width, int height, boolean filled) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.filled = filled;
    }

    /**
     * gets the x position of the rectangle
     * @return x position
     */
    public int getXPos() {
        return x;
    }

    /**
     * gets the y position of the rectangle
     * @return y position
     */
    public int getYPos() {
        return y;
    }

    /**
     * gets the width of the rectangle
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * gets the height of the rectangle
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * sets a new x position to the rectangle
     * @param newX the new x position
     */
    public void setXPos(int newX) {
        x = newX;
    }

    /**
     * sets a new y position to the rectangle
     * @param newY the new y position
     */
    public void setYPos(int newY) {
        y = newY;
    }

    /**
     * sets a new width to the rectangle
     * @param newWidth the new width
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
     * sets a new height to the rectangle
     * @param newHeight the new height
     */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    @Override
    public void draw(Graphics graphic) {
        if(filled) {
            graphic.fillRect(x, y, width, height);
        } else {
            graphic.drawRect(x, y, width, height);
        }
    }
}
