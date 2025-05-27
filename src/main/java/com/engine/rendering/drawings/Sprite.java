package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Sprite extends PointController implements Drawable {
    private final Image image;

    public Sprite(PointConfig pointConfig, Image image) {
        super(pointConfig);
        this.image = image;
    }

    public Sprite(double x, double y, Image image) {
        this(new PointConfig(x, y), image);
    }

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, getX(), getY());
    }

    public Image getImage() {
        return image;
    }
}
