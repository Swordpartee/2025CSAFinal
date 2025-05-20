package com.engine.game.objects;

import com.engine.game.collision.Collider;
import com.engine.rendering.drawings.Drawable;
import com.engine.util.Updateable;

public interface GameObject extends Updateable, Drawable, Collider {
    // GameObject is an interface that combines the functionalities of Drawable, Updateable, and Collidable.
    // It represents a game object that can be drawn, updated, and checked for collisions.
    // No additional methods are needed here as the interface already extends the necessary interfaces.
}
