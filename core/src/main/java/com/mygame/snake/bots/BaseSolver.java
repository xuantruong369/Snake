package com.mygame.snake.bots;

import com.mygame.snake.models.Direc;
import com.mygame.snake.models.Map;
import com.mygame.snake.models.Snake;

public abstract class BaseSolver {
    protected Snake snake;
    protected Map map;

    /**
     * Initialize a BaseSolver object.
     */
    public BaseSolver(Snake snake) {
        this.snake = snake;
        this.map = snake.getMap();
    }

    public Map getMap() {
        return map;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
        this.map = snake.getMap();
    }

    /**
     * Generate the next direction of the snake.
     */
    public abstract Direc nextDirec();

    public abstract boolean ready();

    /**
     * Release resources.
     */
    public void close() {
        // Override in subclasses if needed
    }
}
