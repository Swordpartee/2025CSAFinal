package com.engine.util;

public class PointController {
    private final PointConfig pointConfig;

    public PointController(PointConfig pointConfig) {
        this.pointConfig = pointConfig;
    }

    public void setOffset(int x, int y) {
        pointConfig.setOffset(x, y);
    }

    public void setPosition(double x, double y) {
        pointConfig.setPosition(x, y);
    }

    public Point getPosition() {
        return pointConfig.getPosition();
    }

    public Point getOffset() {
        return pointConfig.getOffset();
    }

    public PointConfig getPoint() {
        return pointConfig;
    }

    public void setX(double x) {
        pointConfig.getPosition().setX(x);
    }

    public void setY(double y) {
        pointConfig.getPosition().setY(y);
    }

    public double getX() {
        return pointConfig.getX();
    }

    public double getY() {
        return pointConfig.getY();
    }

    public void moveX(double x) {
        pointConfig.moveX(x);
    }

    public void moveY(double y) {
        pointConfig.moveY(y);
    }

    public void scaleX(double x) {
        pointConfig.scaleX(x);
    }

    public void scaleY(double y) {
        pointConfig.scaleY(y);
    }
}
