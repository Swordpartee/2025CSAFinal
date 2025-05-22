package com.engine.game.collision;

public interface Collider {
    default public boolean colliding(Collider other) {
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
