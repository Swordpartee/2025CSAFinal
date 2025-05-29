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

    public void damage(int damage) {
        if (health - damage >= 0) {
            health -= damage;
        } else {
            health = 0;
        }
    }

    public void heal(int amount) {
        if (health + amount <= maxHealth) {
            health += amount;
        } else {
            health = maxHealth;
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
        // Draw the health bar centered at position
        int heartWidth = 16 * 3;
        int totalWidth = maxHealth * heartWidth;
        int startX = (int)(getX() - totalWidth * 2 / maxHealth) - (8 * (maxHealth /2));

        for (int i = 0; i < maxHealth; i++) {
            int currentX = startX + (i * heartWidth) + (8 * i);
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
