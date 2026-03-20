package com.mygame.snake.bots;

import java.util.*;
import com.mygame.snake.models.Direc;
import com.mygame.snake.models.Pos;
import com.mygame.snake.models.Snake;

/**
 * Table cell for Hamilton cycle algorithm.
 */
class HamiltonTableCell {
    public Integer idx;
    public Direc direc;

    public HamiltonTableCell() {
        reset();
    }

    public void reset() {
        idx = null;
        direc = Direc.NONE;
    }

    @Override
    public String toString() {
        return "{ idx: " + idx + "  direc: " + direc + " }";
    }
}

/**
 * Hamilton Solver for the snake game.
 */
public class HamiltonSolver extends BaseSolver {
    private boolean shortcuts;
    private PathSolver pathSolver;
    private HamiltonTableCell[][] table;
    //
    private boolean isFull = false;
    //

    /**
     * Initialize a HamiltonSolver object.
     */
    public HamiltonSolver(Snake snake) {
        this(snake, true);
    }

    /**
     * Initialize a HamiltonSolver object with shortcuts option.
     */
    public HamiltonSolver(Snake snake, boolean shortcuts) {
        super(snake);
        
        if (map.getNumRows() % 2 != 0 || map.getNumCols() % 2 != 0) {
            throw new IllegalArgumentException("numRows and numCols must be even.");
        }

        this.shortcuts = shortcuts;
        this.pathSolver = new PathSolver(snake);
        this.table = new HamiltonTableCell[map.getNumRows()][map.getNumCols()];
        
        for (int i = 0; i < map.getNumRows(); i++) {
            for (int j = 0; j < map.getNumCols(); j++) {
                table[i][j] = new HamiltonTableCell();
            }
        }
        
        buildCycle();
        //
        
        System.out.println("MapSize: " + snake.getMap().getCapacity());
        //
    }

    public HamiltonTableCell[][] getTable() {
        return table;
    }

    @Override
    public Direc nextDirec() {
        Pos head = snake.head();
        Direc nextDirec = table[head.getX()][head.getY()].direc;

        // Take shortcuts when the snake is not too long
        if (shortcuts && snake.len() < 0.5 * map.getCapacity()) {
            Deque<Direc> path = pathSolver.shortestPathToFood();
            
            if (!path.isEmpty()) {
                Pos tail = snake.tail();
                Pos nxt = head.adj(path.getFirst());
                Pos food = map.getFood();
                
                Integer tailIdx = table[tail.getX()][tail.getY()].idx;
                Integer headIdx = table[head.getX()][head.getY()].idx;
                Integer nxtIdx = table[nxt.getX()][nxt.getY()].idx;
                Integer foodIdx = table[food.getX()][food.getY()].idx;
                
                // Exclude one exception
                if (!(path.size() == 1 && Math.abs(foodIdx - tailIdx) == 1)) {
                    int headIdxRel = relativeDist(tailIdx, headIdx, map.getCapacity());
                    int nxtIdxRel = relativeDist(tailIdx, nxtIdx, map.getCapacity());
                    int foodIdxRel = relativeDist(tailIdx, foodIdx, map.getCapacity());
                    
                    if (nxtIdxRel > headIdxRel && nxtIdxRel <= foodIdxRel) {
                        nextDirec = path.getFirst();
                    }
                }
            }
        }

        return nextDirec;
    }

    /**
     * Build a hamiltonian cycle on the map.
     */
    private void buildCycle() {
        Deque<Direc> path = pathSolver.longestPathToTail();
        //
        System.out.println("--------------------------------------");
        System.out.println("PathSize: " + path.size());
        // System.out.println("PathList: " + path);
        System.out.println(isFull);
        if (path.size() == snake.getMap().getCapacity() - 1) isFull = true;
        System.out.println(isFull);
        //
        Pos cur = snake.head();
        int cnt = 0;
        
        for (Direc direc : path) {
            table[cur.getX()][cur.getY()].idx = cnt;
            table[cur.getX()][cur.getY()].direc = direc;
            cur = cur.adj(direc);
            cnt++;
        }
        
        // Process snake bodies
        cur = snake.tail();
        for (int i = 0; i < snake.len() - 1; i++) {
            table[cur.getX()][cur.getY()].idx = cnt;
            table[cur.getX()][cur.getY()].direc = snake.getDirec();
            cur = cur.adj(snake.getDirec());
            cnt++;
        }
    }

    /**
     * Calculate relative distance.
     */
    private int relativeDist(int ori, int x, int size) {
        if (ori > x) {
            x += size;
        }
        return x - ori;
    }

    @Override
    public boolean ready() {
        return isFull;
    }
}
