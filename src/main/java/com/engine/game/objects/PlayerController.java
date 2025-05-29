package com.engine.game.objects;

import com.engine.Constants;
import com.engine.game.collision.Collider;
import com.engine.game.collision.RectCollider;
import com.engine.network.Network;
import com.engine.network.headers.Header;
import com.engine.network.states.NetState;
import com.engine.rendering.Renderer;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Functions;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Updateable;

public class PlayerController extends PointController implements Updateable, Collider {

    private final Point velocity;

    private final RectCollider collider;

    public PlayerController(PointConfig position) {
        super(position);
        this.velocity = new Point(0, 0);

        this.collider = new RectCollider(getPoint(), Constants.PlayerConstants.PLAYER_WIDTH, Constants.PlayerConstants.PLAYER_HEIGHT);
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
            NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager,
                    new Projectile(getPoint().copy(), 10, true));
            Renderer.addGameObjects(projectile.getValue());
            projectile.getValue().getVelocity().setX(1);
            try {
                projectile.sendSelf();
            } catch (Exception e) {
            }
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

        velocity.setX(Functions.clamp(velocity.getX() * Constants.PlayerConstants.PLAYER_FRICTION,
                -Constants.PlayerConstants.PLAYER_MAX_SPEED, Constants.PlayerConstants.PLAYER_MAX_SPEED));
        velocity.setY(Functions.clamp(velocity.getY() * Constants.PlayerConstants.PLAYER_FRICTION,
                -Constants.PlayerConstants.PLAYER_MAX_SPEED, Constants.PlayerConstants.PLAYER_MAX_SPEED));

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

        // Ensure player stays within screen bounds (considering position is the center)
        setX(Functions.clamp(getX(), Constants.PlayerConstants.PLAYER_WIDTH / 2,
                Renderer.getWidth() - Constants.PlayerConstants.PLAYER_WIDTH / 2));
        setY(Functions.clamp(getY(), Constants.PlayerConstants.PLAYER_HEIGHT / 2,
                Renderer.getHeight() - Constants.PlayerConstants.PLAYER_HEIGHT / 2));
    }
    
    public Point getVelocity() {
        return velocity;
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
    
}
