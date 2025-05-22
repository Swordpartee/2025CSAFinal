package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.game.collision.RectCollider;
import com.engine.rendering.drawings.DrawerRect;
<<<<<<< HEAD
import com.engine.util.PointConfig;
=======
<<<<<<< HEAD
import com.engine.util.PointConfig;
=======
import com.engine.util.Point;
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
import com.engine.util.PointController;

public class GameRect extends PointController implements GameObject {
    private final DrawerRect drawable;
    private final RectCollider collider;

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
    public GameRect(PointConfig position, double width, double height, boolean filled) {
        super(position);
        this.drawable = new DrawerRect(position, width, height, filled);
        this.collider = new RectCollider(position, width, height);
<<<<<<< HEAD
    }

    public GameRect(double x, double y, double width, double height, boolean filled) {
        this(new PointConfig(x, y), width, height, filled);
=======
    }

    public GameRect(double x, double y, double width, double height, boolean filled) {
        this(new PointConfig(x, y), width, height, filled);
=======
    public GameRect(double x, double y, double width, double height, boolean filled) {
        super(x, y);
        this.drawable = new DrawerRect(getPosition(), width, height, filled);
        this.collider = new RectCollider(getPosition(), width, height);
    }

    public GameRect(Point position, double width, double height, boolean filled) {
        super(position);
        this.drawable = new DrawerRect(getPosition(), width, height, filled);
        this.collider = new RectCollider(getPosition(), width, height);
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
    }

    @Override
    public void draw(Graphics graphic) {
        drawable.draw(graphic);
    }

    @Override
    public boolean colliding(Collider other) {
        return collider.colliding(other);
    }

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
    @Override
    public ColliderType getType() {
        return ColliderType.OTHER;
    }

>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
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
