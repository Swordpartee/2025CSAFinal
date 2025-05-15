package com.engine.util;

public class Point {
    private double x, y;

    /**
     * Creates a new point.
     * Used to define a position on the canvas.
     * Should be used as a reference point for all drawable objects.
     * @param x
     * @param y
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new point (default is (0, 0)).
     * Used to define a position on the canvas.
     * Should be used as a reference point for all drawable objects.
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Gets the x coordinate of the position.
     * @return x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the position.
     * @return y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets a new x coordinate for the position.
     * @param newX the new x coordinate
     */
    public void setX(double newX) {
        x = newX;
    }

    /**
     * Sets a new y coordinate for the position.
     * @param newY the new y coordinate
     */
    public void setY(double newY) {
        y = newY;
    }

    /**
     * Sets a new coordinate for the position.
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     */
    public void setPos(double newX, double newY) {
        x = newX;
        y = newY;
    }

    /**
     * Moves the x coordinate by a given amount.
     * @param dx change in x
     */
    public void moveX(double dx) {
        x += dx;
    }

    /**
     * Moves the y coordinate by a given amount.
     * @param dy change in y
     */
    public void moveY(double dy) {
        y += dy;
    }

    /**
     * Increments both x and y coordinates by a given amount.
     * @param dx change in x
     * @param dy change in y
     */
    public void moveXY(double dx, double dy) {
        x += dx;
        y += dy;
    }

    /**
     * Creates a copy of the point based on current coordinates.
     * @return a new Point object with the same coordinates
     */
    public Point copy() {
        return new Point(this.x, this.y);
    }

    /**
     * Returns a string representation of the point.
     * @return a string in the format "(x, y)"
     */
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
