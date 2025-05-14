package com.engine.game.collision;

import com.engine.util.Circle;
import com.engine.util.Point;

public class CircleCollider extends Circle implements Collidable {
    /**
     * Creates a new circle collider
     * @param x position of the center of the circle
     * @param y position of the center of the circle
     * @param radius of the circle
     */
    public CircleCollider(double x, double y, double radius) {
        super(x, y, radius);
    }

    /**
     * Creates a new circle collider
     * @param circle the circle to use as the collider
     */
    public CircleCollider(Point center, double radius) {
        super(center, radius);
    }

    @Override
    public boolean colliding(Collidable other) {
        ColliderType otherType = other.getType();
        switch (otherType) {
            case RECT -> {
                RectCollider rect = (RectCollider) other;
                double closestX = Math.max(rect.getX(), Math.min(this.getX(), rect.getX() + rect.getWidth()));
                double closestY = Math.max(rect.getY(), Math.min(this.getY(), rect.getY() + rect.getHeight()));
                double distanceSquared = Math.pow(this.getX() - closestX, 2) + Math.pow(this.getY() - closestY, 2);
                return distanceSquared <= Math.pow(this.getRadius(), 2);
            }
            case CIRCLE -> {
                CircleCollider circle = (CircleCollider) other;
                double distanceSquared = Math.pow(this.getX() - circle.getX(), 2) + Math.pow(this.getY() - circle.getY(), 2);
                double radiusSum = this.getRadius() + circle.getRadius();
                return distanceSquared <= Math.pow(radiusSum, 2);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean colliding(double x, double y) {
        return Math.pow(x - this.getX(), 2) + Math.pow(y - this.getY(), 2) <= Math.pow(this.getRadius(), 2);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.CIRCLE;
    }

}
