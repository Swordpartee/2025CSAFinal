package com.engine.game.collision;

public class NoneCollider implements Collider {
    @Override
    public boolean colliding(Collider other) {
        return false;
    }

    @Override
    public ColliderType getType() {
        return ColliderType.NONE;
    }
  

}
