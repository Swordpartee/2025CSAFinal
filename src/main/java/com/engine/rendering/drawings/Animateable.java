package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;
import com.engine.util.PointController;

public class Animateable extends PointController implements Drawable {
    private final Sprite[] frames;

    private int frame = 0;

    public Animateable(Point position, Image... Frames) {
        super(position);

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(super.getPosition(), Frames[i]);
        }
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
