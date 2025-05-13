package com.engine.rendering.drawings;

import java.awt.Graphics;

public class Background implements Drawable {
    private Drawable[][] tiles;

    public Background(int width, int height, int scale, String tilePaths) {
        this.tiles = new Drawable[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                tiles[i][j] = new Sprite(i * 16 * scale, j * 16 * scale, tilePaths, scale);
            }
        }
    }

    @Override
    public void draw(Graphics graphic) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].draw(graphic);
            }
        }
    }
}
