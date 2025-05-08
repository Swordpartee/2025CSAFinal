package com.engine.rendering.drawings;

import java.awt.Graphics;

public class DrawerRect implements Drawable {
    private int x;
    private int y;

    private int width;
    private int height;

    private boolean filled;

    public DrawerRect(int x, int y, int width, int height, boolean filled) {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;

        this.filled = filled;
    }

    @Override
    public void draw(Graphics graphic) {
        if(filled) {
            graphic.fillRect(x, y, width, height);
        } else {
            graphic.drawRect(x, y, width, height);
        }
    }
}
