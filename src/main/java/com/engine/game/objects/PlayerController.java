package com.engine.game.objects;

import java.util.ArrayList;

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

    @Override
    public void update() {
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.SPACE, () -> {
            ArrayList<Projectile> projectiles = new ArrayList<>();

            int numProjectiles = 3; // Number of projectiles to fire
            for (int i = 0; i < numProjectiles; i++) {
                NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager,
                    new Projectile(getPoint().copy(), true));
                projectile.getValue().getVelocity().setX(10);
                // Evenly space projectiles around the player's Y center
                double spacing = 50; // pixels between projectiles
                double centerY = getPoint().getY();
                double offset = (i - (numProjectiles - 1) / 2.0) * spacing;
                projectile.getValue().getPosition().setY(centerY + offset);
                try {
                    projectile.sendSelf();
                    projectiles.add(projectile.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 

            Renderer.addGameObjects(projectiles.toArray(GameObject[]::new));
        });
        

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
