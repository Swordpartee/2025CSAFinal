package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.util.Image;

public class Background implements Drawable {
    private final Drawable[][] tiles;

    public Background(int width, int height, int scale, Image tilePaths) {
        this.tiles = new Drawable[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Sprite(i * 16 * scale, j * 16 * scale, tilePaths);
            }
        }
    }

    @Override
    public void draw(Graphics graphic) {
        for (Drawable[] tile : tiles) {
            for (Drawable tile1 : tile) {
                tile1.draw(graphic);
            }
        }
    }
}
