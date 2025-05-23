package com.engine.game.collision;

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

    enum ColliderType {
        CIRCLE,
        RECT,
        OTHER
    }
}
