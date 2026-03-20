package com.mygame.snake.models;

import java.util.*;

public class Map {
    /**
     * 2D game map.
     */
    private int numRows;
    private int numCols;
    private int capacity;
    private Point[][] content;
    private Pos food;

    /**
     * Initialize a Map object.
     */
    public Map(int numRows, int numCols) {
        if (numRows < 5 || numCols < 5) {
            throw new IllegalArgumentException("'numRows' and 'numCols' must >= 5");
        }

        this.numRows = numRows;
        this.numCols = numCols;
        this.capacity = (numRows - 2) * (numCols - 2);
        this.content = new Point[numRows][numCols];
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                content[i][j] = new Point();
            }
        }
        reset();
    }

    /**
     * Reset the map to initial state.
     */
    public void reset() {
        food = null;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i == 0 || i == numRows - 1 || j == 0 || j == numCols - 1) {
                    content[i][j].setType(PointType.WALL);
                } else {
                    content[i][j].setType(PointType.EMPTY);
                }
            }
        }
    }

    /**
     * Create a copy of this map.
     */
    public Map copy() {
        Map mapCopy = new Map(numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                mapCopy.content[i][j].setType(content[i][j].getType());
            }
        }
        return mapCopy;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getCapacity() {
        return capacity;
    }

    public Pos getFood() {
        return food;
    }

    /**
     * Return a point on the map.
     * DO NOT directly modify the point type to PointType.FOOD and vice versa.
     * Use addFood() or rmFood() methods instead.
     */
    public Point point(Pos pos) {
        return content[pos.getX()][pos.getY()];
    }

    /**
     * Check if a position is inside the map boundary.
     */
    public boolean isInside(Pos pos) {
        return pos.getX() > 0 && pos.getX() < numRows - 1 &&
               pos.getY() > 0 && pos.getY() < numCols - 1;
    }

    /**
     * Check if a position is empty.
     */
    public boolean isEmpty(Pos pos) {
        return isInside(pos) && point(pos).getType() == PointType.EMPTY;
    }

    /**
     * Check if a position is safe to move to.
     */
    public boolean isSafe(Pos pos) {
        return isInside(pos) && (
            point(pos).getType() == PointType.EMPTY ||
            point(pos).getType() == PointType.FOOD
        );
    }

    /**
     * Check if the map is filled with the snake's bodies.
     */
    public boolean isFull() {
        for (int i = 1; i < numRows - 1; i++) {
            for (int j = 1; j < numCols - 1; j++) {
                PointType type = content[i][j].getType();
                if (type.getValue() < PointType.HEAD_L.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if food exists on the map.
     */
    public boolean hasFood() {
        return food != null;
    }

    /**
     * Remove food from the map.
     */
    public void rmFood() {
        if (hasFood()) {
            point(food).setType(PointType.EMPTY);
            food = null;
        }
    }

    /**
     * Create food at a specific position.
     */
    public Pos createFood(Pos pos) {
        point(pos).setType(PointType.FOOD);
        food = pos;
        return food;
    }

    /**
     * Create food at a random empty position.
     */
    public Pos createRandFood() {
        List<Pos> emptyPos = new ArrayList<>();
        for (int i = 1; i < numRows - 1; i++) {
            for (int j = 1; j < numCols - 1; j++) {
                PointType type = content[i][j].getType();
                if (type == PointType.EMPTY) {
                    emptyPos.add(new Pos(i, j));
                } else if (type == PointType.FOOD) {
                    return null;  // Stop if food exists
                }
            }
        }
        if (!emptyPos.isEmpty()) {
            Random rand = new Random();
            return createFood(emptyPos.get(rand.nextInt(emptyPos.size())));
        }
        return null;
    }
}
