package com.engine.util;

public class Circle extends PointController {
    private double radius;

<<<<<<< HEAD
    // public Circle(double x, double y, double radius) {
    //     this(new PointConfig(x, y), radius);
    // }
=======
<<<<<<< HEAD
    // public Circle(double x, double y, double radius) {
    //     this(new PointConfig(x, y), radius);
    // }

    public Circle(PointConfig center, double radius) {
        super(center);
        this.radius = radius;
    }

=======
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
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6

    public Circle(PointConfig center, double radius) {
        super(center);
        this.radius = radius;
    }

>>>>>>> origin/main
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
    }
        
}
