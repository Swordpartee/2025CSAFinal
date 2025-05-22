package com.engine.game.UI;

import com.engine.game.objects.GameRect;
import com.engine.rendering.drawings.DrawerText;
import com.engine.util.Point;
import com.engine.util.PointConfig;

public class Button extends GameRect implements UIElement {
    private final DrawerText text;
    private final Runnable action;

    public Button(PointConfig position, int width, int height, String text, Runnable action) {
        super(position, width, height, true);

        this.text = new DrawerText(position, text);
        this.action = action;
    }

    @Override
    public void draw(java.awt.Graphics graphic) {
        super.draw(graphic);
        text.draw(graphic);
    }

    @Override
    public void onClick(Point point) {
        if (getCollider().colliding(point)) {
            action.run();
        }
    }
}
