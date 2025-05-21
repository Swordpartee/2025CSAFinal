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
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Color;
import com.engine.util.Functions;
import com.engine.util.Point;
import com.engine.util.PointController;

public class PlayerController implements GameObject {
    public enum SpriteState {
        FRONT_STOP,
        BACK_STOP,
        LEFT_STOP,
        RIGHT_STOP,
        FRONT_WALK,
        BACK_WALK,
        LEFT_WALK,
        RIGHT_WALK;

        public static SpriteState from(int i) {
            return SpriteState.values()[i];
        }

        public int value() {
            return this.ordinal();
        }
    }

    private final Animateable frontWalk;
    private final Sprite frontStop;

    private final Animateable backWalk;
    private final Sprite backStop;

    private final Animateable leftWalk;
    private final Sprite leftStop;

    private final Animateable rightWalk;
    private final Sprite rightStop;

    private final Drawable[] spriteStates;
    private SpriteState spriteState;

    private final Point position;
    private final Point velocity;

    private final RectCollider collider;

    public PlayerController(Color color1, Color color2) {
        super(0, 0);
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
           

        this.collider = new RectCollider(getPosition(), Constants.PlayerConstants.PLAYER_WIDTH,
                Constants.PlayerConstants.PLAYER_HEIGHT);
        
        // System.out.println("PlayerController: " + this.collider.getWidth() + " " + this.collider.getHeight());

        // joes problem
        this.spriteStates = new Drawable[] {
                this.frontStop,
                this.backStop,
                this.leftStop,
                this.rightStop,
                this.frontWalk,
                this.backWalk,
                this.leftWalk,
                this.rightWalk
        };

        this.spriteState = SpriteState.FRONT_STOP;
    }
    
    public PlayerController() {
        this(Color.WHITE, Color.BLACK);
    }

    public PlayerController(Color color) {
        this(color, Color.BLACK);
    }

    @Override
    public void update() {
        if (RenderListener.isKeyPressed(EventCode.SPACE)) {
            NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager, new Projectile(getX(), getY(), 10, true));
            Renderer.addGameObjects(projectile.getValue());
            projectile.getValue().getVelocity().setX(1);
            try {
                projectile.sendSelf();
            } catch (Exception e) {}
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

        moveX(velocity.getX());
        if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
            moveX(-velocity.getX());
            velocity.setX(0);
        }

        moveY(velocity.getY());
        if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
            moveY(-velocity.getY());
            velocity.setY(0);
        }

        if (velocity.getX() < -0.1 && RenderListener.isKeyPressed(EventCode.A)) {
            spriteState = SpriteState.LEFT_WALK;
        } else 
        if (velocity.getX() > 0.1 && RenderListener.isKeyPressed(EventCode.D)) {
            spriteState = SpriteState.RIGHT_WALK;
        } else 
        if (velocity.getY() < -0.1 && RenderListener.isKeyPressed(EventCode.W)) {
            spriteState = SpriteState.BACK_WALK;
        } else 
        if (velocity.getY() > 0.1 && RenderListener.isKeyPressed(EventCode.S)) {
            spriteState = SpriteState.FRONT_WALK;
        } else {
            spriteState = SpriteState.FRONT_STOP;
        }

        // Ensure player stays within screen bounds (considering position is the center)
        setX(Functions.clamp(getX(), Constants.PlayerConstants.PLAYER_WIDTH / 2, Renderer.getWidth() - Constants.PlayerConstants.PLAYER_WIDTH / 2));
        setY(Functions.clamp(getY(), Constants.PlayerConstants.PLAYER_HEIGHT / 2, Renderer.getHeight() - Constants.PlayerConstants.PLAYER_HEIGHT / 2));
    }

    @Override
    public void draw(Graphics g) {
        spriteStates[spriteState.ordinal()].draw(g);
    }

    @Override
    public boolean colliding(Collider other) {
        if (other == null) {
            return false;
        }

        return collider.colliding(other);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.OTHER;
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        setX(dataSegments.readInt());
        setY(dataSegments.readInt());
        this.velocity.setX(dataSegments.readInt());
        this.velocity.setY(dataSegments.readInt());
        this.spriteState = SpriteState.from(dataSegments.read());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
        dataSegments.writeInt((int) velocity.getX());
        dataSegments.writeInt((int) velocity.getY());
        dataSegments.write(spriteState.value());
    }

    @Override
    public String toString() {
        return "PlayerController{" +
                "position=" + getPosition() +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    public void onNetworkCreate() throws Exception {
        Renderer.addDrawables(this);
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        Renderer.removeDrawables(this);
    }
    
}
