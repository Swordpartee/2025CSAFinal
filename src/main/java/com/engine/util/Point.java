package com.engine.util;

public class Point {
    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void moveX(double dx) {
        this.x += dx;
    }

    public void moveY(double dy) {
        this.y += dy;
    }

    public void moveXY(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public Point copy() {
        return new Point(this.x, this.y);
    }
}
