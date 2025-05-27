package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.Constants.GameConstants;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class HealthDisplay extends PointController implements GameObject {

    private int health;
    private int maxHealth;

    public HealthDisplay(PointConfig position, int maxHealth) {
        super(position);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public HealthDisplay(int x, int y, int maxHealth) {
        this(new PointConfig(x, y), maxHealth);
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void damage() {
        if (health > 0) {
            health--;
        }
    }

    public void heal() {
        if (health < maxHealth) {
            health++;
        }
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public void update() {
        // Update logic for health display can be added here
    }

    @Override
    public void draw(Graphics graphic) {
        // Calculate dimensions once outside the loop
        int iconWidth = GameConstants.HEALTH_SPRITE_SCALE * 16;
        int totalWidth = maxHealth * iconWidth;
        // Calculate starting X position to center the health bar
        int startX = (int)(getX() - totalWidth / 2.0);

        // Draw each heart
        for (int i = 0; i < maxHealth; i++) {
            int currentX = startX + i * iconWidth;
            
            if (i < health) {
                GameConstants.HEART_SPRITE.draw(graphic, currentX, getY());
            } else {
                GameConstants.EMPTY_HEALTH_IMAGE.draw(graphic, currentX, getY());
            }
        }
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNetworkCreate() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
