package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import com.engine.Constants;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.drawings.SpriteStates;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Tuple;

public class WeaponController extends PointController implements GameObject {
    private Point velocity;

    private final InstanceAnimateable downSwing;
    private final InstanceAnimateable upSwing;
    private final InstanceAnimateable leftSwing;
    private final InstanceAnimateable rightSwing;

    private String currentSwingDirection = "";

    /**
     * Creates a WeaponController that manages the weapon's swing and idle animations.
     * It's meant to be used in conjunction with the player to handle.
     * @param pointConfig the set up position of the weapon
     * @param velocity the velocity point from the player to be used for swing direction
     */
    public WeaponController(PointConfig pointConfig, Point velocity) {
        super(pointConfig);

        this.velocity = velocity;

        this.downSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0.0, 105.0), 90);
        this.upSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0, -105), -90);
        this.leftSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), -105, 0), -180);
        this.rightSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 105, 0), 0);

        Renderer.addDrawables(downSwing, upSwing, leftSwing, rightSwing);
    }

    /**
     * Sets the current animation state based on the velocity of the player.
     */
    public void swing() {
        if (velocity.getY() > 0.1) {
            downSwing.run();
            currentSwingDirection = "Down";
        } else if (velocity.getY() < -0.1) {
            upSwing.run();
            currentSwingDirection = "Up";
        } else if (velocity.getX() < -0.1) {
            leftSwing.run();
            currentSwingDirection = "Left";
        } else if (velocity.getX() > 0.1) {
            rightSwing.run();
            currentSwingDirection = "Right";
        } else {
            switch (currentSwingDirection) {
                case "Up":
                    upSwing.run();
                    break;
                case "Down":
                    downSwing.run();
                    break;
                case "Left":
                    leftSwing.run();
                    break;
                case "Right":
                    rightSwing.run();
                    break;
                default:  
                    
                    // No swing direction set, do nothing
                    return;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        // don't need basic draw method since is only used in player
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
