package com.engine.game.objects;

import java.awt.Graphics;
import java.awt.image.renderable.RenderContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.game.collision.Damageable;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.drawings.SpriteStates;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Tuple;

public class Player extends PointController implements GameObject, Damageable {
    private final PlayerController controller;
    private final HealthDisplay healthDisplay;

    private final SpriteStates sprite;
    private final SpriteStates idleSprites;

    private WeaponController weaponController;

    public Player() {
        super(new PointConfig());

        this.controller = new PlayerController(getPoint());
        healthDisplay = new HealthDisplay(getPoint().createOffset(0, -80), 5);

        sprite = new SpriteStates(
                new Tuple<>("Down", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerDownWalkOne(),
                        Constants.PlayerConstants.getPlayerDownWalkTwo())),
                new Tuple<>("Up", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerUpWalkOne(),
                        Constants.PlayerConstants.getPlayerUpWalkTwo())),
                new Tuple<>("Left", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerLeftWalkOne(),
                        Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerRightWalkOne(),
                        Constants.PlayerConstants.getPlayerRightSprite())));

        idleSprites = new SpriteStates(
                new Tuple<>("Down", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerDownSprite())),
                new Tuple<>("Up", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerUpSprite())),
                new Tuple<>("Left", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerRightSprite())));
        
        this.weaponController = new WeaponController(super.getPoint(), sprite, idleSprites);
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
                    sprite.setCurrentState("Down");
                    idleSprites.setCurrentState("Down");
                } else {
                    sprite.setCurrentState("Up");
                    idleSprites.setCurrentState("Up");
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

        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.E, () -> {
            weaponController.swing();
        });
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

    public void heal(int amount) {
        healthDisplay.heal(amount);
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        controller.setPosition(dataSegments.readInt(), dataSegments.readInt());
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
        // throw new UnsupportedOperationException("Not supported yet.");
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
