package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.rendering.Renderer;
import com.engine.util.Image;

public class Background implements Drawable {
    private final Drawable[][] tiles;

    public Background(Image... tileImages) {
        if (tileImages == null || tileImages.length == 0) {
            throw new IllegalArgumentException("At least one tile image must be provided.");
        }
        double tileWidth = tileImages[0].getWidth();
        double tileHeight = tileImages[0].getHeight();
        for (Image img : tileImages) {
            if (img.getWidth() != tileWidth || img.getHeight() != tileHeight) {
                throw new IllegalArgumentException("All tile images must have the same dimensions.");
            }
        }

        int cols = (int) Math.ceil((double) Renderer.getWidth() / tileImages[0].getWidth()) + 1;
        int rows = (int) Math.ceil((double) Renderer.getHeight() / tileImages[0].getHeight()) + 1;

        tiles = new Drawable[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int randomIndex = (int) (Math.random() * tileImages.length);
                Image tileImage = tileImages[randomIndex];

                tiles[row][col] = new Sprite(col * tileImage.getWidth(), row * tileImage.getHeight(), tileImage);
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
