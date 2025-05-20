package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.util.Point;

public class GameRect implements GameObject {
    private final Point position;
    private final DrawerRect drawable;
    private final RectCollider collider;

    public GameRect(double x, double y, double width, double height, boolean filled) {
        this.position = new Point(x, y);
        this.drawable = new DrawerRect(position, width, height, filled);
        this.collider = new RectCollider(position, width, height);
    }

    public GameRect(Point position, double width, double height, boolean filled) {
        this.position = position;
        this.drawable = new DrawerRect(position, width, height, filled);
        this.collider = new RectCollider(position, width, height);
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

    public Point getPosition() {
        return position;
    }

    public DrawerRect getDrawable() {
        return drawable;
    }

    public RectCollider getCollider() {
        return collider;
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        this.position.setX(dataSegments.readInt());
        this.position.setY(dataSegments.readInt());
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) position.getX());
        dataSegments.writeInt((int) position.getY());
    }
}
