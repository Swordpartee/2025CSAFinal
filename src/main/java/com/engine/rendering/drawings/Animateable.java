package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;

public class Animateable extends PointController implements Drawable {
    private final Sprite[] frames;

    private int frame = 0;

    public Animateable(Point position, Image... Frames) {
        super(position);

    public double getX() {
        return center.getX();
    }

    public double getY() {
        return center.getY();
    }

    public void moveX(double x) {
        center.moveX(x);
    }

    public void moveY(double y) {
        center.moveY(y);
    }

    public void reset() {
        frame = 0;
    }

    public void nextFrame() {
        frame++;
        frame %= frames.length;
    }

    @Override
    public void draw(Graphics graphic) {
        frames[frame].draw(graphic);
    }

}
