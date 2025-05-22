package com.engine.util;

public class PointConfig {
    private final Point position;
    private final Point offset;

    public PointConfig() {
        this(new Point(0, 0), new Point(0, 0));
    }

    public PointConfig(double x, double y) {
        this(new Point(x, y), new Point(0, 0));
    }

    public PointConfig(Point position, double x, double y) {
        this(position, new Point(x, y));
    }

    public PointConfig(Point position) {
        this(position, new Point(0, 0));
    }

    public PointConfig(Point position, Point offset) {
        this.position = position;
        this.offset = offset;
    }

    public PointConfig setOffset(int x, int y) {
        this.offset.setX(x);
        this.offset.setY(y);
        return this;
    }

    public Point getOffset() {
        return offset;
    }

    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public Point getPosition() {
        return position;
    }

    public double getX() {
        return position.getX() + offset.getX();
    }

    public double getY() {
        return position.getY() + offset.getY();
    }

    public void moveX(double x) {
        position.moveX(x);
    }

    public void moveY(double y) {
        position.moveY(y);
    }

    public PointConfig copy() {
        return new PointConfig(new Point(position.getX(), position.getY()), new Point(offset.getX(), offset.getY()));
    }

}
