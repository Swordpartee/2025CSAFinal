package com.engine.game.collision;

public interface Collidable {
    public boolean colliding(Collidable other);

    public boolean colliding(double x, double y);
    
    public ColliderType getType();

    enum ColliderType {
        CIRCLE,
        RECT,
    }
}
