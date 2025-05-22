package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.Point;
import com.engine.util.PointController;

public class Sprite extends PointController implements Drawable {
    private final Image image;

    public Sprite(Point center, Image image) {
        super(center);
        this.image = image;
    }

    public Sprite(double x, double y, Image image) {
        super(x, y);
        this.image = image;
    }

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, getX(), getY());
    }

}
