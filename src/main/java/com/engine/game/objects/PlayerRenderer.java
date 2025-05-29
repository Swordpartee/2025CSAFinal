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

    public PlayerRenderer(PointConfig position, Point velocity) {
        super(position);
        this.velocity = velocity;

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
    public void update() {
        if ((int) velocity.getX() != 0 || (int) velocity.getY() != 0) {
            if ((int) velocity.getX() == 0) {
                // Moving vertically
                if (velocity.getY() > 0) {
                    sprite.setCurrentState("Front");
                    idleSprites.setCurrentState("Front");
                } else {
                    sprite.setCurrentState("Back");
                    idleSprites.setCurrentState("Back");
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
    }

    @Override
    public void draw(Graphics graphic) {
        if ((int) velocity.getX() != 0 || (int) velocity.getY() != 0) {
            sprite.draw(graphic);
        } else {
            idleSprites.draw(graphic);
        }
    }
}
