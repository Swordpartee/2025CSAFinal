package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;
<<<<<<< HEAD
import com.engine.util.PointConfig;
=======
<<<<<<< HEAD
import com.engine.util.PointConfig;
=======
import com.engine.util.Point;
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
import com.engine.util.PointController;

public class Sprite extends PointController implements Drawable {
    private final Image image;

<<<<<<< HEAD
    public Sprite(PointConfig pointConfig, Image image) {
        super(pointConfig);
=======
<<<<<<< HEAD
    public Sprite(PointConfig pointConfig, Image image) {
        super(pointConfig);
=======
    public Sprite(Point center, Image image) {
        super(center);
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
        this.image = image;
    }

    public Sprite(double x, double y, Image image) {
<<<<<<< HEAD
        this(new PointConfig(x, y), image);
=======
<<<<<<< HEAD
        this(new PointConfig(x, y), image);
=======
        super(x, y);
        this.image = image;
>>>>>>> origin/main
>>>>>>> 6fdcdf302b0a17d74ae40c18398d77ea6af752e6
    }

    @Override
    public void draw(Graphics graphic) {
        image.draw(graphic, getX(), getY());
    }

}
