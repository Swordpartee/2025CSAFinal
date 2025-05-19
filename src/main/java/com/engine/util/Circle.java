package com.engine.util;

public class Circle {
    private Point center;
    private double radius;

    /**
     * Creates a new Circle.
     * @param x the x-coordinate of the center of the circle
     * @param y the y-coordinate of the center of the circle
     * @param radius the radius of the circle
     */
    public Circle(double x, double y, double radius) {
        this.center = new Point(x, y);
        this.radius = radius;
    }

    /**
     * Creates a new Circle.
     * @param center the x y position of the center of the circle
     * @param radius the radius of the circle
     */
    public Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    /**
     * Gets the x-coordinate of the center of the circle
     * @return the x-coordinate of the center of the circle
     */
    public double getX() {
        return center.getX();
    }
    
    /**
     * Sets the x-coordinate of the center of the circle
     * @param newX the new x-coordinate
     */
    public void setX(double newX) {
        center.setX(newX);
    }

    /**
     * Moves the circle by a given amount in the x direction
     * @param dx the amount to move the circle by
     */
    public void moveX(double dx) {
        center.moveX(dx);
    }
    
    /**
     * Gets the y-coordinate of the center of the circle
     * @return the y-coordinate of the center of the circle
     */
    public double getY() {
        return center.getY();
    }

    /**
     * Sets the y-coordinate of the center of the circle
     * @param newY the new y-coordinate
     */
    public void setY(double newY) {
        center.setY(newY);
    }

    /**
     * Moves the circle by a given amount in the y direction
     * @param dy the amount to move the circle by
     */
    public void moveY(double dy) {
        center.moveX(dy);
    }
    
    /**
     * Gets the radius of the circle
     * @return the radius of the circle
     */
    public double getRadius() {
        return radius;
    }
    
    /**
     * Sets the radius of the circle
     * @param radius the new radius of the circle
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }
        
}
