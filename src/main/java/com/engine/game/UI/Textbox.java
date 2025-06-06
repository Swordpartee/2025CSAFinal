package com.engine.game.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import com.engine.game.objects.GameRect;
import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.DrawerRect;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.rendering.io.RenderListener.KeyTypedListener;
import com.engine.util.Point;
import com.engine.util.PointConfig;

public class Textbox extends Button {
    public interface SubmitAction {
        boolean onSubmit(String text);
    }

    private static Textbox instance = null;

    public static Textbox getInstance() {
        return instance;
    }

    private static KeyTypedListener keyTypedListener = new KeyTypedListener() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            if (instance == null) {
                return;
            }

            instance.updateText(e);
        }
    };

    public static KeyTypedListener getKeyTypedListener() {
        return keyTypedListener;
    }

    private String text = "";
    private int cursorPosition = 0; 

    private Color textColor = Color.WHITE;
    private final Font font;

    private final SubmitAction submitAction;

    private final Drawable drawable;

    private String regex = "[^a-zA-Z0-9 ]"; // Only allow alphanumeric characters and spaces

    private boolean isSecret = false;
    public boolean isSecret() { return isSecret; }
    public void setSecret(boolean isSecret) { this.isSecret = isSecret; }

    public Textbox(PointConfig pointConf, double width, double height, Color bgColor, Color textColor, int fontSize, String regex, SubmitAction action) {
        super(new GameRect(pointConf, width, height, true, bgColor), () -> {});
        this.drawable = new DrawerRect(new PointConfig(pointConf.getX(), pointConf.getY() + height / 2), width, 5, true, bgColor);
        this.submitAction = action;
        this.textColor = textColor;
        this.font = new Font("Arial", Font.PLAIN, fontSize);
        this.regex = regex;
    }

    public Textbox(PointConfig pointConf, double width, double height, Color bgColor, Color textColor, int fontSize, SubmitAction action) {
        this(pointConf, width, height, bgColor, textColor, fontSize, "(?s).*", action);
    }
    
    @Override
    public boolean onClick(Point point) {
        boolean clicked = super.onClick(point);

        if (!clicked && instance == this) {
            instance = null;
            return false;
        } else if (!clicked) {
            return false;
        }
        instance = this;
        return true;
    }

    @Override
    public void draw(Graphics graphic) {
        this.drawable.draw(graphic);
        GameRect object = (GameRect) getObject();
        int width = (int) object.getCollider().getWidth();
        int height = (int) object.getCollider().getHeight();
        
        // Determine the display text based on whether it's a secret
        String displayText = isSecret ? "*".repeat(text.length()) : text;

        graphic.setFont(font);

        // Store the original clip to restore later
        Shape originalClip = graphic.getClip();
        

        // Set clipping region to the bounds of the GameRect
        graphic.setClip((int) object.getX() - (width / 2), (int) object.getY() - (height / 2), 
                        width, height);
        
        // Add some padding so text doesn't touch the edges
        int padding = 5;
        int textX = (int) object.getX() - (width / 2) + padding;
        
        // Get font metrics to properly position text vertically
        FontMetrics fm = graphic.getFontMetrics();
        

        int textBaselineY = (int) object.getY() + (fm.getAscent() - fm.getDescent()) / 2;

        // Get cursor draw info
        String textToCursor = displayText.substring(0, Math.min(cursorPosition, displayText.length()));
        int cursorX = textX + fm.stringWidth(textToCursor);
        int cursorTopY = textBaselineY - fm.getAscent();
        int cursorBottomY = textBaselineY + fm.getDescent();
        
        if (cursorX > textX + width - padding * 2) {
            cursorX = textX + width - padding * 2;
            int textWidth = fm.stringWidth(textToCursor);
            if (textWidth > width - padding * 2) {
                textX -= textWidth - (width - (padding * 2));
            }
        }
        
        // Draw the text - it will be clipped to the rectangle bounds
        graphic.setColor(textColor);
        // Center the text vertically at object.getY()
        graphic.drawString(displayText, textX, textBaselineY);
        if (instance == this) { graphic.drawLine(cursorX, cursorTopY, cursorX, cursorBottomY); }

        // Restore the original clipping region
        graphic.setClip(originalClip);

        // Get the arrow keybinds
        if (RenderListener.isKeyPressed(EventCode.LEFT)) {
            cursorPosition = Math.max(0, cursorPosition - 1);
        }
        else if (RenderListener.isKeyPressed(EventCode.RIGHT)) {
            cursorPosition = Math.min(displayText.length(), cursorPosition + 1);
        }
        else if (RenderListener.isKeyPressed(EventCode.UP)) {
            cursorPosition = 0;
        }
        else if (RenderListener.isKeyPressed(EventCode.DOWN)) {
            cursorPosition = displayText.length();
        }
    }

    // Getter methods for accessing the text
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    public void updateText(java.awt.event.KeyEvent e) {
        char keyChar = e.getKeyChar();
        
        switch (keyChar) {
            case KeyEvent.VK_BACK_SPACE -> {
                if (cursorPosition > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (submitAction.onSubmit(text)) {
                    // Reset the textbox or perform any other action
                    text = "";
                    cursorPosition = 0;
                }
            }
            default -> {
                if (!Pattern.matches(regex, String.valueOf(keyChar))) {
                    // Ignore characters that do not match the regex
                    return;
                }   // Only add printable characters
                if (Character.isDefined(keyChar) && !Character.isISOControl(keyChar)) {
                    text = text.substring(0, cursorPosition) + keyChar + text.substring(cursorPosition);
                    cursorPosition++;
                }
            }
        }
    }
}