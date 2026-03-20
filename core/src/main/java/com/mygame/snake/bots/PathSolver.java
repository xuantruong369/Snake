package com.mygame.snake.bots;

import java.util.*;

import com.mygame.snake.models.Direc;
import com.mygame.snake.models.PointType;
import com.mygame.snake.models.Pos;
import com.mygame.snake.models.Snake;
/**
 * Table cell for path solver.
 */
class PathTableCell {
    public Pos parent;
    public int dist;
    public boolean visit;

    public PathTableCell() {
        reset();
    }

    public void reset() {
        parent = null;
        dist = Integer.MAX_VALUE;
        visit = false;
    }

    @Override
    public String toString() {
        return "{ dist: " + dist + "  parent: " + parent + "  visit: " + visit + " }";
    }
}

/**
 * Path Solver for finding shortest and longest paths.
 */
public class PathSolver extends BaseSolver {
    private PathTableCell[][] table;

    /**
     * Initialize a PathSolver object.
     */
    public PathSolver(Snake snake) {
        super(snake);
        this.table = new PathTableCell[snake.getMap().getNumRows()][snake.getMap().getNumCols()];
        
        for (int i = 0; i < snake.getMap().getNumRows(); i++) {
            for (int j = 0; j < snake.getMap().getNumCols(); j++) {
                table[i][j] = new PathTableCell();
            }
        }
    }

    public PathTableCell[][] getTable() {
        return table;
    }

    /**
     * Find the shortest path to food.
     */
    public Deque<Direc> shortestPathToFood() {
        return pathTo(map.getFood(), "shortest");
    }

    /**
     * Find the longest path to the snake's tail.
     */
    public Deque<Direc> longestPathToTail() {
        return pathTo(snake.tail(), "longest");
    }

    /**
     * Find path to destination (shortest or longest).
     */
    public Deque<Direc> pathTo(Pos des, String pathType) {
        PointType oriType = map.point(des).getType();
        map.point(des).setType(PointType.EMPTY);
        
        Deque<Direc> path;
        if ("shortest".equals(pathType)) {
            path = shortestPathTo(des);
        } else if ("longest".equals(pathType)) {
            path = longestPathTo(des);
        } else {
            path = new LinkedList<>();
        }
        
        map.point(des).setType(oriType);  // Restore origin type
        return path;
    }

    /**
     * Find the shortest path from the snake's head to the destination.
     */
    public Deque<Direc> shortestPathTo(Pos des) {
        resetTable();

        Pos head = snake.head();
        table[head.getX()][head.getY()].dist = 0;
        Deque<Pos> queue = new LinkedList<>();
        queue.add(head);

        while (!queue.isEmpty()) {
            Pos cur = queue.removeFirst();
            if (cur.equals(des)) {
                return buildPath(head, des);
            }

            // Arrange the order of traverse to make the path as straight as possible
            Direc firstDirec;
            if (cur.equals(head)) {
                firstDirec = snake.getDirec();
            } else {
                firstDirec = table[cur.getX()][cur.getY()].parent.direcTo(cur);
            }
            
            List<Pos> adjs = cur.allAdj();
            Collections.shuffle(adjs);
            
            for (int i = 0; i < adjs.size(); i++) {
                if (firstDirec == cur.direcTo(adjs.get(i))) {
                    Collections.swap(adjs, 0, i);
                    break;
                }
            }

            // Traverse adjacent positions
            for (Pos pos : adjs) {
                if (isValid(pos)) {
                    PathTableCell adjCell = table[pos.getX()][pos.getY()];
                    if (adjCell.dist == Integer.MAX_VALUE) {
                        adjCell.parent = cur;
                        adjCell.dist = table[cur.getX()][cur.getY()].dist + 1;
                        queue.add(pos);
                    }
                }
            }
        }

        return new LinkedList<>();
    }

    /**
     * Find the longest path from the snake's head to the destination.
     */
    //des = tail snake 
    public Deque<Direc> longestPathTo(Pos des) {
        Deque<Direc> path = shortestPathTo(des);
        //
        System.out.println("Short head to tail: " + path);
        //
        if (path.isEmpty()) {
            return new LinkedList<>();
        }

        resetTable();
        Pos cur = snake.head();
        Pos head = cur;

        // Set all positions on the shortest path to 'visited'
        table[cur.getX()][cur.getY()].visit = true;
        for (Direc direc : path) {
            cur = cur.adj(direc);
            table[cur.getX()][cur.getY()].visit = true;
        }

        // Extend the path between each pair of the positions
        int idx = 0;
        cur = head;
        
        while (true) {
            Direc curDirec = path.getFirst();
            // Get the direc at index idx
            Iterator<Direc> iter = path.iterator();
            for (int i = 0; i < idx; i++) {
                iter.next();
            }
            curDirec = iter.next();
            
            Pos nxt = cur.adj(curDirec);

            List<Direc> tests = new ArrayList<>();
            if (curDirec == Direc.LEFT || curDirec == Direc.RIGHT) {
                tests.add(Direc.UP);
                tests.add(Direc.DOWN);
            } else if (curDirec == Direc.UP || curDirec == Direc.DOWN) {
                tests.add(Direc.LEFT);
                tests.add(Direc.RIGHT);
            }

            boolean extended = false;
            for (Direc testDirec : tests) {
                Pos curTest = cur.adj(testDirec);
                Pos nxtTest = nxt.adj(testDirec);
                
                if (isValid(curTest) && isValid(nxtTest)) {
                    table[curTest.getX()][curTest.getY()].visit = true;
                    table[nxtTest.getX()][nxtTest.getY()].visit = true;
                    
                    // Insert into deque at position idx
                    List<Direc> tempList = new ArrayList<>(path);
                    tempList.add(idx, testDirec);
                    tempList.add(idx + 2, Direc.opposite(testDirec));
                    path = new LinkedList<>(tempList);
                    
                    extended = true;
                    break;
                }
            }

            if (!extended) {
                cur = nxt;
                idx++;
                if (idx >= path.size()) {
                    break;
                }
            }
        }

        return path;
    }

    /**
     * Generate the next direction of the snake.
     */
    @Override
    public Direc nextDirec() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Reset the table.
     */
    private void resetTable() {
        for (PathTableCell[] row : table) {
            for (PathTableCell cell : row) {
                cell.reset();
            }
        }
    }

    /**
     * Build the path from source to destination.
     */
    private Deque<Direc> buildPath(Pos src, Pos des) {
        Deque<Direc> path = new LinkedList<>();
        Pos tmp = des;
        
        while (!tmp.equals(src)) {
            Pos parent = table[tmp.getX()][tmp.getY()].parent;
            path.addFirst(parent.direcTo(tmp));
            tmp = parent;
        }
        
        return path;
    }

    /**
     * Check if a position is valid for traversal.
     */
    private boolean isValid(Pos pos) {
        return map.isSafe(pos) && !table[pos.getX()][pos.getY()].visit;
    }

    @Override
    public boolean ready() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ready'");
    }
}

