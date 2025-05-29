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
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Color;
import com.engine.util.Functions;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class PlayerController extends PointController implements GameObject {
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

    private String lastButton = "";

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

    private final InstanceAnimateable leftSwing;
    private final InstanceAnimateable rightSwing;
    private final InstanceAnimateable upSwing;
    private final InstanceAnimateable downSwing;

    private final Point velocity;

    private final RectCollider collider;

    public PlayerController(Color color1, Color color2) {
        super(new PointConfig(0, 0));
        this.velocity = new Point(0, 0);

        this.rightSwing = Constants.WeaponConstants.getSwing(105, 0, 0);

        this.leftSwing = Constants.WeaponConstants.getSwing(-105, 0, -180);

        this.upSwing = Constants.WeaponConstants.getSwing(0, -105, -90);

        this.downSwing = Constants.WeaponConstants.getSwing(0, 105, 90);

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.E, () -> {
            if (velocity.getX() < -0.1 || lastButton.equals("A")) {
                leftSwing.run();
            } else if (velocity.getX() > 0.1 || lastButton.equals("D")) {
                rightSwing.run();
            } else if (velocity.getY() < -0.1 || lastButton.equals("W")) {
                upSwing.run();
            } else if (velocity.getY() > 0.1 || lastButton.equals("S")) {
                downSwing.run();
            }
        });

        this.frontWalk = new CycleAnimateable(getPoint(), 25,
                Constants.PlayerConstants.getPlayerFrontWalkOne().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2),
                Constants.PlayerConstants.getPlayerFrontWalkTwo().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
        this.frontStop = new Sprite(getPoint(),
                Constants.PlayerConstants.getPlayerFrontSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.backWalk = new CycleAnimateable(getPoint(), 25,
                Constants.PlayerConstants.getPlayerBackWalkOne().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2),
                Constants.PlayerConstants.getPlayerBackWalkTwo().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
        this.backStop = new Sprite(getPoint(),
                Constants.PlayerConstants.getPlayerBackSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.leftWalk = new CycleAnimateable(getPoint(), 25,
                Constants.PlayerConstants.getPlayerLeftSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2),
                Constants.PlayerConstants.getPlayerLeftWalkSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
        this.leftStop = new Sprite(getPoint(),
                Constants.PlayerConstants.getPlayerLeftSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));

        this.rightWalk = new CycleAnimateable(getPoint(), 25,
                Constants.PlayerConstants.getPlayerRightSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2),
                Constants.PlayerConstants.getPlayerRightWalkSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
        this.rightStop = new Sprite(getPoint(),
                Constants.PlayerConstants.getPlayerRightSprite().replaceColor(Color.WHITE, color1).replaceColor(Color.BLACK, color2));
           
        this.collider = new RectCollider(getPoint(), Constants.PlayerConstants.PLAYER_WIDTH,
                Constants.PlayerConstants.PLAYER_HEIGHT);

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

    private void setDirection() {
        if (velocity.getX() < -0.1 && RenderListener.isKeyPressed(EventCode.A)) {
            spriteState = SpriteState.LEFT_WALK;
            lastButton = "A";
        } else if (velocity.getX() > 0.1 && RenderListener.isKeyPressed(EventCode.D)) {
            spriteState = SpriteState.RIGHT_WALK;
            lastButton = "D";
        } else if (velocity.getY() < -0.1 && RenderListener.isKeyPressed(EventCode.W)) {
            spriteState = SpriteState.BACK_WALK;
            lastButton = "W";
        } else if (velocity.getY() > 0.1 && RenderListener.isKeyPressed(EventCode.S)) {
            spriteState = SpriteState.FRONT_WALK;
            lastButton = "S";
        } else {
            switch(lastButton) {
                case "W":
                    spriteState = SpriteState.BACK_STOP;    
                    break;
                case "S":
                    spriteState = SpriteState.FRONT_STOP;
                    break;
                case "A":
                    spriteState = SpriteState.LEFT_STOP;
                    break;
                case "D":
                    spriteState = SpriteState.RIGHT_STOP;
                    break;
                default:
                    // No change
                    break;
            }
        }
    }

    @Override
    public void update() {
        if (RenderListener.isKeyPressed(EventCode.SPACE)) {
            NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager, new Projectile(getPoint().copy(), 10, true));
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
            velocity.scaleX(0.2);
        }

        moveY(velocity.getY());
        if (Functions.collidingWithAny(collider, Renderer.getCollidables())) {
            moveY(-velocity.getY());
            velocity.scaleY(0.2);
        }

        // Update player sprite position
        setDirection();

        // Ensure player stays within screen bounds (considering position is the center)
        setX(Functions.clamp(getX(), Constants.PlayerConstants.PLAYER_WIDTH / 2, Renderer.getWidth() - Constants.PlayerConstants.PLAYER_WIDTH / 2));
        setY(Functions.clamp(getY(), Constants.PlayerConstants.PLAYER_HEIGHT / 2, Renderer.getHeight() - Constants.PlayerConstants.PLAYER_HEIGHT / 2));
    }

    @Override
    public void draw(Graphics g) {
        spriteStates[spriteState.ordinal()].draw(g);
        leftSwing.draw(g);
        rightSwing.draw(g);
        upSwing.draw(g);
        downSwing.draw(g);
    }

    @Override
    public boolean colliding(Collider other) {
        if (other == null) {
            return false;
        }

        return collider.colliding(other);
    }

    @Override
    public boolean colliding(Point point) {
        if (point == null) {
            return false;
        }

        return collider.colliding(point);
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
