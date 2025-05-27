package com.engine;

import com.engine.util.Image;

public class Constants {
    public static class PlayerConstants {
        public static final int PLAYER_PIXEL_SIZE = 16;
        public static final int PLAYER_SPRITE_SCALE = 7;
        public static final int PLAYER_WIDTH = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;
        public static final int PLAYER_HEIGHT = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;
        
        public static Image getBlank() {
            return new Image();
        }

        public static Image getPlayerFrontWalkOne() {
            return new Image("src/main/resources/playerfrontwalk1.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerFrontWalkTwo() {
            return new Image("src/main/resources/playerfrontwalk2.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerBackWalkOne() {
            return new Image("src/main/resources/playerbackwalk1.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerBackWalkTwo() {
            return new Image("src/main/resources/playerbackwalk2.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerLeftWalkSprite() {
            return new Image("src/main/resources/playerleftwalk.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerRightWalkSprite() {
            return new Image("src/main/resources/playerrightwalk.spr", PLAYER_SPRITE_SCALE);
        }

        public static Image getPlayerFrontSprite() {
            return new Image("src/main/resources/playerfrontstand.spr", PLAYER_SPRITE_SCALE);
        }
        
        public static Image getPlayerBackSprite() {
            return new Image("src/main/resources/playerbackstand.spr", PLAYER_SPRITE_SCALE);
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

        public static Image getLeftSwing1() {
            return new Image("src/main/resources/leftswing1.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getLeftSwing2() {
            return new Image("src/main/resources/leftswing2.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getLeftSwing3() {
            return new Image("src/main/resources/leftswing3.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getLeftSwing4() {
            return new Image("src/main/resources/leftswing4.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getLeftSwing5() {
            return new Image("src/main/resources/leftswing5.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getRightSwing1() {
            return new Image("src/main/resources/rightswing1.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getRightSwing2() {
            return new Image("src/main/resources/rightswing2.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getRightSwing3() {
            return new Image("src/main/resources/rightswing3.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getRightSwing4() {
            return new Image("src/main/resources/rightswing4.spr", WEAPON_SPRITE_SCALE);
        }

        public static Image getRightSwing5() {
            return new Image("src/main/resources/rightswing5.spr", WEAPON_SPRITE_SCALE);
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
