package com.engine.rendering.drawings;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.engine.game.UI.UIElement;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class DrawerText extends PointController implements UIElement {
    private String text;
    private int fontSize;
    private String fontName;
    private Font font;
    private Color color;

  /**
   * Creates a new drawable text
   * @param text the text to be drawn
   * @param x the x position of the text
   * @param y the y position of the text
   * @param fontSize the size of the font
   * @param fontName the name of the font
   */
  public DrawerText(PointConfig position, String text, int fontSize, String fontName, Color color) {
    super(position);

    this.color = color;
    this.text = text;
    this.fontSize = fontSize;
    this.fontName = fontName;
    this.font = new Font(fontName, Font.PLAIN, fontSize);
  }

  public DrawerText(double x, double y, String text, int fontSize, String fontName, Color color) {
    this(new PointConfig(x, y), text, fontSize, fontName, color);
  }

  public DrawerText(PointConfig position, String text, Color color) {
    this(position, text, 12, "Arial", color);
  }

  /**
   * Gets the text to be drawn
   * @return the text to be drawn
   */
  public String getText() {
    return text;
  }

    /**
     * Sets the text to be drawn
     * @param text the text to be drawn
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the font size of the text
     * @return the font size of the text
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size of the text
     * @param fontSize the font size of the text
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.font = new Font(fontName, Font.PLAIN, fontSize);
    }

  /**
   * Gets the font name of the text
   * @return the font name of the text
   */
  public String getFontName() {
    return fontName;
  }

  /**
   * Sets the font name of the text
   * @param fontName the font name of the text
   */
  public void setFontName(String fontName) {
    this.fontName = fontName;
    this.font = new Font(fontName, Font.PLAIN, fontSize);
  }

  /**
   * Draws the text on the screen
   * @param graphic the graphics object to draw on
   */
  @Override
  public void draw(Graphics graphic) {
    graphic.setColor(this.color);
    graphic.setFont(this.font);
    String[] lines = text.split("\n");
    int lineHeight = graphic.getFontMetrics(this.font).getHeight();
    int totalHeight = lineHeight * lines.length;
    int startY = (int) Math.round(getY() - totalHeight / 2.0) + graphic.getFontMetrics(this.font).getAscent();

    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      int textWidth = graphic.getFontMetrics(this.font).stringWidth(line);
      int drawX = (int) Math.round(getX() - textWidth / 2.0);
      int drawY = startY + i * lineHeight;
      graphic.drawString(line, drawX, drawY);
    }
  }
  
}
