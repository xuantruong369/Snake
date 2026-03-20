package com.mygame.snake.models;

import java.util.*;

public class Pos {
    /**
     * Integer coordinate in 2D plane.
     * The origin of the coordinate system is at the top-left corner,
     * with x-axis extends downward and y-axis extends rightward.
     */
    private int x;
    private int y;

    /**
     * Initialize a Pos object with default coordinates (0, 0).
     */
    public Pos() {
        this(0, 0);
    }

    /**
     * Initialize a Pos object with given coordinates.
     */
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Pos(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Pos other = (Pos) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }

    /**
     * Unary plus operator (returns a copy).
     */
    public Pos positive() {
        return new Pos(x, y);
    }

    /**
     * Unary minus operator (negates coordinates).
     */
    public Pos negate() {
        return new Pos(-x, -y);
    }

    /**
     * Add another Pos to this one.
     */
    public Pos add(Pos other) {
        return new Pos(x + other.x, y + other.y);
    }

    /**
     * Subtract another Pos from this one.
     */
    public Pos subtract(Pos other) {
        return this.add(other.negate());
    }

    /**
     * Calculate Manhattan distance between two positions.
     */
    public static int manhattanDist(Pos p1, Pos p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    /**
     * Return the direction of an adjacent Pos relative to this one.
     */
    public Direc direcTo(Pos adjPos) {
        if (x == adjPos.x) {
            int diff = y - adjPos.y;
            if (diff == 1) return Direc.LEFT;
            if (diff == -1) return Direc.RIGHT;
        } else if (y == adjPos.y) {
            int diff = x - adjPos.x;
            if (diff == 1) return Direc.UP;
            if (diff == -1) return Direc.DOWN;
        }
        return Direc.NONE;
    }

    /**
     * Return the adjacent Pos in a given direction.
     */
    public Pos adj(Direc direc) {
        switch (direc) {
            case LEFT:
                return new Pos(x, y - 1);
            case RIGHT:
                return new Pos(x, y + 1);
            case UP:
                return new Pos(x - 1, y);
            case DOWN:
                return new Pos(x + 1, y);
            default:
                return null;
        }
    }

    /**
     * Return a list of all adjacent positions.
     */
    public List<Pos> allAdj() {
        List<Pos> adjs = new ArrayList<>();
        for (Direc direc : Direc.values()) {
            if (direc != Direc.NONE) {
                adjs.add(adj(direc));
            }
        }
        return adjs;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
