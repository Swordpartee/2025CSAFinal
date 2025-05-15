package com.engine.game.collision;

import com.engine.util.Point;
import com.engine.util.Rect;

public class RectCollider extends Rect implements Collidable {

    public RectCollider() {
        super();
    }
    /**
     * Creates a new rectangle collider
     * @param x position of the top left corner of the rectangle
     * @param y position of the top left corner of the rectangle
     * @param width of the rectangle
     * @param height of the rectangle
     */
    public RectCollider(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Creates a new rectangle collider
     * @param rect the rectangle to use as the collider
     */
    public RectCollider(Rect rect) {
        super(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public RectCollider(Point center, double width, double height) {
        super(center, width, height);
    }


    @Override
    public boolean colliding(Collidable other) {
        if (this == other) {
            return false;
        }
        ColliderType otherType = other.getType();
        switch (otherType) {
            case RECT -> {
                RectCollider rect = (RectCollider) other;
                // Convert from center-based to edge-based calculation
                double thisLeft = this.getX() - this.getWidth()/2;
                double thisRight = this.getX() + this.getWidth()/2;
                double thisTop = this.getY() - this.getHeight()/2;
                double thisBottom = this.getY() + this.getHeight()/2;
                
                double otherLeft = rect.getX() - rect.getWidth()/2;
                double otherRight = rect.getX() + rect.getWidth()/2;
                double otherTop = rect.getY() - rect.getHeight()/2;
                double otherBottom = rect.getY() + rect.getHeight()/2;
                
                return thisLeft < otherRight && 
                       thisRight > otherLeft && 
                       thisTop < otherBottom && 
                       thisBottom > otherTop;
            }
            case CIRCLE -> {
                CircleCollider circle = (CircleCollider) other;
                double closestX = Math.max(this.getX(), Math.min(circle.getX(), this.getX() + this.getWidth()));
                double closestY = Math.max(this.getY(), Math.min(circle.getY(), this.getY() + this.getHeight()));
                double distanceSquared = Math.pow(circle.getX() - closestX, 2) + Math.pow(circle.getY() - closestY, 2);
                return distanceSquared <= Math.pow(circle.getRadius(), 2);
            }
            case OTHER -> {
                return other.colliding(this);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean colliding(double x, double y) {
        return x >= this.getX() && x <= this.getX() + this.getWidth() &&
               y >= this.getY() && y <= this.getY() + this.getHeight();
    }

    @Override
    public ColliderType getType() {
        return ColliderType.RECT;
    }
}
