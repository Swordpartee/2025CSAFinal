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
    private int scale = 1;

    private final Color[][] pixels;

    public Sprite(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String dimensionLine = reader.readLine();
            if (dimensionLine == null) {
                throw new IOException("Sprite file is empty");
            }
            
            String[] dimensions = dimensionLine.split(",");
            if (dimensions.length < 2) {
                throw new IOException("Invalid dimensions format in sprite file");
            }
            
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            this.rect = new Rect(0, 0, width, height);
            this.pixels = new Color[width][height];

            for (int i = 0; i < height; i++) {
                String line = reader.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file at line " + (i + 1));
                }
                
                String[] pixelData = line.split(":");
                if (pixelData.length < width) {
                    throw new IOException("Not enough pixel data at line " + (i + 1));
                }
                
                for (int j = 0; j < width; j++) {
                    String colorInfo = pixelData[j];
                    if (!colorInfo.equals("(null)")) {
                        try {
                            // Parse color values from format (r,g,b)
                            colorInfo = colorInfo.substring(1, colorInfo.length() - 1); // Remove parentheses
                            String[] rgb = colorInfo.split(",");
                            int r = Integer.parseInt(rgb[0]);
                            int g = Integer.parseInt(rgb[1]);
                            int b = Integer.parseInt(rgb[2]);
                            pixels[j][i] = new Color(r, g, b);
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid color format at position [" + j + "," + i + "]: " + colorInfo);
                        }
                    } else {
                        pixels[j][i] = null; // Transparent pixel
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite: " + path, e);
        }
    }

    public Sprite(double x, double y, String path, int scale) {
        this(path);
        this.rect.setX(x);
        this.rect.setY(y);
        this.scale = scale;
    }
    
    public Sprite(Point center, String path, int scale) {
        this(path);
        this.rect = new Rect(center, rect.getWidth(), rect.getHeight());
        this.scale = scale;
    }
    
    @Override
    public void draw(Graphics graphic) {
        // Calculate top-left position from center
        double topLeftX = rect.getX() - (rect.getWidth() * scale)/2;
        double topLeftY = rect.getY() - (rect.getHeight() * scale)/2;
        
        for (int i = 0; i < rect.getHeight(); i++) {
            for (int j = 0; j < rect.getWidth(); j++) {
                Color color = pixels[j][i];
                if (color != null) {
                    graphic.setColor(new java.awt.Color(color.r(), color.g(), color.b()));
                    graphic.fillRect(
                        (int) (topLeftX + j * scale), 
                        (int) (topLeftY + i * scale), 
                        scale, 
                        scale
                    );
                }
            }
        }
    }
}
