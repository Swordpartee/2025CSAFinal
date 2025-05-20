package com.engine.game.collision;

public interface Collider {
    public boolean colliding(Collider other);
    
    public ColliderType getType();

    enum ColliderType {
        CIRCLE,
        RECT,
        OTHER, 
        NONE
    }
}
