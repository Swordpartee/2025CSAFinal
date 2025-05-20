package com.engine.rendering.drawings;

import java.awt.Font;
import java.awt.Graphics;

import com.engine.util.Point;

public class DrawerText implements Drawable {
  private String text;
  private Point position;
  private int fontSize;
  private String fontName;
  private Font font;

  /**
   * Creates a new drawable text
   * @param text the text to be drawn
   * @param x the x position of the text
   * @param y the y position of the text
   * @param fontSize the size of the font
   * @param fontName the name of the font
   */
  public DrawerText(String text, double x, double y, int fontSize, String fontName) {
    this.text = text;
    this.position = new Point(x, y);
    this.fontSize = fontSize;
    this.fontName = fontName;
  }

  /**
   * Creates a new drawable text
   * @param text the text to be drawn
   * @param position the position of the text
   * @param fontSize the size of the font
   * @param fontName the name of the font
   */
  public DrawerText(String text, Point position, int fontSize, String fontName) {
    this.text = text;
    this.position = position;
    this.fontSize = fontSize;
    this.fontName = fontName;
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
   * Gets the position of the text
   * @return the position of the text
   */
  public Point getPosition() {
    return position;
  }

  /**
   * Sets the position of the text
   * @param position the position of the text
   */
  public void setPosition(Point position) {
    this.position = position;
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
    graphic.setFont(this.font);
    graphic.drawString(text, (int) Math.round(position.getX()), (int) Math.round(position.getY()));
  }
  
}
