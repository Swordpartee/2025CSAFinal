package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.game.collision.Damageable;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.drawings.SpriteStates;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Tuple;

public class Player extends PointController implements GameObject, Damageable {
    private final PlayerController controller;
    private final HealthDisplay healthDisplay;

    private final SpriteStates sprite;
    private final SpriteStates idleSprites;

    public Player() {
        super(new PointConfig());

        this.controller = new PlayerController(getPoint());
        healthDisplay = new HealthDisplay(getPoint().createOffset(0, -80), 5);

        sprite = new SpriteStates(
                new Tuple<>("Front", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerFrontWalkOne(),
                        Constants.PlayerConstants.getPlayerFrontWalkTwo())),
                new Tuple<>("Back", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerBackWalkOne(),
                        Constants.PlayerConstants.getPlayerBackWalkTwo())),
                new Tuple<>("Left", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerLeftWalkOne(),
                        Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerRightWalkOne(),
                        Constants.PlayerConstants.getPlayerRightSprite())));

        idleSprites = new SpriteStates(
                new Tuple<>("Front", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerFrontSprite())),
                new Tuple<>("Back", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerBackSprite())),
                new Tuple<>("Left", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerRightSprite())));
    }
    
    @Override
    public void damage(int damage) {
        healthDisplay.damage(damage);
    }

    @Override
    public void update() {
        controller.update();
        healthDisplay.update();
        if ((int) controller.getVelocity().getX() != 0 || (int) controller.getVelocity().getY() != 0) {
            if ((int) controller.getVelocity().getX() == 0) {
                // Moving vertically
                if (controller.getVelocity().getY() > 0) {
                    sprite.setCurrentState("Front");
                    idleSprites.setCurrentState("Front");
                } else {
                    sprite.setCurrentState("Back");
                    idleSprites.setCurrentState("Back");
                }
            } else {
                // Moving horizontally
                if (controller.getVelocity().getX() < 0) {
                    sprite.setCurrentState("Left");
                    idleSprites.setCurrentState("Left");
                } else {
                    sprite.setCurrentState("Right");
                    idleSprites.setCurrentState("Right");
                }
            }
        }
    }

    @Override
    public void draw(Graphics graphic) {
        healthDisplay.draw(graphic);
        if ((int) controller.getVelocity().getX() != 0 || (int) controller.getVelocity().getY() != 0) {
            sprite.draw(graphic);
        } else {
            idleSprites.draw(graphic);
        }

    }

    public void heal() {
        healthDisplay.heal();
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
