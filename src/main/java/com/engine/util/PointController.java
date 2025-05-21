package com.engine.util;

public class PointController {
    private Point position;
    
    public PointController() {
        this.position = new Point(0, 0);
    }

    public PointController(double x, double y) {
        this.position = new Point(x, y);
    }

    public PointController(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    public void move(double dx, double dy) {
        this.position.setX(this.position.getX() + dx);
        this.position.setY(this.position.getY() + dy);
    }

    public void move(Point delta) {
        this.position.setX(this.position.getX() + delta.getX());
        this.position.setY(this.position.getY() + delta.getY());
    }

    public void setX(double x) {
        this.position.setX(x);
    }

    public void setY(double y) {
        this.position.setY(y);
    }

    public double getX() {
        return this.position.getX();
    }

    public double getY() {
        return this.position.getY();
    }

    public void moveX(double dx) {
        this.position.setX(this.position.getX() + dx);
    }

    public void moveY(double dy) {
        this.position.setY(this.position.getY() + dy);
    }
}
