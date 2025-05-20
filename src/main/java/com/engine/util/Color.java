package com.engine.util;

public enum Color {
    RED(255, 0, 0),
    ORANGE(255, 165, 0),
    YELLOW(255, 255, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    PURPLE(128, 0, 128),
    PINK(255, 192, 203),
    WHITE(255, 255, 255),
    BLACK(0, 0, 0);

    private final int r, g, b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getRGB() {
        return (r << 16) | (g << 8) | b | 0xFF000000;
    }
    
}
