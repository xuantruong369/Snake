package com.mygame.snake.bots;

import java.util.*;

import com.mygame.snake.models.Direc;
import com.mygame.snake.models.Pos;
import com.mygame.snake.models.Snake;

/**
 * Greedy Solver for the snake game.
 */
public class GreedySolver extends BaseSolver {
    private PathSolver pathSolver;

    /**
     * Initialize a GreedySolver object.
     */
    public GreedySolver(Snake snake) {
        super(snake);
        this.pathSolver = new PathSolver(snake);
    }

    @Override
    public Direc nextDirec() {
        // Step 1: Find path to food
        pathSolver.setSnake(snake);
        Deque<Direc> pathToFood = pathSolver.shortestPathToFood();

        if (!pathToFood.isEmpty()) {
            // Step 2: Check if snake can eat food and still have a path to tail
            Snake sCopy = copiedSnake(snake);
            moveSnakePath(sCopy, pathToFood);
            
            if (map.isFull()) {
                return pathToFood.getFirst();
            }

            // Step 3: Check if there's a long path to tail after eating food
            pathSolver.setSnake(sCopy);
            Deque<Direc> pathToTail = pathSolver.longestPathToTail();
            
            if (pathToTail.size() > 1) {
                return pathToFood.getFirst();
            }
        }

        // Step 4: Find longest path to tail
        pathSolver.setSnake(snake);
        Deque<Direc> pathToTail = pathSolver.longestPathToTail();
        
        if (pathToTail.size() > 1) {
            return pathToTail.getFirst();
        }

        // Step 5: Move away from food to maximize distance
        Pos head = snake.head();
        Direc direc = snake.getDirec();
        int maxDist = -1;
        
        for (Pos adj : head.allAdj()) {
            if (map.isSafe(adj)) {
                int dist = Pos.manhattanDist(adj, map.getFood());
                if (dist > maxDist) {
                    maxDist = dist;
                    direc = head.direcTo(adj);
                }
            }
        }
        
        return direc;
    }

    /**
     * Create a copy of the snake and move it along a path.
     */
    private Snake copiedSnake(Snake originalSnake) {
        // Create a deep copy of the snake and its map
        Snake copy = new Snake(originalSnake.getMap().copy());
        copy.setDead(originalSnake.isDead());
        copy.setDirecNext(originalSnake.getDirecNext());
        // Copy bodies and other states
        return copy;
    }

    /**
     * Move snake along a path.
     */
    private void moveSnakePath(Snake snake, Deque<Direc> path) {
        for (Direc direc : path) {
            snake.move(direc);
        }
    }

    @Override
    public boolean ready() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ready'");
    }
}
