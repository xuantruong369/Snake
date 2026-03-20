package com.mygame.snake.models;


public class Point {
    /**
     * Point on the game map.
     */
    private PointType type;

    /**
     * Initialize a Point object.
     */
    public Point() {
        this.type = PointType.EMPTY;
    }

    /**
     * Get the type of this point.
     */
    public PointType getType() {
        return type;
    }

    /**
     * Set the type of this point.
     */
    public void setType(PointType type) {
        this.type = type;
    }
}