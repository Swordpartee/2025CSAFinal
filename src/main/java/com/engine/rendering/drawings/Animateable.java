package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
<<<<<<< HEAD
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public abstract class Animateable extends PointController implements Drawable {
=======
<<<<<<< HEAD
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public abstract class Animateable extends PointController implements Drawable {
=======
import com.engine.util.Point;
import com.engine.util.PointController;

public class Animateable extends PointController implements Drawable {
    private final double speed;
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
    private final Sprite[] frames;

    private int frame = 0;

<<<<<<< HEAD
    public Animateable(PointConfig position, Image... Frames) {
        super(position);
=======
<<<<<<< HEAD
    public Animateable(PointConfig position, Image... Frames) {
        super(position);

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(position, Frames[i]);
        }
    }

    public int getFrames() {
        return frames.length;
=======
    public Animateable(double x, double y, double speed, Image... Frames) {
        super(x, y);
        this.speed = speed;

        time = 0;
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(position, Frames[i]);
        }
    }

<<<<<<< HEAD
    public int getFrames() {
        return frames.length;
=======
    public Animateable(Point center, double speed, Image... Frames) {
        super(center);
        this.speed = speed;

        time = 0;

        frames = new Sprite[Frames.length];
        for (int i = 0; i < Frames.length; i++) {
            frames[i] = new Sprite(super.getPosition(), Frames[i]);
        }
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
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
