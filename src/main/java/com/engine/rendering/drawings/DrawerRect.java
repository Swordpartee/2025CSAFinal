package com.engine.rendering.drawings;

import java.awt.Color;
import java.awt.Graphics;

import com.engine.util.PointConfig;
import com.engine.util.Rect;

public class DrawerRect extends Rect implements Drawable {
    private boolean filled;
    private Color color;
    
    public DrawerRect(PointConfig position, double width, double height, boolean filled, Color color) {
        super(position, width, height);
        this.filled = filled;
        this.color = color;
    }

    /**
     * gets whether the rectangle is filled or not
     * @return true if filled, false if not
     */
    public boolean isFilled() {
        return filled;
    }

    /**
     * sets whether the rectangle is filled or not
     * @param filled true if filled, false if not
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public void draw(Graphics graphic) {
        // Convert center coordinates to top-left for drawing
        int x = (int) Math.round(this.getX() - this.getWidth()/2);
        int y = (int) Math.round(this.getY() - this.getHeight()/2);
        int width = (int) Math.round(this.getWidth());
        int height = (int) Math.round(this.getHeight());
        
        graphic.setColor(this.color);

        if(filled) {
            graphic.fillRect(x, y, width, height);
        } else {
            // Draw a rectangle with a border thickness of 3
            for (int i = 0; i < 3; i++) {
                graphic.drawRect(x + i, y + i, width - i * 2, height - i * 2);
            }
        }
    }
}
