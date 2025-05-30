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
    private final InstanceAnimateable downSwing;
    private final InstanceAnimateable upSwing;
    private final InstanceAnimateable leftSwing;
    private final InstanceAnimateable rightSwing;

    private final InstanceAnimateable downIdle;
    private final InstanceAnimateable upIdle;
    private final InstanceAnimateable leftIdle;
    private final InstanceAnimateable rightIdle;

    private SpriteStates currentPlayerWalk;
    private SpriteStates currentPlayerIdle;

    /**
     * Creates a WeaponController that manages the weapon's swing and idle animations.
     * It's meant to be used in conjunction with the player to handle weapon actions.
     * @param pointConfig the set up position of the weapon
     */
    public WeaponController(PointConfig pointConfig, SpriteStates pWalk, SpriteStates pIdle) {
        super(pointConfig);

        this.downSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0.0, 105.0), 90);
        this.upSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 0, -105), -90);
        this.leftSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), -105, 0), -180);
        this.rightSwing = Constants.WeaponConstants.getSwing(new PointConfig(getPoint().getPosition(), 105, 0), 0);

        Renderer.addDrawables(downSwing, upSwing, leftSwing, rightSwing);

        this.downIdle = Constants.WeaponConstants.getIdle(new PointConfig(getPoint().getPosition(), 0.0, 105.0), 90);
        this.upIdle = Constants.WeaponConstants.getIdle(new PointConfig(getPoint().getPosition(), 0, -105), -90);
        this.leftIdle = Constants.WeaponConstants.getIdle(new PointConfig(getPoint().getPosition(), -105, 0), -180);
        this.rightIdle = Constants.WeaponConstants.getIdle(new PointConfig(getPoint().getPosition(), 105, 0), 0);

        Renderer.addDrawables(downIdle, upIdle, leftIdle, rightIdle);

        this.currentPlayerWalk = pWalk;
        this.currentPlayerIdle = pIdle;
    }

    /**
     * Sets the current swing animation state based on the sprite state of the player.
     */
    public void swing() {
        if (currentPlayerWalk.getCurrentState().equals("Down") || currentPlayerWalk.getCurrentState().equals("Down")) {
            downSwing.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Up") || currentPlayerWalk.getCurrentState().equals("Up")) {
            upSwing.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Left") || currentPlayerWalk.getCurrentState().equals("Left")) {
            leftSwing.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Right") || currentPlayerWalk.getCurrentState().equals("Right")) {
            rightSwing.run();
        }
    }

    /**
     * Sets the current idle animation state based on the sprite state of the player.
     */
    public void idle() {
        if (currentPlayerWalk.getCurrentState().equals("Down") || currentPlayerWalk.getCurrentState().equals("Down")) {
            downIdle.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Up") || currentPlayerWalk.getCurrentState().equals("Up")) {
            upIdle.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Left") || currentPlayerWalk.getCurrentState().equals("Left")) {
            leftIdle.run();
        } else if (currentPlayerWalk.getCurrentState().equals("Right") || currentPlayerWalk.getCurrentState().equals("Right")) {
            rightIdle.run();
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
