package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;

public class Sprite implements Drawable {
    private final Point center;

    private final Image image;

    public Sprite(Point center, Image image) {
        this.center = center;

        this.image = image;
    }

    public Sprite(double x, double y, Image image) {
        this.center = new Point(x, y);

        this.image = image;
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

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, center.getX(), center.getY());
    }
}
