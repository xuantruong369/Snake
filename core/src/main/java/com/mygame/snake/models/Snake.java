package com.mygame.snake.models;

import java.util.*;

public class Snake {
    /**
     * Snake of the game.
     */
    private Map map;
    private Direc initDirec;
    private List<Pos> initBodies;
    private List<PointType> initTypes;
    private int steps;
    private boolean dead;
    private Direc direc;
    private Direc direcNext;
    private Deque<Pos> bodies;

    /**
     * Initialize a Snake object.
     */
    public Snake(Map gameMap, Direc initDirec, List<Pos> initBodies, List<PointType> initTypes) {
        this.map = gameMap;
        this.initDirec = initDirec;
        this.initBodies = initBodies;
        this.initTypes = initTypes;
        reset(false);
    }

    /**
     * Initialize a Snake object with required map parameter.
     */
    public Snake(Map gameMap) {
        this(gameMap, null, null, null);
    }

    /**
     * Reset the snake to initial state.
     */
    public void reset(boolean resetMap) {
        boolean randInit = false;
        Random rand = new Random();

        if (initDirec == null) {  // Randomly initialize
            randInit = true;
            int headRow = 2 + rand.nextInt(map.getNumRows() - 4);
            int headCol = 2 + rand.nextInt(map.getNumCols() - 4);
            Pos head = new Pos(headRow, headCol);

            List<Direc> dirs = Arrays.asList(Direc.LEFT, Direc.UP, Direc.RIGHT, Direc.DOWN);
            initDirec = dirs.get(rand.nextInt(dirs.size()));
            
            initBodies = new ArrayList<>();
            initBodies.add(head);
            initBodies.add(head.adj(Direc.opposite(initDirec)));

            initTypes = new ArrayList<>();
            switch (initDirec) {
                case LEFT:
                    initTypes.add(PointType.HEAD_L);
                    break;
                case UP:
                    initTypes.add(PointType.HEAD_U);
                    break;
                case RIGHT:
                    initTypes.add(PointType.HEAD_R);
                    break;
                case DOWN:
                    initTypes.add(PointType.HEAD_D);
                    break;
                default:
                    break;
            }
            
            if (initDirec == Direc.LEFT || initDirec == Direc.RIGHT) {
                initTypes.add(PointType.BODY_HOR);
            } else if (initDirec == Direc.UP || initDirec == Direc.DOWN) {
                initTypes.add(PointType.BODY_VER);
            }
        }

        steps = 0;
        dead = false;
        direc = initDirec;
        direcNext = Direc.NONE;
        bodies = new LinkedList<>(initBodies);




        if (resetMap) {
            map.reset();
        }
        
        for (int i = 0; i < initBodies.size(); i++) {
            map.point(initBodies.get(i)).setType(initTypes.get(i));
        }

        if (randInit) {
            initDirec = null;
            initBodies = null;
            initTypes = null;
        }
    }

    /**
     * Create a copy of this snake and its map.
     */
    public Map copy() {
        Map mapCopy = map.copy();
        // Additional copying logic would go here if needed
        return mapCopy;
    }

    public Map getMap() {
        return map;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Direc getDirec() {
        return direc;
    }

    public Direc getDirecNext() {
        return direcNext;
    }

    public void setDirecNext(Direc direcNext) {
        this.direcNext = direcNext;
    }

    public Deque<Pos> getBodies() {
        return bodies;
    }

    /**
     * Get the length of the snake.
     */
    public int len() {
        return bodies.size();
    }

    /**
     * Get the head position of the snake.
     */
    public Pos head() {
        if (bodies.isEmpty()) return null;
        return bodies.getFirst();
    }

    /**
     * Get the tail position of the snake.
     */
    public Pos tail() {
        if (bodies.isEmpty()) return null;
        return bodies.getLast();
    }

    /**
     * Move the snake along a path.
     */
    public void movePath(List<Direc> path) {
        for (Direc direc : path) {
            move(direc);
        }
    }

    /**
     * Move the snake in a direction.
     */
    public void move(Direc newDirec) {
        if (newDirec != null) {
            direcNext = newDirec;
        }

        if (dead || direcNext == Direc.NONE || map.isFull() ||
            direcNext == Direc.opposite(direc)) {
            return;
        }

        PointType[] newTypes = getNewTypes();
        PointType oldHeadType = newTypes[0];
        PointType newHeadType = newTypes[1];

        //
        PointType currentType = map.point(head()).getType();
        //
        map.point(head()).setType(oldHeadType);
        Pos newHead = head().adj(direcNext);
        
        // Kiểm tra an toàn trước khi thêm
        if (!map.isSafe(newHead)) {
            //
            map.point(head()).setType(currentType);
            //
            dead = true;
            return;
        }

        bodies.addFirst(newHead);

        if (map.point(newHead).getType() == PointType.FOOD) {
            map.rmFood();
        } else {
            rmTail();
        }

        map.point(newHead).setType(newHeadType);
        direc = direcNext;
        steps++;
    }

    /**
     * Remove the tail of the snake.
     */
    private void rmTail() {
        map.point(tail()).setType(PointType.EMPTY);
        bodies.removeLast();
    }

    /**
     * Determine the new types for head and body based on direction change.
     */
    private PointType[] getNewTypes() {
        PointType oldHeadType = null;
        PointType newHeadType = null;

        // Determine new head type
        switch (direcNext) {
            case LEFT:
                newHeadType = PointType.HEAD_L;
                break;
            case UP:
                newHeadType = PointType.HEAD_U;
                break;
            case RIGHT:
                newHeadType = PointType.HEAD_R;
                break;
            case DOWN:
                newHeadType = PointType.HEAD_D;
                break;
            default:
                break;
        }

        // Determine old head type based on direction change
        if ((direc == Direc.LEFT && direcNext == Direc.LEFT) ||
            (direc == Direc.RIGHT && direcNext == Direc.RIGHT)) {
            oldHeadType = PointType.BODY_HOR;
        } else if ((direc == Direc.UP && direcNext == Direc.UP) ||
                   (direc == Direc.DOWN && direcNext == Direc.DOWN)) {
            oldHeadType = PointType.BODY_VER;
        } else if ((direc == Direc.RIGHT && direcNext == Direc.UP) ||
                   (direc == Direc.DOWN && direcNext == Direc.LEFT)) {
            oldHeadType = PointType.BODY_LU;
        } else if ((direc == Direc.LEFT && direcNext == Direc.UP) ||
                   (direc == Direc.DOWN && direcNext == Direc.RIGHT)) {
            oldHeadType = PointType.BODY_UR;
        } else if ((direc == Direc.LEFT && direcNext == Direc.DOWN) ||
                   (direc == Direc.UP && direcNext == Direc.RIGHT)) {
            oldHeadType = PointType.BODY_RD;
        } else if ((direc == Direc.RIGHT && direcNext == Direc.DOWN) ||
                   (direc == Direc.UP && direcNext == Direc.LEFT)) {
            oldHeadType = PointType.BODY_DL;
        }

        return new PointType[] { oldHeadType, newHeadType };
    }
}
