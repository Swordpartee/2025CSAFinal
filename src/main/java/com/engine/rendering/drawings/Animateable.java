package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;

public class Animateable implements Drawable {
    private final Point center;

    private final double speed;
    private final Sprite[] frames;

    private double time;

    /**
     * Creates a new Animateable Drawable.
     * @param x position of the center of the animation
     * @param y position of the center of the animation
     * @param speed number of frames between each frame switch
     * @param Frames number of frames to switch between during the animation
     */
    public Animateable(double x, double y, double speed, Image... Frames) {
        this.center = new Point(x, y);
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(center, Frames[i]);
        }
    }

    /**
     * Sets the x-coordinate of the animation
     * @param newX the new x-coordinate
     */
    public void setX(double newX) {
        center.setX(newX);
    }

    /**
     * Sets the y-coordinate of the animation
     * @param newY the new y-coordinate
     */
    public void setY(double newY) {
        center.setY(newY);
    }

    /**
     * Gets the current x-coordinate of the animation
     * @return the current x-coordinate
     */
    public double getX() {
        return center.getX();
    }

    /**
     * Gets the current y-coordinate of the animation
     * @return the current y-coordinate
     */
    public double getY() {
        return center.getY();
    }

    /**
     * Moves the animation by a given x value
     * @param dx the amount to move the animation by
     */
    public void moveX(double dx) {
        center.moveX(dx);
    }

    /**
     * Moves the animation by a given y value
     * @param dy the amount to move the animation by
     */
    public void moveY(double dy) {
        center.moveY(dy);
    }

    /**
     * Resets the animation to the beginning
     */
    public void reset() {
        time = 0;
    }

    @Override
    public void draw(Graphics graphic) {
        time ++;
        frames[(int) (time / speed) % frames.length].draw(graphic);
    }

}
