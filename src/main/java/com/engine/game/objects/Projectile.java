package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.network.Network;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Drawable;
import com.engine.util.Image;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Projectile extends PointController implements GameObject {

    private final Drawable drawable;
    private final Point velocity;

    public Point getVelocity() {
        return velocity;
    }

    /**
     * Constructor for Projectile.
     * @param x The x-coordinate of the projectile.
     * @param y The y-coordinate of the projectile.
     * @param width The width of the projectile.
     * @param height The height of the projectile.
     * @param filled Indicates if the projectile is filled or not.
     */
    public Projectile(PointConfig position, boolean filled) {
        super(position);
        this.velocity = new Point(0, 0);
        // this.drawable = new CycleAnimateable(position, 10,
        //     new Image("src/main/resources/arrow1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/arrow3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));
        this.drawable = new CycleAnimateable(position, 5,
            new Image("src/main/resources/fireball/fireball1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball6.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball7.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball8.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball9.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
            new Image("src/main/resources/fireball/fireball10.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));
    }

    public Projectile() {
        this(new PointConfig(0, 0), true);
    }

    @Override
    public void update() {
        moveX(velocity.getX());
        moveY(velocity.getY());
        try {
            if (getX() < 0 || getX() > 640) {
                Network.stateManager.deleteStateByValue(this);
            }
            if (getY() < 0 || getY() > 480) {
                Network.stateManager.deleteStateByValue(this);
                onNetworkDestroy();
            }
        } catch (Exception e) {
            onNetworkDestroy();

        } // EAT THE ERROR HAHAH
    }

    @Override
    public void draw(Graphics graphic) {
        drawable.draw(graphic);
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        this.setX(dataSegments.readInt());
        this.setY(dataSegments.readInt());
        this.velocity.setX(dataSegments.readInt());
        this.velocity.setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
        dataSegments.writeInt((int) velocity.getX());
        dataSegments.writeInt((int) velocity.getY());
    }

    @Override
    public void onNetworkCreate() throws Exception {
        Renderer.addGameObjects(this);
    }

    @Override
    public void onNetworkDestroy() {
        Renderer.removeGameObjects(this);
    }

    @Override
    public String toString() {
        return "Projectile{" +
                "position=" + getPosition() +
                ", velocity=" + velocity +
                '}';
    }
}
