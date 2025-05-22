package com.engine.util;

public class Circle extends PointController {
    private double radius;

    /**
     * Constructs a new Circle.
     * 
     * @param x the x-coordinate of the center
     * @param y the y-coordinate of the center
     * @param radius the radius of the circle
     */
    public Circle(double x, double y, double radius) {
        super(x, y);
        this.radius = radius;
    }

    public Circle(Point center, double radius) {
        super(center);
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
        
}
