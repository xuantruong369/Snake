package com.mygame.snake.views;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygame.snake.configs.Config;
import com.mygame.snake.models.Map;
import com.mygame.snake.models.PointType;
import com.mygame.snake.models.Pos;

/**
 * Renderer cho bản đồ
 */
public class MapRenderer {
    /**
     * Vẽ bản đồ
     */
    public void render(ShapeRenderer renderer, Map map) {
        for (int i = 1; i < map.getNumRows() - 1; i++) {
            for (int j = 1; j < map.getNumCols() - 1; j++) {
                Pos pos = new Pos(i, j);
                PointType type = map.point(pos).getType();

                float x = j * Config.CELL_SIZE;
                float y = (Config.MAP_ROWS - i - 1) * Config.CELL_SIZE;


                float[] color = getColorForType(type, i + j);
                renderer.setColor(color[0], color[1], color[2], color[3]);
                renderer.rect(x, y, Config.CELL_SIZE, Config.CELL_SIZE);
            }
        }
        
    }
    
    /**
     * Lấy màu cho loại ô
     */
    private float[] getColorForType(PointType type, int index) {
        switch (type) {
            case WALL:
                return Config.Colors.WALL;
            
            default:
                if (index % 2 == 0)
                {
                    return Config.Colors.EVEN;
                }
                else {
                    return Config.Colors.ODD;
                }
        }
    }

}
