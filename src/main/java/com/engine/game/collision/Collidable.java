package com.engine.game.collision;

import com.engine.util.Point;

public interface Collidable {
    /**
     * Checks if current collidable is colliding with another collidable object
     * @param other the other collidable object
     * @return true if colliding, false otherwise
     */
    public boolean colliding(Collidable other);

    /**
     * Checks if current collidable is colliding with a point
     * @param x-coordinate of the point
     * @param y-coordinate of the point
     * @return true if colliding, false otherwise
     */
    public boolean colliding(double x, double y);

    /**
     * Checks if current collidable is colliding with a point
     * @param p the point of collision
     * @return true if colliding, false otherwise
     */
    public boolean colliding(Point p);
    
    /**
     * Gets the type of collidable
     * @return the type of collidable
     */
    public ColliderType getType();

    enum ColliderType {
        CIRCLE,
        RECT,
        OTHER, 
        NONE
    }
}
