package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.PointConfig;

public class CycleAnimateable extends Animateable {
    private final double period;
    private double time;

    public CycleAnimateable(PointConfig position, double period, Image... frames) {
        super(position, frames);

        this.period = period;
        this.time = 0;
    }

    public CycleAnimateable(double x, double y, double period, Image... frames) {
        this(new PointConfig(x, y), period, frames);
    }

    @Override
    public void draw(Graphics graphics) {
        time++;
        if (time >= period) {
            time = 0;
            nextFrame();
        }
        super.draw(graphics);
    }

}