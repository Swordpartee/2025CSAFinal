package com.engine.game;

import java.awt.Graphics;

import com.engine.game.collision.Collidable;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Functions;
import com.engine.util.Point;
import com.engine.util.Updateable;

public class PlayerController implements Updateable, Drawable, Collidable {
    private final Sprite sprite;
    private final Point position;
    private final RectCollider collider;

    public PlayerController(String spritePath) {
        this.position = new Point(0, 0);

        this.sprite = new Sprite(position, spritePath, 7);

        this.collider = new RectCollider(position, 7 * 16, 7 * 16);
    }

    @Override
    public void update() {
        if (RenderListener.isKeyPressed(EventCode.W)) {
            position.moveY(-2);

            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveY(2);
            }
        }
        if (RenderListener.isKeyPressed(EventCode.S)) {
            position.moveY(2);

            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveY(-2);
            }
        }
        if (RenderListener.isKeyPressed(EventCode.A)) {
            position.moveX(-2);
            
            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveX(2);
            }
        }
        if (RenderListener.isKeyPressed(EventCode.D)) {
            position.moveX(2);
            
            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveX(-2);
            }
        }

    }

    @Override
    public void draw(Graphics g) {
        sprite.draw(g);
    }

    @Override
    public boolean colliding(Collidable other) {
        return collider.colliding(other);
    }

    @Override
    public boolean colliding(double x, double y) {
        return collider.colliding(x, y);
    }

    @Override
    public ColliderType getType() {
        return collider.getType();
    }
}
