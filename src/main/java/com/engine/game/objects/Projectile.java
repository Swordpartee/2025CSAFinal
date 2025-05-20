package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.util.Point;
import com.engine.util.PointController;

public class Projectile extends PointController implements GameObject {

    private final DrawerCircle drawable;
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
        super(x, y);
        this.velocity = new Point(0, 0);
        this.rad = rad;
        this.drawable = new DrawerCircle(getPosition(), rad, filled);
    }

    public Projectile() {
        super();
        this.velocity = new Point(0, 0);
        this.rad = 0;
        this.drawable = new DrawerCircle(getPosition(), rad, true);
    }

    @Override
    public void update() {
        setX(getX() + velocity.getX());
        setY(getY() + velocity.getY());

        if (getX() < 0 || getX() > 640) {
            velocity.setX(-velocity.getX());
        }
        if (getY() < 0 || getY() > 480) {
            velocity.setY(-velocity.getY());
        }
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
        getPosition().setX(dataSegments.readInt());
        getPosition().setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) rad);
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
    }

    @Override
    public String toString() {
        return "Projectile{" +
                "position=" + getPosition() +
                ", rad=" + rad +
                ", velocity=" + velocity +
                '}';
    }
}
