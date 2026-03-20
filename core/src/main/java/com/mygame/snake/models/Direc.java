package com.mygame.snake.models;

public enum Direc {
    NONE(0),
    LEFT(1),
    UP(2),
    RIGHT(3),
    DOWN(4);

    private final int value;

    Direc(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Direc opposite(Direc direc) {
        switch (direc) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                return NONE;
        }
    }
}
