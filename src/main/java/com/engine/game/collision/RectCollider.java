package com.engine.game.collision;

import java.awt.Color;

import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.Rect;

public class RectCollider extends Rect implements Collider {

    private final Drawable debugDrawable;

    public RectCollider(PointConfig center, double width, double height) {
        super(center, width, height);
        this.debugDrawable = new DrawerRect(center, width, height, false, Color.RED); // Initialize with no debug drawable
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
                // Convert from center-based to edge-based calculation
                double thisLeft = this.getX() - this.getWidth() / 2;
                double thisRight = this.getX() + this.getWidth() / 2;
                double thisTop = this.getY() - this.getHeight() / 2;
                double thisBottom = this.getY() + this.getHeight() / 2;

                double otherLeft = rect.getX() - rect.getWidth() / 2;
                double otherRight = rect.getX() + rect.getWidth() / 2;
                double otherTop = rect.getY() - rect.getHeight() / 2;
                double otherBottom = rect.getY() + rect.getHeight() / 2;

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
    public boolean colliding(Point point) {
        // Convert from center-based to edge-based calculation
        double thisLeft = this.getX() - this.getWidth() / 2;
        double thisRight = this.getX() + this.getWidth() / 2;
        double thisTop = this.getY() - this.getHeight() / 2;
        double thisBottom = this.getY() + this.getHeight() / 2;

        return point.getX() > thisLeft && point.getX() < thisRight &&
               point.getY() > thisTop && point.getY() < thisBottom;
    }

    @Override
    public ColliderType getType() {
        return ColliderType.RECT;
    }
}
