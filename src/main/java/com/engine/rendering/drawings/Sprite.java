package com.engine.rendering.drawings;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.engine.util.Color;
import com.engine.util.Point;
import com.engine.util.Rect;

public class Sprite implements Drawable {
    private Rect rect;
    private final int width, height;
    private int scale = 1;

    private final Color[][] pixels;

    public Sprite(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String[] dimensions = reader.readLine().split(",");
            this.width = Integer.parseInt(dimensions[0]);
            this.height = Integer.parseInt(dimensions[1]);
            this.pixels = new Color[width][height];
            this.rect = new Rect(0, 0, width * scale, height * scale);

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
        this.scale = scale;
        updateRect(x, y);
    }
    
    public Sprite(Point center, String path, int scale) {
        this(path);
        this.scale = scale;
        updateRect(center.getX(), center.getY());
    }
    
    private void updateRect(double centerX, double centerY) {
        double scaledWidth = width * scale;
        double scaledHeight = height * scale;
        double x = centerX - scaledWidth / 2;
        double y = centerY - scaledHeight / 2;
        this.rect = new Rect(x, y, scaledWidth, scaledHeight);
    }
    
    @Override
    public void draw(Graphics graphic) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = pixels[j][i];
                if (color != null) {
                    graphic.setColor(new java.awt.Color(color.r(), color.g(), color.b()));
                    graphic.fillRect(
                        (int) (rect.getX() + j * scale), 
                        (int) (rect.getY() + i * scale), 
                        scale, 
                        scale
                    );
                }
            }
        }
    }
    
    public Rect getRect() {
        return rect;
    }
    
    public void setPosition(double centerX, double centerY) {
        updateRect(centerX, centerY);
    }
}
