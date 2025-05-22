package com.engine.util;

public class Circle extends PointController {
    private double radius;

    // public Circle(double x, double y, double radius) {
    //     this(new PointConfig(x, y), radius);
    // }

    public Circle(PointConfig center, double radius) {
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
