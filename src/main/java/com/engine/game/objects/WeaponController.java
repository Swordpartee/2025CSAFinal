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
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.drawings.SpriteStates;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Tuple;

public class WeaponController extends PointController implements GameObject {
    private final Point velocity;

    private final InstanceAnimateable downSwing;
    private final InstanceAnimateable upSwing;
    private final InstanceAnimateable leftSwing;
    private final InstanceAnimateable rightSwing;

    /**
     * Creates a WeaponController that manages the weapon's swing and idle animations.
     * It's meant to be used in conjunction with the player to handle.
     * @param pointConfig the set up position of the weapon
     */
    public WeaponController(PointConfig pointConfig, Point velocity) {
        super(pointConfig);

        this.velocity = velocity;

        this.downSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0.0, 105.0), 90);
        this.upSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0, -105), -90);
        this.leftSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), -105, 0), -180);
        this.rightSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 105, 0), 0);

    }

    /**
     * Sets the current animation state based on the velocity of the player.
     */
    public void swing() {
        Collider collider = new Collider() {
        };
        
        if ((int) velocity.getY() > 0) {
            downSwing.run();
            collider = new RectCollider(getPoint().createOffset(0, 105), 112, 112);
        } else if ((int) velocity.getY() < 0) {
            upSwing.run();
            collider = new RectCollider(getPoint().createOffset(0, -105), 112, 112);
        } else if ((int) velocity.getX() < 0) {
            leftSwing.run();
            collider = new RectCollider(getPoint().createOffset(-105, 0), 112, 112);
        } else if ((int) velocity.getX() > 0) {
            rightSwing.run();
            collider = new RectCollider(getPoint().createOffset(105, 0), 112, 112);
        }

        for (Damageable c : Renderer.getDamageables()) {
            if (c.colliding(collider) ) {
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
