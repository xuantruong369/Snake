package com.mygame.snake.views;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygame.snake.configs.Config;
import com.mygame.snake.controllers.GameController;
import com.mygame.snake.models.Map;
import com.mygame.snake.models.Snake;


/**
 * View - Renderer cho game
 * Quản lý tất cả graphics rendering
 */
public class GameRenderer {
    private GameController gameController;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    
    private MapRenderer mapRenderer;
    private SnakeRenderer snakeRenderer;
    private FoodRenderer foodRenderer;
    
    public GameRenderer(GameController gameController) {
        this.gameController = gameController;
        this.shapeRenderer = new ShapeRenderer();
        this.spriteBatch = new SpriteBatch();
        
        this.font = new BitmapFont();
        
        this.mapRenderer = new MapRenderer();
        this.snakeRenderer = new SnakeRenderer();
        foodRenderer = new FoodRenderer();
    }
    
    /**
     * Vẽ toàn bộ game
     */
    public void render() {
        Map map = gameController.getMap();
        Snake snake = gameController.getSnake();
        ScreenUtils.clear(Color.BLACK);
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Vẽ nền
        renderBackground();
        
        // Vẽ bản đồ
        mapRenderer.render(shapeRenderer, map);

        shapeRenderer.end();

        spriteBatch.begin();
        spriteBatch.enableBlending();

        // Vẽ food
        foodRenderer.render(spriteBatch, map);
        
        // Vẽ rắn
        snakeRenderer.render(spriteBatch, snake);
         
        // Vẽ UI
        renderUI();
        spriteBatch.end();
    }
    
    /**
     * Vẽ nền
     */
    private void renderBackground() {
       
        
        float[] bgColor = Config.Colors.BACKGROUND;
        shapeRenderer.setColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);
        shapeRenderer.rect(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        
        
    }
    
    /**
     * Vẽ UI (Score, Mode, Instructions)
     */
    private void renderUI() {
        
        // Vẽ chữ
        font.setColor(Color.WHITE);
        
        // Score
        font.draw(spriteBatch, "Score: " + gameController.getScore(), 
                Config.MAP_ROWS*Config.CELL_SIZE, (Config.MAP_ROWS - 1)*Config.CELL_SIZE);
        
        // Snake length
        font.draw(spriteBatch, "Length: " + gameController.getSnake().len() + "/" + gameController.getMap().getCapacity(), 
                Config.MAP_ROWS*Config.CELL_SIZE, (Config.MAP_ROWS - 2)*Config.CELL_SIZE);
        
        // Moves
        font.draw(spriteBatch, "Moves: " + gameController.getMoves(), 
                Config.MAP_ROWS*Config.CELL_SIZE, (Config.MAP_ROWS - 3)*Config.CELL_SIZE);
        
        // AI Mode
        font.draw(spriteBatch, "Mode: " + gameController.getAIMode().toString(), 
                Config.MAP_ROWS*Config.CELL_SIZE, (Config.MAP_ROWS - 4)*Config.CELL_SIZE);
        
        // Game Over warning
        if (gameController.getSnake().isDead()) {
            font.setColor(Color.RED);
            font.draw(spriteBatch, "GAME OVER! Press R to restart", 
                     Config.WINDOW_WIDTH / 2 - 100, Config.WINDOW_HEIGHT / 2);
        }
        
        // Pause info
        if (gameController.isPaused()) {
            font.setColor(Color.YELLOW);
            font.draw(spriteBatch, "PAUSED", 
                     Config.WINDOW_WIDTH / 2 - 50, Config.WINDOW_HEIGHT / 2 + 30);
        }
        
        // Controls info
        font.setColor(Color.LIGHT_GRAY);
        font.draw(spriteBatch, "SPACE=Pause  R=Reset  G=Greedy  H=Hamilton  M=Manual", 
                Config.CELL_SIZE, Config.CELL_SIZE / 2);
        
    }
    
    /**
     * Dispose resources
     */
    public void dispose() {
        shapeRenderer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
