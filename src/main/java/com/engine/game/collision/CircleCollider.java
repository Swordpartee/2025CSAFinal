package com.engine.game.collision;

import java.awt.Color;

import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.util.Circle;
import com.engine.util.Point;
import com.engine.util.PointConfig;

public class CircleCollider extends Circle implements Collider {
    
    private final Drawable debugDrawable;

    /**
     * Creates a new circle collider
     * @param circle the circle to use as the collider
     */
    public CircleCollider(PointConfig center, double radius) {
        super(center, radius);
        this.debugDrawable = new DrawerCircle(center, radius, false, Color.RED);
    }

    @Override
    public Drawable getDebugDrawable() {
        return debugDrawable;
    }

    @Override
    public boolean colliding(Collider other) {
        if (this == other) {
            return false;
        }
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
                double distanceSquared = Math.pow(this.getX() - circle.getX(), 2)
                        + Math.pow(this.getY() - circle.getY(), 2);
                double radiusSum = this.getRadius() + circle.getRadius();
                return distanceSquared <= Math.pow(radiusSum, 2);
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
    public boolean colliding(Point point) {
        double distanceSquared = Math.pow(point.getX() - this.getX(), 2)
                + Math.pow(point.getY() - this.getY(), 2);
        return distanceSquared <= Math.pow(this.getRadius(), 2);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.CIRCLE;
    }

}
