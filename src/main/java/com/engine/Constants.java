package com.engine;

import java.awt.Point;

import com.engine.rendering.drawings.Animateable;
import com.engine.rendering.drawings.InstanceAnimateable;
import com.engine.util.Image;
import com.engine.util.PointConfig;

public class Constants {
    public static class PlayerConstants {
        public static final int PLAYER_PIXEL_SIZE = 16;
        public static final int PLAYER_SPRITE_SCALE = 7;
        public static final int PLAYER_WIDTH = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;
        public static final int PLAYER_HEIGHT = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;
        
        public static Image getBlank() {
            return new Image();
        }

        public static Image getPlayerDownWalkOne() {
            return new Image("src/main/resources/playerdownwalk1.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerDownWalkTwo() {
            return new Image("src/main/resources/playerdownwalk2.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerUpWalkOne() {
            return new Image("src/main/resources/playerupwalk1.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerUpWalkTwo() {
            return new Image("src/main/resources/playerupwalk2.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerLeftWalkOne() {
            return new Image("src/main/resources/playerleftwalk.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerRightWalkOne() {
            return new Image("src/main/resources/playerrightwalk.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerDownSprite() {
            return new Image("src/main/resources/playerdownstand.spr", PLAYER_SPRITE_SCALE);
        }
        
        public static Image getPlayerUpSprite() {
            return new Image("src/main/resources/playerupstand.spr", PLAYER_SPRITE_SCALE);
        }
        
        public static Image getPlayerLeftSprite() {
            return new Image("src/main/resources/playerleftstand.spr", PLAYER_SPRITE_SCALE);
        }
        
        public static Image getPlayerRightSprite() {
            return new Image("src/main/resources/playerrightstand.spr", PLAYER_SPRITE_SCALE);
        }

        public static final double PLAYER_ACCELERATION = 0.5;
        public static final double PLAYER_MAX_SPEED = 3;
        public static final double PLAYER_FRICTION = 0.8;
    }

    public static class WeaponConstants {
        public static final int WEAPON_PIXEL_SIZE = 16;
        public static final int WEAPON_SPRITE_SCALE = 7;
        public static final int WEAPON_WIDTH = WEAPON_SPRITE_SCALE * WEAPON_PIXEL_SIZE;
        public static final int WEAPON_HEIGHT = WEAPON_SPRITE_SCALE * WEAPON_PIXEL_SIZE;

        public static Image getBlank() {
            return new Image();
        }

        public static InstanceAnimateable getSwing(PointConfig anchor, int angle) {
            return new InstanceAnimateable(anchor, 5, getBlank(),
                new Image("src/main/resources/rightswing1.spr", WEAPON_SPRITE_SCALE, angle),
                new Image("src/main/resources/rightswing2.spr", WEAPON_SPRITE_SCALE, angle),
                new Image("src/main/resources/rightswing3.spr", WEAPON_SPRITE_SCALE, angle),
                new Image("src/main/resources/rightswing4.spr", WEAPON_SPRITE_SCALE, angle),
                new Image("src/main/resources/rightswing5.spr", WEAPON_SPRITE_SCALE, angle));
        }
    }

    public static class GameConstants {
        public static final int GAME_WIDTH = 640;
        public static final int GAME_HEIGHT = 480;
        public static final String GAME_TITLE = "Game Engine Renderer";
        
        public static Image getRockSprite() {
            return new Image("src/main/resources/rock.spr", PlayerConstants.PLAYER_SPRITE_SCALE);
        }

        public static final int HEALTH_SPRITE_SCALE = 3;

        public static final Image HEART_SPRITE = new Image("src/main/resources/heart.spr",
                HEALTH_SPRITE_SCALE);
        public static final Image EMPTY_HEALTH_IMAGE = new Image("src/main/resources/emptyheart.spr",
                HEALTH_SPRITE_SCALE);
    }

    public static class RendererConstants {
        public static final int THREADS = 1;
        public static final int BUFFERS = 3;
        public static final int FRAME_RATE = 60;
        public static final int FRAME_DELAY = 1000 / FRAME_RATE;
    }

}
