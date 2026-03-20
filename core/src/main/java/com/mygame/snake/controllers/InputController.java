package com.mygame.snake.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygame.snake.configs.Config;
import com.mygame.snake.configs.Config.AIMode;
import com.mygame.snake.models.Direc;

/**
 * InputController - Xử lý input từ người chơi
 */
public class InputController {
    private Direc currentDirection = Direc.NONE;
    private AIMode aiMode = Config.CURRENT_AI_MODE;
    private boolean togglePause = false;
    private boolean isReset = false;
    private boolean changeMode = false;

    /**
     * Xử lý input mỗi frame
     */
    public void handleInput() {
        if (aiMode == Config.AIMode.MANUAL)
        {
            if (Gdx.input.isKeyJustPressed(Input.Keys.W) && currentDirection != Direc.DOWN)
            {
                currentDirection = Direc.UP;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.S) && currentDirection != Direc.UP)
            {
                currentDirection = Direc.DOWN;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.A) && currentDirection != Direc.RIGHT)
            {
                currentDirection = Direc.LEFT;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && currentDirection != Direc.LEFT)
            {
                currentDirection = Direc.RIGHT;
            }
        }
        
        // Các phím điều khiển khác
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            togglePause = !togglePause;
            System.out.println("PAUSE: " + togglePause);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            isReset = true;
            System.out.println("Game Reset!");
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.G))
        {
            aiMode = AIMode.GREEDY;
            changeMode = true;
            System.out.println("Switched to Greedy mode");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.H))
        {
            aiMode = AIMode.HAMILTON;
            changeMode = true;
            System.out.println("Switched to Hamilton mode");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            aiMode = AIMode.MANUAL;
            changeMode = true;
            System.out.println("Switched to Manual mode");
        }
    }
    
    public AIMode getAiMode() {
        return aiMode;
    }

    public Direc getCurrentDirection() {
        return currentDirection;
    }

    public boolean getTogglePause() {
        return togglePause;
    }

    public boolean getReset()
    {
        if (isReset)
        {
            currentDirection = Direc.NONE;
            isReset = false;
            return true;
        }
        return false;
    }

    public boolean getChangeMode()
    {
        if (changeMode)
        {
            currentDirection = Direc.NONE;
            changeMode = false;
            return true;
        }
        return false;
    }

    public boolean ischangeMode() {
        return changeMode;
    }

    public void setAiMode(AIMode aiMode) {
        this.aiMode = aiMode;
    }

    public void setCurrentDirection(Direc currentDirection) {
        this.currentDirection = currentDirection;
    }


}
