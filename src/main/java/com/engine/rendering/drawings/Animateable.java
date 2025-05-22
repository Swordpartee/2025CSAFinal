package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;
import com.engine.util.PointController;

public class Animateable extends PointController implements Drawable {
    private final double speed;
    private final Sprite[] frames;

    private double time;

    public Animateable(double x, double y, double speed, Image... Frames) {
        super(x, y);
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(super.getPosition(), Frames[i]);
        }
    }

    public Animateable(Point center, double speed, Image... Frames) {
        super(center);
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(super.getPosition(), Frames[i]);
        }
    }

    public void reset() {
        time = 0;
    }

    @Override
    public void draw(Graphics graphic) {
        time ++;
        frames[(int) (time / speed) % frames.length].draw(graphic);
    }

}
