package com.mygame.snake.configs;
/**
 * Cấu hình chung cho game
 */
public class Config {
    // Game settings
    public static final int MAP_ROWS = 16;
    public static final int MAP_COLS = 16;
    public static final int CELL_SIZE = 40;  // pixels
    public static final int UI_COLS = 6;

    // Window settings
    public static final int WINDOW_WIDTH = (MAP_COLS + UI_COLS) * CELL_SIZE;
    public static final int WINDOW_HEIGHT = MAP_ROWS * CELL_SIZE;
    public static final String WINDOW_TITLE = "Snake";
    
    // Game speed
    public static final float MOVE_DELAY = 0.15f;  // seconds
    // public static final float MOVE_DELAY = 0.01f;
    
    // Colors (RGBA)
    public static class Colors {
        public static final float[] WALL = {0.2f, 0.2f, 0.2f, 1f};
        public static final float[] EVEN = {0.6f, 0.8f, 0.4f, 1f};
        public static final float[] ODD = {0.47f, 0.67f, 0.33f, 1f};
        public static final float[] EMPTY = {0.1f, 0.1f, 0.1f, 1f};
        public static final float[] FOOD = {1f, 0.2f, 0.2f, 1f};
        public static final float[] HEAD = {0.2f, 1f, 0.2f, 1f};
        public static final float[] BODY = {0f, 0.8f, 0f, 1f};
        public static final float[] BACKGROUND = {0.05f, 0.05f, 0.05f, 1f};
    }
    
    public static class Textures {
        public static final String FOOD = "images/apple.png";

        public static final String BODY_BOTTOM_LEFT = "images/body_bottomleft.png";
        public static final String BODY_BOTTOM_RIGHT = "images/body_bottomright.png";
        public static final String BODY_HORIZONTAL = "images/body_horizontal.png";
        public static final String BODY_TOP_LEFT = "images/body_topleft.png";
        public static final String BODY_TOP_RIGHT = "images/body_topright.png";
        public static final String BODY_VERTICAL = "images/body_vertical.png";

        public static final String HEAD_DOWN = "images/head_down.png";
        public static final String HEAD_LEFT = "images/head_left.png";
        public static final String HEAD_RIGHT = "images/head_right.png";
        public static final String HEAD_UP = "images/head_up.png";

        public static final String TAIL_DOWN = "images/tail_down.png";
        public static final String TAIL_LEFT = "images/tail_left.png";
        public static final String TAIL_RIGHT = "images/tail_right.png";
        public static final String TAIL_UP = "images/tail_up.png";
        
    }
    
    // AI Mode
    public enum AIMode {
        MANUAL,      // Người chơi điều khiển
        GREEDY,      // AI tham lam
        HAMILTON,    // AI chu trình Hamilton
        PATHFIND,   // AI tìm đường
        FLOODFILL
    }
    
    public static AIMode CURRENT_AI_MODE = AIMode.MANUAL;
}
