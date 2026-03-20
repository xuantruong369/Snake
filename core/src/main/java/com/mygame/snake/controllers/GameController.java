package com.mygame.snake.controllers;

import com.mygame.snake.bots.BaseSolver;
import com.mygame.snake.bots.FloodFill;
import com.mygame.snake.bots.GreedySolver;
import com.mygame.snake.bots.HamiltonSolver;
import com.mygame.snake.bots.PathSolver;
import com.mygame.snake.configs.Config;
import com.mygame.snake.configs.Config.AIMode;
import com.mygame.snake.models.Direc;
import com.mygame.snake.models.Map;
import com.mygame.snake.models.Snake;

/**
 * Controller - Điều khiển logic game
 * Quản lý: Game state, Update logic, AI decisions
 */
public class GameController {
    private Snake snake;
    private Map map;
    private BaseSolver solver;
    private AIMode aiMode;
    
    private float moveTimer = 0;
    private int score = 0;
    private int moves = 0;

    private InputController inputController;
    
    /**
     * Initialize game controller
     */
    public GameController(InputController inputController) {
        this.inputController = inputController;
        initializeGame();
    }
    
    /**
     * Khởi tạo game mới
     */
    private void initializeGame() {
        map = new Map(Config.MAP_ROWS, Config.MAP_COLS);
        snake = new Snake(map);
        
        aiMode = inputController.getAiMode();
        initializeSolver();
        
        // Create food AFTER solver is initialized to ensure table is properly set up
        map.createRandFood();
        
        moveTimer = 0;
        score = 0;
        moves = 0;
    }
    
    /**
     * Khởi tạo AI Solver theo mode
     */
    private void initializeSolver() {
        switch (aiMode) {
            case GREEDY:
                solver = new GreedySolver(snake);
                break;
            case HAMILTON:
                if (map.getNumRows() % 2 == 0 && map.getNumCols() % 2 == 0) {
                    solver = new HamiltonSolver(snake);
                    if (!solver.ready()) initializeGame();
                } else {
                    System.out.println("Hamilton requires even dimensions, using Greedy");
                    solver = new GreedySolver(snake);
                }
                break;
            case PATHFIND:
                solver = new PathSolver(snake);
                break;
            case FLOODFILL:
                solver = new FloodFill(snake);
                break;
            case MANUAL:
            default:
                solver = null;
                break;
        }
    }
    
    /**
     * Update game state (gọi mỗi frame)
     */
    public void update(float deltaTime) {

        if (inputController.getChangeMode())
        {
            initializeGame();
        }

        if (inputController.getReset())
        {
            initializeGame();
        }

        if (inputController.getTogglePause())
        {
            return;
        }

        if (snake.isDead() || snake.getMap().isFull()) {
            // xu lu gameover
            onGameOver();
            return;
        }

        // Tạo thức ăn mới nếu cần
        if (!map.hasFood()) {
            map.createRandFood();
        } 
        
        moveTimer += deltaTime;
        
        if (moveTimer >= Config.MOVE_DELAY) {

            // Xử lý di chuyển
            handleMovement();
            moveTimer = 0;
          
        }
    }
    
    /**
     * Xử lý di chuyển rắn
     */
    private void handleMovement() {
        Direc nextDirec = null;
        
        if (aiMode == AIMode.MANUAL) {
            // Người chơi tự điều khiển (qua InputController)
            nextDirec = inputController.getCurrentDirection();  // Input sẽ gọi moveSnake() trực tiếp
        } else if (solver != null) {
            // AI tự động
            nextDirec = solver.nextDirec();
        }
        
        if (nextDirec != null) {
            moveSnake(nextDirec);
        }
        
        moves++;
    }
    
    /**
     * Di chuyển rắn (gọi từ InputController hoặc AI)
     */
    public void moveSnake(Direc direction) {
        snake.move(direction);
        
        // Cập nhật score khi ăn food
        if (snake.len() > 2) {  // Độ dài thay đổi
            score += 10;
        }
    }
    
    /**
     * Xử lý khi game kết thúc
     */
    private void onGameOver() {
        System.out.println("┌─────────────────┐");
        System.out.println("│   GAME OVER!    │");
        System.out.println("├─────────────────┤");
        System.out.println("│ Score: " + score);
        System.out.println("│ Length: " + snake.len());
        System.out.println("│ Moves: " + moves);
        System.out.println("│ Steps: " + snake.getSteps());
        System.out.println("└─────────────────┘");
    }
    
    /**
     * Reset game
     */
    public void reset() {
        initializeGame();
    }
    
    
    /**
     * Thay đổi AI mode
     */
    public void setAIMode(AIMode mode) {
        aiMode = mode;
        initializeGame();
        initializeSolver();
    }
    
    // ==================== GETTERS ====================
    
    public Snake getSnake() {
        return snake;
    }
    
    public Map getMap() {
        return map;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getMoves() {
        return moves;
    }

    public boolean isPaused()
    {
        return inputController.getTogglePause();
    }
    
    public AIMode getAIMode() {
        return aiMode;
    }
    
    public void dispose() {
        if (solver != null) {
            solver.close();
        }
    }
}
