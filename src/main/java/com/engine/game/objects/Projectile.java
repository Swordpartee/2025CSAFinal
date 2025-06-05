package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants;
import com.engine.game.collision.Collider;
import com.engine.game.collision.Damageable;
import com.engine.game.collision.RectCollider;
import com.engine.network.Network;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.CycleAnimateable;
import com.engine.rendering.drawings.Drawable;
import com.engine.util.Image;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Projectile extends PointController implements GameObject {

    private final Collider collider;
    private Drawable drawable;
    private final Point velocity;
    private double rotation;

    private boolean isSelf = true;
    public boolean isSelf() {
        return isSelf;
    }

    private final Damageable ignore;

    public Point getVelocity() {
        return velocity;
    }

    /**
     * Constructor for Projectile.
     * @param x The x-coordinate of the projectile.
     * @param y The y-coordinate of the projectile.
     * @param width The width of the projectile.
     * @param height The height of the projectile.
     */
    public Projectile(PointConfig position, Damageable ignore, double rotation) {
        super(position);
        this.velocity = new Point(0, 0);
        this.rotation = rotation;

        this.collider = new RectCollider(position.createOffset(Constants.WeaponConstants.WEAPON_WIDTH / 3, 0), Constants.WeaponConstants.WEAPON_WIDTH / 3, Constants.WeaponConstants.WEAPON_HEIGHT / 4);
        // Renderer.addDrawables(collider.getDebugDrawable()); // Debugging collider position and size

        Image sprite1 = new Image("src/main/resources/arrow1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        Image sprite2 = new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        Image sprite3 = new Image("src/main/resources/arrow3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        sprite1.setRotation(rotation);
        sprite2.setRotation(rotation);
        sprite3.setRotation(rotation);

        this.drawable = new CycleAnimateable(position, 10,
            sprite1,
            sprite2,
            sprite3,
            sprite2);

        // this.drawable = new CycleAnimateable(position, 5,
        //     new Image("src/main/resources/fireball/fireball1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball4.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball5.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball6.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball7.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball8.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball9.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE),
        //     new Image("src/main/resources/fireball/fireball10.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE));

        this.ignore = ignore;
    }

    public Projectile() {
        this(new PointConfig(0, 0), null, 0);
        this.isSelf = false;
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

            // Damage things that collide with the projectile
            for (Damageable c : Renderer.getDamageables()) {
                if (c != this.ignore && c.colliding(collider)) {
                    c.damage(1);
                    Network.stateManager.deleteStateByValue(this);
                    onNetworkDestroy();
                    break; // Exit loop after damaging one object
                }
            }
        } catch (Exception e) {
            onNetworkDestroy();
        }
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

        this.rotation = (double) dataSegments.readInt();

        Image sprite1 = new Image("src/main/resources/arrow1.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        Image sprite2 = new Image("src/main/resources/arrow2.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        Image sprite3 = new Image("src/main/resources/arrow3.spr", Constants.PlayerConstants.PLAYER_SPRITE_SCALE);
        sprite1.setRotation(rotation);
        sprite2.setRotation(rotation);
        sprite3.setRotation(rotation);

        this.drawable = new CycleAnimateable(getPoint(), 10,
            sprite1,
            sprite2,
            sprite3,
            sprite2);
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
        dataSegments.writeInt((int) velocity.getX());
        dataSegments.writeInt((int) velocity.getY());
        dataSegments.writeInt((int) rotation);
    }

    @Override
    public void onNetworkCreate() throws Exception {
        Renderer.addGameObjects(this);
    }

    @Override
    public void onNetworkDestroy() {
        // System.out.println("Destroying projectile: " + this);
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
