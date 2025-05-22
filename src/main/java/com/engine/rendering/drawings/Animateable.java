package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Animateable extends PointController implements Drawable {
    private final Sprite[] frames;

    private int frame = 0;

    public Animateable(PointConfig position, Image... Frames) {
        super(position);

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(position, Frames[i]);
        }
    }

    public int getFrames() {
        return frames.length;
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
