package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.PointConfig;

public class InstanceAnimateable extends Animateable {
    private final Sprite defaultSprite;

    private final double period;
    private double time;
    private int loops;
    private boolean running;

    public InstanceAnimateable(PointConfig position, double period, Image defaultSprite, Image... frames) {
        super(position, frames);

        this.defaultSprite = new Sprite(position, defaultSprite);

        this.period = period;
        this.time = 0;
        this.loops = 0;
        this.running = false;
    }

    public InstanceAnimateable(double x, double y, double period, Image defaultSprite, Image... frames) {
        this(new PointConfig(x, y), period, defaultSprite, frames);
    }

    public void run() {
        running = true;
    }

    @Override
    public void draw(Graphics graphics) {
        if (running) {
            time++;
            if (time >= period) {
                time = 0;
                loops++;

                nextFrame();
            }

            if (loops >= super.getFrames()) {
                loops = 0;
                running = false;
            } else {
                super.draw(graphics);
            }
        } else {
            reset();
            defaultSprite.draw(graphics);
        }
    }

}