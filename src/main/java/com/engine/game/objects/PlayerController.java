package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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
    private final Sprite[] sprites;
    private int spriteState;
    private final boolean isSelf;
    private final Point position;
    private final Point velocity;
    private final RectCollider collider;

    /**
     * Constructor for PlayerController.
     * @param isSelf Indicates if this player is the local player.
     */
    public PlayerController(boolean isSelf) {
        this.position = new Point(0, 0);
        this.velocity = new Point(0, 0);

        this.frontSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_FRONT_SPRITE);
        this.backSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_BACK_SPRITE);
        this.leftSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_LEFT_SPRITE);
        this.rightSprite = new Sprite(position, Constants.PlayerConstants.PLAYER_RIGHT_SPRITE);   
        
        this.sprites = new Sprite[] { this.frontSprite, this.backSprite, this.leftSprite, this.rightSprite };
        this.spriteState = 0;

        this.collider = new RectCollider(position, Constants.PlayerConstants.PLAYER_WIDTH, Constants.PlayerConstants.PLAYER_HEIGHT);

        this.isSelf = isSelf;
    }

    /**
     * Constructor for PlayerController with default isSelf value (false).
     */
    public PlayerController() {
        this(false);
    }

    @Override
    public void update() {
        if (!isSelf) {
            return;
        }

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

        position.moveX(velocity.getX());
        if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
            position.moveX(-velocity.getX());
            velocity.setX(0);
        }
        
        position.moveY(velocity.getY());
        if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
            position.moveY(-velocity.getY());
            velocity.setY(0);
        }

        // Ensure player stays within screen bounds (considering position is the center)
        position.setX(Functions.clamp(position.getX(), Constants.PlayerConstants.PLAYER_WIDTH / 2, Renderer.getWidth() - Constants.PlayerConstants.PLAYER_WIDTH / 2));
        position.setY(Functions.clamp(position.getY(), Constants.PlayerConstants.PLAYER_HEIGHT / 2, Renderer.getHeight() - Constants.PlayerConstants.PLAYER_HEIGHT / 2));
    }

    @Override
    public void draw(Graphics g) {
        if (!isSelf) {
            sprites[spriteState].draw(g);
            return;
        }

        if (velocity.getX() < -0.1 && RenderListener.isKeyPressed(EventCode.A)) {
            spriteState = 2;
        } else 
        if (velocity.getX() > 0.1 && RenderListener.isKeyPressed(EventCode.D)) {
            spriteState = 3;
        } else 
        if (velocity.getY() < -0.1 && RenderListener.isKeyPressed(EventCode.W)) {
            spriteState = 1;
        } else { // I changed it back for you Nate :)
            spriteState = 0;
        }

        sprites[spriteState].draw(g);
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

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        this.position.setX(dataSegments.readInt());
        this.position.setY(dataSegments.readInt());
        this.velocity.setX(dataSegments.readInt());
        this.velocity.setY(dataSegments.readInt());
        this.spriteState = dataSegments.readInt();
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) position.getX());
        dataSegments.writeInt((int) position.getY());
        dataSegments.writeInt((int) velocity.getX());
        dataSegments.writeInt((int) velocity.getY());
        dataSegments.writeInt(spriteState);
    }

    public String toString() {
        return "PlayerController{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }
    
}
