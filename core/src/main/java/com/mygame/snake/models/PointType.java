package com.mygame.snake.models;

public enum PointType {
    EMPTY(0),
    WALL(1),
    FOOD(2),
    HEAD_L(100),
    HEAD_U(101),
    HEAD_R(102),
    HEAD_D(103),
    BODY_LU(104),
    BODY_UR(105),
    BODY_RD(106),
    BODY_DL(107),
    BODY_HOR(108),
    BODY_VER(109),
    //
    TAIL_RIGHT(110),
    TAIL_LEFT(111),
    TAIL_UP(112),
    TAIL_DOWN(113);
    //

    private final int value;

    PointType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
