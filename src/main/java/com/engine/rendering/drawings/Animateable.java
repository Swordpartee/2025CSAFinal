package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Functions;
import com.engine.util.Image;
import com.engine.util.Point;

public class Animateable implements Drawable {
    private final Point center;

    private final double speed;
    private final Sprite[] frames;

    private double beginningTime;

    public Animateable(double x, double y, double speed, Image... Frames) {
        this.center = new Point(x, y);
        this.speed = speed;

        beginningTime = Functions.getTime();

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
        beginningTime = Functions.getTime();
    }

    @Override
    public void draw(Graphics graphic) {
        double time = Functions.getTime() - beginningTime;
        int index = ((int) Math.floor(time / speed)) % frames.length;
        frames[index].draw(graphic);
    }

}
