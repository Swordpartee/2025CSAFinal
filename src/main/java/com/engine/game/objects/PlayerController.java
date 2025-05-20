package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.game.collision.Collider;
import com.engine.game.collision.RectCollider;
import com.engine.network.Network;
import com.engine.network.headers.Header;
import com.engine.network.states.NetState;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.Animateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Color;
import com.engine.util.Functions;
import com.engine.util.Point;

public class PlayerController implements GameObject {
    private final Animateable frontWalk;
    private final Sprite frontStop;

    private final Animateable backWalk;
    private final Sprite backStop;

    private final Animateable leftWalk;
    private final Sprite leftStop;

    private final Animateable rightWalk;
    private final Sprite rightStop;

    // joes problem
    private final Animateable[] walkAnimations;
    private final Sprite[] stopSprites;

    private int spriteState;
    private final boolean isSelf;
    private final Point position;
    private final Point velocity;

    private final RectCollider collider;

    public PlayerController(Color color1, Color color2) {
        this.position = new Point(0, 0);
        this.velocity = new Point(0, 0);

        this.frontWalk = new Animateable(position, 25, Constants.PlayerConstants.PLAYER_FRONT_WALK_SPRITE_ONE, Constants.PlayerConstants.PLAYER_FRONT_WALK_SPRITE_TWO);
        this.frontStop = new Sprite(position,
                Constants.PlayerConstants.getPlayerFrontSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.backWalk = new Animateable(position, 25, Constants.PlayerConstants.PLAYER_BACK_WALK_SPRITE_ONE, Constants.PlayerConstants.PLAYER_BACK_WALK_SPRITE_TWO);
        this.backStop = new Sprite(position,
                Constants.PlayerConstants.getPlayerBackSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.leftWalk = new Animateable(position, 25, Constants.PlayerConstants.PLAYER_LEFT_SPRITE, Constants.PlayerConstants.PLAYER_LEFT_WALK_SPRITE);
        this.leftStop = new Sprite(position,
                Constants.PlayerConstants.getPlayerLeftSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.rightWalk = new Animateable(position, 25, Constants.PlayerConstants.PLAYER_RIGHT_SPRITE, Constants.PlayerConstants.PLAYER_RIGHT_WALK_SPRITE);
        this.rightStop = new Sprite(position,
                Constants.PlayerConstants.getPlayerRightSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
           

        this.collider = new RectCollider(position, Constants.PlayerConstants.PLAYER_WIDTH,
                Constants.PlayerConstants.PLAYER_HEIGHT);
        
        // joes problem
        this.stopSprites = new Sprite[] {
                frontStop,
                backStop,
                leftStop,
                rightStop
        };

        this.walkAnimations = new Animateable[] {
                frontWalk,
                backWalk,
                leftWalk,
                rightWalk
        };

        this.spriteState = 0;
        this.isSelf = true; // Assuming this is the local player
    }
    
    public PlayerController() {
        this(Color.WHITE, Color.BLACK);
    }

    public PlayerController(Color color) {
        this(color, Color.BLACK);
    }

    @Override
    public void update() {
        if (!isSelf) {
            return;
        }

        if (RenderListener.isKeyPressed(EventCode.SPACE)) {
            NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager, new Projectile(position.getX(), position.getY(), 10, true));
            Renderer.addGameObjects(projectile.getValue());
            projectile.getValue().getVelocity().setX(1);
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
        if (velocity.getX() < -0.1 && RenderListener.isKeyPressed(EventCode.A)) {
            leftWalk.draw(g);
        } else 
        if (velocity.getX() > 0.1 && RenderListener.isKeyPressed(EventCode.D)) {
            rightWalk.draw(g);
        } else 
        if (velocity.getY() < -0.1 && RenderListener.isKeyPressed(EventCode.W)) {
            backWalk.draw(g);
        } else 
        if (velocity.getY() > 0.1 && RenderListener.isKeyPressed(EventCode.S)) {
            frontWalk.draw(g);
        } else {
            frontStop.draw(g);
        }
    }

    @Override
    public boolean colliding(Collider other) {
        return collider.colliding(other);
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
