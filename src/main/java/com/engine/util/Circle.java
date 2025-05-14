package com.engine.util;

public class Circle {
    private Point center;
    private double radius;

    /**
     * Constructs a new Circle.
     * 
     * @param x the x-coordinate of the center
     * @param y the y-coordinate of the center
     * @param radius the radius of the circle
     */
    public Circle(double x, double y, double radius) {
        this.center = new Point(x, y);
        this.radius = radius;
    }

    public Circle(Point center, double radius) {
        this.radius = radius;
    }

    public double getX() {
        return center.getX();
    }
    
    public void setX(double x) {
        center.setX(x);
    }
    
    public double getY() {
        return center.getY();
    }

    public void setY(double y) {
        center.setY(y);
    }
    
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
        
}
