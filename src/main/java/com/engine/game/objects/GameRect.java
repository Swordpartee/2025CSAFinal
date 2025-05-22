package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.util.Point;
import com.engine.util.PointController;

public class GameRect extends PointController implements GameObject {
    private final DrawerRect drawable;
    private final RectCollider collider;

    public GameRect(double x, double y, double width, double height, boolean filled) {
        super(x, y);
        this.drawable = new DrawerRect(getPosition(), width, height, filled);
        this.collider = new RectCollider(getPosition(), width, height);
    }

    public GameRect(Point position, double width, double height, boolean filled) {
        super(position);
        this.drawable = new DrawerRect(getPosition(), width, height, filled);
        this.collider = new RectCollider(getPosition(), width, height);
    }

    @Override
    public void draw(Graphics graphic) {
        drawable.draw(graphic);
    }

    @Override
    public void update() {
        // Update logic for the rectangle can be added here
    }

    @Override
    public boolean colliding(Collider other) {
        return collider.colliding(other);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.OTHER;
    }

    public DrawerRect getDrawable() {
        return drawable;
    }

    public RectCollider getCollider() {
        return collider;
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        setX(dataSegments.readInt());
        setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
    }

    @Override
    public void onNetworkCreate() throws Exception {
        // Logic for when the object is created in the network
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        // Logic for when the object is destroyed in the network
    }
}
