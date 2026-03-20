package com.mygame.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.mygame.snake.controllers.GameController;
import com.mygame.snake.controllers.InputController;
import com.mygame.snake.views.GameRenderer;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainGame extends ApplicationAdapter {
    private GameController gameController;
    private InputController inputController;
    private GameRenderer gameRenderer;

    @Override
    public void create() {
        System.out.println("SNAKE GAME");
        
        // Khởi tạo MVC
        inputController = new InputController();
        gameController = new GameController(inputController);
        gameRenderer = new GameRenderer(gameController);
        
        System.out.println("Game initialized!");
    }

    @Override
    public void render() {

        // ==================== INPUT (Model) ====================
            inputController.handleInput();  // Nhập từ người dùng 

        // ==================== LOGIC (Controller + Model) ====================
            float deltaTime = Gdx.graphics.getDeltaTime();
        
            gameController.update(deltaTime);   // Cập nhật game logic
        
        // ==================== RENDER (View) ====================
            // Gdx.gl.glClearColor(0.05f, 0.05f, 0.05f, 1);
            // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            gameRenderer.render();  // Vẽ toàn bộ game
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
        gameController.dispose();
    }
}
