package com.engine.game.objects;

import java.awt.Graphics;

import com.engine.Constants;
import com.engine.game.collision.Collidable;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Functions;
import com.engine.util.Point;

public class PlayerController implements GameObject {
    private final Sprite frontSprite;
    private final Sprite backSprite;
    private final Sprite leftSprite;
    private final Sprite rightSprite;
    private final Point position;
    private final Point velocity;
    private final RectCollider collider;

    public PlayerController() {
        this.position = new Point(0, 0);
        this.velocity = new Point(0, 0);

        this.frontSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_FRONT_SPRITE,
                Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        this.backSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_BACK_SPRITE,
                Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        this.leftSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_LEFT_SPRITE,
                Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        this.rightSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_RIGHT_SPRITE,
                Constants.PlayerConstants.PLAYER_SPRITE_SCALE);    

        this.collider = new RectCollider(position, Constants.PlayerConstants.PLAYER_WIDTH, Constants.PlayerConstants.PLAYER_HEIGHT);
    }

    @Override
    public void update() {
        // Set velocity based on input
        if (RenderListener.isKeyPressed(EventCode.W)) {
            velocity.moveY(-Constants.PlayerConstants.PLAYER_ACCELERATION);
        }
        if (RenderListener.isKeyPressed(EventCode.S)) {
            velocity.moveY(Constants.PlayerConstants.PLAYER_ACCELERATION);
        }
        if (RenderListener.isKeyPressed(EventCode.A)) {
            velocity.moveX(-Constants.PlayerConstants.PLAYER_ACCELERATION);
        }
        if (RenderListener.isKeyPressed(EventCode.D)) {
            velocity.moveX(Constants.PlayerConstants.PLAYER_ACCELERATION);
        }

        velocity.setX(Functions.clamp(velocity.getX() * Constants.PlayerConstants.PLAYER_FRICTION, -Constants.PlayerConstants.PLAYER_MAX_SPEED, Constants.PlayerConstants.PLAYER_MAX_SPEED));
        velocity.setY(Functions.clamp(velocity.getY() * Constants.PlayerConstants.PLAYER_FRICTION, -Constants.PlayerConstants.PLAYER_MAX_SPEED, Constants.PlayerConstants.PLAYER_MAX_SPEED));

        if (velocity.getX() != 0) {
            position.moveX(velocity.getX());
            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveX(-velocity.getX());
                velocity.setX(0);
            }
        }
        
        if (velocity.getY() != 0) {
            position.moveY(velocity.getY());
            if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
                position.moveY(-velocity.getY());
                velocity.setY(0);
            }
        }

        // Ensure player stays within screen bounds (considering position is the center)
        position.setX(Functions.clamp(position.getX(), Constants.PlayerConstants.PLAYER_WIDTH / 2, Renderer.getWidth() - Constants.PlayerConstants.PLAYER_WIDTH / 2));
        position.setY(Functions.clamp(position.getY(), Constants.PlayerConstants.PLAYER_HEIGHT / 2, Renderer.getHeight() - Constants.PlayerConstants.PLAYER_HEIGHT / 2));
    }

    @Override
    public void draw(Graphics g) {
        if (velocity.getX() < -0.1 && RenderListener.isKeyPressed(EventCode.A)) {
            leftSprite.draw(g);
        } else 
        if (velocity.getX() > 0.1 && RenderListener.isKeyPressed(EventCode.D)) {
            rightSprite.draw(g);
        } else 
        if (velocity.getY() < -0.1 && RenderListener.isKeyPressed(EventCode.W)) {
            backSprite.draw(g);
        } else {
            frontSprite.draw(g);
        }
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
        return ColliderType.OTHER;
    }
}
