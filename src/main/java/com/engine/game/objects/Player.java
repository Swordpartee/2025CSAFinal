package com.engine.game.objects;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.engine.game.collision.Collider;
import com.engine.game.collision.Damageable;
import com.engine.rendering.Renderer;
import com.engine.util.Point;
import com.engine.util.PointConfig;
import com.engine.util.PointController;

public class Player extends PointController implements GameObject, Damageable {
    private final PlayerController controller;
    private final HealthDisplay healthDisplay;
    private final PlayerRenderer sprite;
    private final WeaponController weaponController;

    public Player() {
        super(new PointConfig());

        controller = new PlayerController(getPoint());
        healthDisplay = new HealthDisplay(getPoint().createOffset(0, -80), 5);
        sprite = new PlayerRenderer(getPoint(), controller.getVelocity());
        weaponController = new WeaponController(getPoint(), controller.getVelocity());

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
        weaponController.swing();
    }

    @Override
    public void deserialize(DataInputStream dataSegments) throws Exception {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serialize(DataOutputStream dataSegments) throws Exception {
        dataSegments.writeInt((int) getX());
        dataSegments.writeInt((int) getY());
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onNetworkCreate() throws Exception {
        Renderer.addDrawables(this);
    }

    @Override
    public void onNetworkDestroy() throws Exception {
        Renderer.removeDrawables(this);
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
