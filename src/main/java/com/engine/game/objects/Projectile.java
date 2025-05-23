package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.network.Network;
import com.engine.rendering.Renderer;
import com.engine.rendering.drawings.DrawerCircle;
import com.engine.util.Point;
import com.engine.util.PointConfig;
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
    public Projectile(PointConfig position, double rad, boolean filled) {
        super(position);
        this.velocity = new Point(0, 0);
        this.rad = rad;
        this.drawable = new DrawerCircle(getPoint(), rad, filled);
    }

    public Projectile() {
        this(new PointConfig(0, 0), 10, true);
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
        this.rad = dataSegments.readInt();
        this.drawable.setRadius(rad);
        this.setX(dataSegments.readInt());
        this.setY(dataSegments.readInt());
        this.velocity.setX(dataSegments.readInt());
        this.velocity.setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) rad);
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
                ", rad=" + rad +
                ", velocity=" + velocity +
                '}';
    }
}
