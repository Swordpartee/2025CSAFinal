package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
<<<<<<< HEAD
import com.engine.util.PointConfig;
=======
import com.engine.util.Point;
>>>>>>> origin/main
import com.engine.util.PointController;

public class Sprite extends PointController implements Drawable {
    private final Image image;

<<<<<<< HEAD
    public Sprite(PointConfig pointConfig, Image image) {
        super(pointConfig);
=======
    public Sprite(Point center, Image image) {
        super(center);
>>>>>>> origin/main
        this.image = image;
    }

    public Sprite(double x, double y, Image image) {
<<<<<<< HEAD
        this(new PointConfig(x, y), image);
=======
        super(x, y);
        this.image = image;
>>>>>>> origin/main
    }

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, getX(), getY());
    }

}
