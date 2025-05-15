package com.engine;

import com.engine.util.Image;

public class Constants {
    public static class PlayerConstants {
        public static final int PLAYER_PIXEL_SIZE = 16;
        public static final int PLAYER_SPRITE_SCALE = 7;
        public static final int PLAYER_WIDTH = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;
        public static final int PLAYER_HEIGHT = PLAYER_SPRITE_SCALE * PLAYER_PIXEL_SIZE;

        public static final Image PLAYER_FRONT_SPRITE = new Image("src/main/resources/front1.spr", PLAYER_SPRITE_SCALE);
        public static final Image PLAYER_BACK_SPRITE = new Image("src/main/resources/back1.spr", PLAYER_SPRITE_SCALE);
        public static final Image PLAYER_LEFT_SPRITE = new Image("src/main/resources/left1.spr", PLAYER_SPRITE_SCALE);
        public static final Image PLAYER_RIGHT_SPRITE = new Image("src/main/resources/right1.spr", PLAYER_SPRITE_SCALE);

        public static final double PLAYER_ACCELERATION = 0.5;
        public static final double PLAYER_MAX_SPEED = 3;
        public static final double PLAYER_FRICTION = 0.8;
    }

    public static class GameConstants {
        public static final int GAME_WIDTH = 640;
        public static final int GAME_HEIGHT = 480;
        public static final String GAME_TITLE = "Game Engine Renderer";
    }

    public static class RendererConstants {
        public static final int THREADS = 1;
        public static final int BUFFERS = 3;
        public static final int FRAME_RATE = 60;
        public static final int FRAME_DELAY = 1000 / FRAME_RATE;
    }

}
