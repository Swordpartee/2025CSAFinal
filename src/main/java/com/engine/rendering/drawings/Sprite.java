package com.engine.rendering.drawings;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.engine.util.Color;
import com.engine.util.Point;

public class Sprite implements Drawable {
    private Point center;
    private final int width, height;
    private int scale = 1;

    private final Color[][] pixels;

    public Sprite(String path) {
        this.center = new Point(0,0);

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String[] dimensions = reader.readLine().split(",");
            this.width = Integer.parseInt(dimensions[0]);
            this.height = Integer.parseInt(dimensions[1]);
            this.pixels = new Color[width][height];

            for (int i = 0; i < height; i++) {
                String line = reader.readLine();
                if (line != null) {
                    String[] pixelData = line.split(":");
                    for (int j = 0; j < width; j++) {
                        String colorInfo = pixelData[j];
                        if (!colorInfo.equals("(null)")) {
                            // Parse color values from format (r,g,b)
                            colorInfo = colorInfo.substring(1, colorInfo.length() - 1); // Remove parentheses
                            String[] rgb = colorInfo.split(",");
                            int r = Integer.parseInt(rgb[0]);
                            int g = Integer.parseInt(rgb[1]);
                            int b = Integer.parseInt(rgb[2]);
                            pixels[j][i] = new Color(r, g, b);
                        } else {
                            pixels[j][i] = null; // Transparent pixel
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite: " + path, e);
        }
    }

    public Sprite(double x, double y, String path, int scale) {
        this(path);

        this.center.setX(x);
        this.center.setY(y);

        this.scale = scale;
    }
    
    public Sprite(Point center, String path, int scale) {
        this(path);

        this.center = center;

        this.scale = scale;
    }
    
    @Override
    public void draw(Graphics graphic) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = pixels[j][i];
                if (color != null) {
                    graphic.setColor(new java.awt.Color(color.r(), color.g(), color.b()));
                    graphic.fillRect((int) center.getX() + j * scale, (int) center.getY() + i * scale, scale, scale);
                }
            }
        }
    }
}
