package com.engine.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Image {
    private final int scale;
    private int width;
    private int height;
    private BufferedImage image;

    public Image(String path, int scale) {
        this.scale = scale;

        getPixels(path);
    }

    public Image(String path, int scale, double angle) {
        this(path, scale);
        this.image = rotateImageByDegrees(this.image, angle);
    }

    public Image() {
        this.scale = 1;

        this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }

    private BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return rotated;
    }
    
    private void getPixels(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String dimensionLine = reader.readLine();
            if (dimensionLine == null) {
                throw new IOException("Sprite file is empty");
            }

            String[] dimensions = dimensionLine.split(",");
            if (dimensions.length < 2) {
                throw new IOException("Invalid dimensions format in sprite file");
            }

            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);
            this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

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
                            // pixels[j][i] = new Color(r, g, b);
                            image.setRGB(j, i, (r << 16) | (g << 8) | b | 0xFF000000); // Set pixel color
                        } catch (NumberFormatException e) {
                            throw new IOException(
                                    "Invalid color format at position [" + j + "," + i + "]: " + colorInfo);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sprite: " + path, e);
        }
    }

    public double getWidth() {
        return width * scale;
    }

    public double getHeight() {
        return height * scale;
    }
    
    public void draw(Graphics graphic, double x, double y) {
        // Calculate top-left position from center
        int topLeftX = (int) (x - (width * scale) / 2);
        int topLeftY = (int) (y - (height * scale) / 2);

        // Draw the image at the calculated position
        graphic.drawImage(image, topLeftX, topLeftY, width * scale, height * scale, null);
    }

    public Image replaceColor(Color oldColor, Color newColor) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = image.getRGB(x, y);
                if (pixelColor == oldColor.getRGB()) {
                    image.setRGB(x, y, newColor.getRGB());
                }
            }
        }
        return this;
    }
}
