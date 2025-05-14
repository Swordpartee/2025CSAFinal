package com.engine.game;

import java.awt.Graphics;

import com.engine.rendering.drawings.Drawable;
import com.engine.rendering.drawings.Sprite;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Point;
import com.engine.util.Updateable;

public class PlayerController extends Point implements Updateable, Drawable {
    private final Sprite sprite;

    public PlayerController(String spritePath) {
        this.sprite = new Sprite(0, 0, spritePath, 7);
    }

    @Override
    public void update() {
        if (RenderListener.isKeyPressed(EventCode.W)) {
            this.moveY(-2);
        }
        if (RenderListener.isKeyPressed(EventCode.S)) {
            this.moveY(2);
        }
        if (RenderListener.isKeyPressed(EventCode.A)) {
            this.moveX(-2);
        }
        if (RenderListener.isKeyPressed(EventCode.D)) {
            this.moveX(2);
        }

        sprite.setPos(this.getX(), this.getY());
    }

    @Override
    public void draw(Graphics g) {
        sprite.draw(g);
    }
}
