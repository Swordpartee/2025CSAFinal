package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import com.engine.game.collision.Collider;
import com.engine.game.collision.Damageable;
import com.engine.game.objects.WeaponController.SwingDirection;
import com.engine.network.Network;
import com.engine.network.headers.Header;
import com.engine.network.states.ControlMode;
import com.engine.network.states.NetState;
import com.engine.rendering.Renderer;
import com.engine.rendering.io.EventCode;
import com.engine.rendering.io.RenderListener;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Player extends PointController implements GameObject, Damageable {
    private final PlayerController controller;
    private final HealthDisplay healthDisplay;
    private final PlayerRenderer sprite;
    private final WeaponController weaponController;

    private boolean shouldSwing = false; // Flag to indicate if the player should swing the weapon

    public Player() {
        super(new PointConfig());

        controller = new PlayerController(getPoint());
        healthDisplay = new HealthDisplay(getPoint().createOffset(0, -80), 5);
        sprite = new PlayerRenderer(getPoint(), controller.getVelocity());
        weaponController = new WeaponController(getPoint(), this);

    }

    @Override
    public void damage(int damage) {
        healthDisplay.damage(damage);
    }

    @Override
    public void update() {
        controller.update();
        healthDisplay.update();
        sprite.update();

        // TEMP SHOOT PROJECTILES - put it somewhere else later, idk, just make it more organized
        RenderListener.addBinding(EventCode.EventType.KEY_PRESSED, EventCode.R, () -> {
            ArrayList<Projectile> projectiles = new ArrayList<>();

            int numProjectiles = 3; // Number of projectiles to fire
            for (int i = 0; i < numProjectiles; i++) {
                NetState<Projectile> projectile = new NetState<>(Header.ProjectileState, Network.stateManager,
                    new Projectile(getPoint().copy(), this));
                projectile.setControlMode(ControlMode.BOTH); // Allow both server and client to control the projectile (Aka. Move + Delete it)
                projectile.getValue().getVelocity().setX(5);
                // Evenly space projectiles around the player's Y center
                double spacing = 50; // pixels between projectiles
                double centerY = getPoint().getY();
                double offset = (i - (numProjectiles - 1) / 2.0) * spacing;
                projectile.getValue().getPosition().setY(centerY + offset);
                try {
                    projectile.sendSelf();
                    projectiles.add(projectile.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 

            Renderer.addGameObjects(projectiles.toArray(GameObject[]::new));
        });

        if (shouldSwing) {
            weaponController.swing(SwingDirection.fromVelo(controller.getVelocity()));
            shouldSwing = false; // Reset the swing flag after swinging
        }
    }

    @Override
    public void draw(Graphics graphic) {
        healthDisplay.draw(graphic);
        sprite.draw(graphic);
        weaponController.draw(graphic);
    }

    public void heal(int amount) {
        healthDisplay.heal(amount);
    }

    public void swing() {
        shouldSwing = true;
        try {
            Network.stateManager.sendStateByValue(this);
        } catch (Exception e) {
            System.err.println("Failed to send swing state: " + e.getMessage());
        }
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        // Deserialize position
        setX(dataSegments.readInt());
        setY(dataSegments.readInt());

        // Deserialize health
        healthDisplay.setHealth(dataSegments.readInt());

        // Deserialize swing
        int swingDirection = dataSegments.read();
        if (swingDirection != 0) {
            weaponController.swing(SwingDirection.fromInt(swingDirection - 1)); // -1 to convert back to enum ordinal
        }
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        // Serialize position
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());

        // Serialize health
        dataSegments.writeInt(healthDisplay.getHealth());

        // Serialize swing
        if (!shouldSwing) {
            dataSegments.write(0); // Bytes cannot be negative, so 0 for no swing
        } else {
            dataSegments.write(SwingDirection.fromVelo(controller.getVelocity()).ordinal() + 1); // +1 to avoid -1 for no swing
        }
    }

    @Override
    public void onNetworkCreate() throws Exception {
        Renderer.addDrawables(this);
        // Renderer.addDamageable(this);
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        Renderer.removeDrawables(this);
        // Renderer.removeDamageable(this);
    }

    @Override
    public boolean colliding(Collider other) {
        if (other == null) {
            return false;
        }

        return controller.colliding(other);
    }

    @Override
    public boolean colliding(Point point) {
        if (point == null) {
            return false;
        }

        return controller.colliding(point);
    }

}
