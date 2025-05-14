package com.engine.util;

public class Rect {
    private final Point center;
    private double width;
    private double height;


    /**
     * Creates a rect with the given center coordinates and dimensions
     */
    public Rect(double centerX, double centerY, double width, double height) {
        this.center = new Point(centerX, centerY);
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a rect with the given center point and dimensions
     */
    public Rect(Point center, double width, double height) {
        this.center = center;
        this.width = width;
        this.height = height;
    }

    public Rect() {
        this.center = new Point(0, 0);
        this.width = 0;
        this.height = 0;
    }

    public double getX() {
        return center.getX();
    }

    public double getY() {
        return center.getY();
    }
    
    public void setX(double x) {
        center.setX(x);
    }

    public void setY(double y) {
        center.setY(y);
    }

    public void moveX(double deltaX) {
        center.moveX(deltaX);
    }

    public void moveY(double deltaY) {
        center.moveY(deltaY);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
    
    /**
     * Gets the top-left corner point
     */
    public Point getTopLeft() {
        return new Point(center.getX() - width/2, center.getY() - height/2);
    }
    
    /**
     * Gets the four corners of the rectangle
     */
    public Point[] getPoints() {
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        
        return new Point[] {
            new Point(center.getX() - halfWidth, center.getY() - halfHeight), // top-left
            new Point(center.getX() + halfWidth, center.getY() - halfHeight), // top-right
            new Point(center.getX() - halfWidth, center.getY() + halfHeight), // bottom-left
            new Point(center.getX() + halfWidth, center.getY() + halfHeight)  // bottom-right
        };
    }
}
