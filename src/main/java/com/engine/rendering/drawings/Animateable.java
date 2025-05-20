package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;

public class Animateable implements Drawable {
    private final Point center;

    private final double speed;
    private final Sprite[] frames;

    private double time;

    public Animateable(double x, double y, double speed, Image... Frames) {
        this.center = new Point(x, y);
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(center, Frames[i]);
        }
    }

    public Animateable(Point center, double speed, Image... Frames) {
        this.center = center;
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(center, Frames[i]);
        }
    }

    public void setX(double x) {
        center.setX(x);
    }

    public void setY(double y) {
        center.setY(y);
    }

    public double getX() {
        return center.getX();
    }

    public double getY() {
        return center.getY();
    }

    public void moveX(double x) {
        center.moveX(x);
    }

    public void moveY(double y) {
        center.moveY(y);
    }

    public void reset() {
        time = 0;
    }

    @Override
    public void draw(Graphics graphic) {
        time ++;
        frames[(int) (time / speed) % frames.length].draw(graphic);
    }

}
