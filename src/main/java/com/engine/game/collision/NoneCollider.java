package com.engine.game.collision;

import com.engine.util.Point;

public class NoneCollider implements Collidable {
    @Override
    public boolean colliding(Collidable other) {
        return false;
    }

    @Override
    public boolean colliding(double x, double y) {
        return false;
    }

    @Override
    public boolean colliding(Point p) {
        return false;
    }

    @Override
    public ColliderType getType() {
        return ColliderType.NONE;
    }
  

}
