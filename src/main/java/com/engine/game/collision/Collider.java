package com.engine.game.collision;

import com.engine.rendering.drawings.Drawable;
import com.engine.util.Point;

public interface Collider {
    default public boolean colliding(Collider other) {
        return false;
    }

    default public boolean colliding(Point point) {
        return false;
    }

    default public ColliderType getType() {
        return ColliderType.OTHER;
    };

    default public Drawable getDebugDrawable() {
        return null;
    }

    enum ColliderType {
        CIRCLE,
        RECT,
        OTHER
    }
}
