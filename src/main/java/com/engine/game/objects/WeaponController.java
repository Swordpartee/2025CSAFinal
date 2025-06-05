package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.game.collision.Collider;
import com.engine.game.collision.Damageable;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class WeaponController extends PointController implements GameObject {
    public enum SwingDirection {
        UP, DOWN, LEFT, RIGHT;

        public static SwingDirection fromVelo(Point point) {
            if (point.getY() < -0.1) {
                // System.out.println("Swinging UP");
                return UP;
            } else if (point.getY() > 0.1) {
                // System.out.println("Swinging DOWN");
                return DOWN;
            } else if (point.getX() < -0.1) {
                // System.out.println("Swinging LEFT");
                return LEFT;
            } else {
                // System.out.println("Swinging RIGHT");
                return RIGHT;
            }
        }

        public static SwingDirection fromString(String direction) {
            switch (direction.toUpperCase()) {
                case "UP":
                    // System.out.println("Swinging UP");
                    return UP;
                case "DOWN":
                    // System.out.println("Swinging DOWN");
                    return DOWN;
                case "LEFT":
                    // System.out.println("Swinging LEFT");
                    return LEFT;
                case "RIGHT":
                    // System.out.println("Swinging RIGHT");
                    return RIGHT;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }
        }

        public static SwingDirection fromInt(int direction) {
            switch (direction) {
                case 0:
                    // System.out.println("Swinging UP");
                    return UP;
                case 1:
                    // System.out.println("Swinging DOWN");
                    return DOWN;
                case 2:
                    // System.out.println("Swinging LEFT");
                    return LEFT;
                case 3:
                    // System.out.println("Swinging RIGHT");
                    return RIGHT;
                default:
                    throw new IllegalArgumentException("Invalid direction: " + direction);
            }

        }
    }

    private final InstanceAnimateable downSwing;
    private final InstanceAnimateable upSwing;
    private final InstanceAnimateable leftSwing;
    private final InstanceAnimateable rightSwing;

    private final Damageable ignore; // Damageable to ignore when swinging

    /**
     * Creates a WeaponController that manages the weapon's swing and idle animations.
     * It's meant to be used in conjunction with the player to handle.
     * @param pointConfig the set up position of the weapon
     */
    public WeaponController(PointConfig pointConfig, Damageable ignore) {
        super(pointConfig);

        this.downSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0.0, 105.0), 90);
        this.upSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0, -105), -90);
        this.leftSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), -105, 0), -180);
        this.rightSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 105, 0), 0);

        this.ignore = ignore;
    }

    /**
     * Sets the current animation state based on the velocity of the player.
     */
    public void swing(SwingDirection direction) {
        Collider collider = null;
        
        if (downSwing.isRunning() || upSwing.isRunning() || leftSwing.isRunning() || rightSwing.isRunning()) {
            // If any swing is already running, do not initiate a new swing
            return;
        }

        if (direction == SwingDirection.DOWN) {
            downSwing.run();
            collider = new RectCollider(getPoint().createOffset(0, 105), 112, 112);
        } else if (direction == SwingDirection.UP) {
            upSwing.run();
            collider = new RectCollider(getPoint().createOffset(0, -105), 112, 112);
        } else if (direction == SwingDirection.LEFT) {
            leftSwing.run();
            collider = new RectCollider(getPoint().createOffset(-105, 0), 112, 112);
        } else if (direction == SwingDirection.RIGHT) {
            rightSwing.run();
            collider = new RectCollider(getPoint().createOffset(105, 0), 112, 112);
        }

        for (Damageable c : Renderer.getDamageables()) {
            if (c != this.ignore && c.colliding(collider) ) {
                c.damage(1);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        downSwing.draw(g);
        upSwing.draw(g);
        leftSwing.draw(g);
        rightSwing.draw(g);
    }

    @Override
    public void update() {
        // don't need basic update method since is only used in player
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNetworkCreate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
