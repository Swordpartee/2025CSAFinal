package com.engine.game.objects;

import java.awt.Graphics;

import com.engine.Constants;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.drawings.SpriteStates;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;
import com.engine.util.Tuple;
import com.engine.util.Updateable;

public class PlayerRenderer extends PointController implements Drawable, Updateable {
    private final Point velocity;

    private final SpriteStates sprite;
    private final SpriteStates idleSprites;

    private boolean idle = true;
    public void setIdle(boolean idle) { this.idle = idle; }
    public boolean isIdle() { return idle; }

    public PlayerRenderer(PointConfig position, Point velocity) {
        super(position);
        this.velocity = velocity;

        sprite = new SpriteStates(
                new Tuple<>("Up", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerUpWalkOne(),
                        Constants.PlayerConstants.getPlayerUpWalkTwo())),
                new Tuple<>("Down", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerDownWalkOne(),
                        Constants.PlayerConstants.getPlayerDownWalkTwo())),
                new Tuple<>("Left", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerLeftWalkOne(),
                        Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new CycleAnimateable(getPoint(), 25,
                        Constants.PlayerConstants.getPlayerRightWalkOne(),
                        Constants.PlayerConstants.getPlayerRightSprite())));

        idleSprites = new SpriteStates(
                new Tuple<>("Up", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerUpSprite())),
                new Tuple<>("Down", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerDownSprite())),
                new Tuple<>("Left", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerLeftSprite())),
                new Tuple<>("Right", new Sprite(super.getPoint(), Constants.PlayerConstants.getPlayerRightSprite())));
    }

    @Override
    public void update() {
        if ((int) velocity.getX() != 0 || (int) velocity.getY() != 0) {
            if ((int) velocity.getX() == 0) {
                // Moving vertically
                if (velocity.getY() > 0) {
                    sprite.setCurrentState("Down");
                    idleSprites.setCurrentState("Down");
                } else {
                    sprite.setCurrentState("Up");
                    idleSprites.setCurrentState("Up");
                }
            } else {
                // Moving horizontally
                if (velocity.getX() < 0) {
                    sprite.setCurrentState("Left");
                    idleSprites.setCurrentState("Left");
                } else {
                    sprite.setCurrentState("Right");
                    idleSprites.setCurrentState("Right");
                }
            }
        }

        idle = !(((int) velocity.getX() != 0 || (int) velocity.getY() != 0));
    }

    @Override
    public void draw(Graphics graphic) {
        if (idle) {
            idleSprites.draw(graphic);
        } else {
            sprite.draw(graphic);
        }
    }

    public SpriteStates getSpriteState() {
        return idle ? idleSprites : sprite;
    }
}
