package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;

public class Sprite implements Drawable {
    private final Point center;

    private final Image image;

    /**
     * Creates a new Sprite Drawable.
     * @param x position of the center of the sprite
     * @param y position of the center of the sprite
     * @param image the image to set the sprite to
     */
    public Sprite(double x, double y, Image image) {
        this.center = new Point(x, y);

        this.image = image;
    }
    
    /**
     * Creates a new Sprite Drawable.
     * @param center the x y position of the center of the sprite
     * @param image the image to set the sprite to
     */
    public Sprite(Point center, Image image) {
        this.center = center;

        this.image = image;
    }

    /**
     * Sets a new x-coordinate for the sprite
     * @param newX the new x-coordinate
     */
    public void setX(double newX) {
        center.setX(newX);
    }

    /**
     * Sets a new y-coordinate for the sprite
     * @param newY the new y-coordinate
     */
    public void setY(double newY) {
        center.setY(newY);
    }

    /**
     * Gets the current x-coordinate of the sprite
     * @return the current x-coordinate
     */
    public double getX() {
        return center.getX();
    }

    /**
     * Gets the current y-coordinate of the sprite
     * @return the current y-coordinate
     */
    public double getY() {
        return center.getY();
    }

    /**
     * Moves the sprite by a given amount in the x direction
     * @param dx the amount to move the sprite by
     */
    public void moveX(double dx) {
        center.moveX(dx);
    }

    /**
     * Moves the sprite by a given amount in the y direction
     * @param dy the amount to move the sprite by
     */
    public void moveY(double dy) {
        center.moveY(dy);
    }

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, center.getX(), center.getY());
    }
}
