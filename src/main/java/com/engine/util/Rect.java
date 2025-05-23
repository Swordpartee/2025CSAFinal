package com.engine.util;

public class Rect extends PointController {
    private double width;
    private double height;

    public Rect(PointConfig pointConfig, double width, double height) {
        super(pointConfig);

        this.width = width;
        this.height = height;
    }
    
    // /**
    //  * Creates a rect with the given center coordinates and dimensions
    //  */
    // public Rect(double centerX, double centerY, double width, double height) {
    //     this(new PointConfig(centerX, centerY), width, height);
    // }

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
        return new Point(getX() - width/2, getY() - height/2);
    }
    
    /**
     * Gets the four corners of the rectangle
     */
    public Point[] getPoints() {
        double halfWidth = width / 2;
        double halfHeight = height / 2;
        
        return new Point[] {
            new Point(getX() - halfWidth, getY() - halfHeight), // top-left
            new Point(getX() + halfWidth, getY() - halfHeight), // top-right
            new Point(getX() - halfWidth, getY() + halfHeight), // bottom-left
            new Point(getX() + halfWidth, getY() + halfHeight)  // bottom-right
        };
    }
}
