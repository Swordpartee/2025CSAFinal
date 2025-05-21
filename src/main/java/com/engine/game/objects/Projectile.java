package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.network.Network;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.util.Point;

public class Projectile implements GameObject {

    private final DrawerCircle drawable;
    private final Point position;
    private double rad;
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
    public Projectile(double x, double y, double rad, boolean filled) {
        this.position = new Point(x, y);
        this.velocity = new Point(0, 0);
        this.rad = rad;
        this.drawable = new DrawerCircle(this.position, rad, filled);
    }

    public Projectile() {
        this.position = new Point(0, 0);
        this.velocity = new Point(0, 0);
        this.rad = 0;
        this.drawable = new DrawerCircle(this.position, rad, true);
    }

    public void update(){
        position.setX(position.getX() + velocity.getX());
        position.setY(position.getY() + velocity.getY());

        try {
            if (position.getX() < 0 || position.getX() > 640) {
                Network.stateManager.deleteStateByValue(this);
            }
            if (position.getY() < 0 || position.getY() > 480) {
                Network.stateManager.deleteStateByValue(this);
            }
        } catch (Exception e) { } // EAT THE ERROR HAHAH
    }

    @Override
    public void draw(Graphics graphic) {
        drawable.draw(graphic);
    }

    @Override
    public boolean colliding(Collider other) {
        return false;
    }

    @Override
    public ColliderType getType() {
        return ColliderType.NONE;
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        this.rad = dataSegments.readInt();
        this.drawable.setRadius(rad);
        this.position.setX(dataSegments.readInt());
        this.position.setY(dataSegments.readInt());
        this.velocity.setX(dataSegments.readInt());
        this.velocity.setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) rad);
        dataSegments.writeInt((int) position.getX());
        dataSegments.writeInt((int) position.getY());
        dataSegments.writeInt((int) velocity.getX());
        dataSegments.writeInt((int) velocity.getY());
    }

    public String toString() {
        return "Projectile{" +
                "position=" + position +
                ", rad=" + rad +
                ", velocity=" + velocity +
                '}';
    }
}
