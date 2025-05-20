package com.engine.rendering.drawings;

import java.awt.Graphics;

import com.engine.rendering.Renderer;
import com.engine.util.Image;

public class Background implements Drawable {
    private final Drawable[][] tiles;

    public Background(Image tileImage) {
        int cols = (int) Math.ceil((double) Renderer.getWidth() / tileImage.getWidth()) + 1;
        int rows = (int) Math.ceil((double) Renderer.getHeight() / tileImage.getHeight()) + 1;

        tiles = new Drawable[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
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
