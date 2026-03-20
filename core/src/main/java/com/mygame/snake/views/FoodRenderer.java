package com.mygame.snake.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.snake.configs.Config;
import com.mygame.snake.models.Map;
import com.mygame.snake.models.PointType;
import com.mygame.snake.models.Pos;

public class FoodRenderer {
    private Texture foodTexture;

    public FoodRenderer()
    {
        foodTexture = new Texture(Gdx.files.internal(Config.Textures.FOOD));
        //
        System.out.println("food: " + foodTexture.getTextureData().getFormat());
        // Lấy dữ liệu ảnh
        foodTexture.getTextureData().prepare();
        Pixmap pixmap = foodTexture.getTextureData().consumePixmap();

        // Lấy màu pixel tại (x, y)
        int pixel = pixmap.getPixel(0, 0); // thử góc trên trái

        int r = (pixel >> 24) & 0xff;
        int g = (pixel >> 16) & 0xff;
        int b = (pixel >> 8) & 0xff;
        int a = pixel & 0xff;

        System.out.println("R=" + r + " G=" + g + " B=" + b + " A=" + a);
        //
        foodTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void render(SpriteBatch batch, Map map) {
        for (int i = 1; i < map.getNumRows() - 1; i++) {
            for (int j = 1; j < map.getNumCols() - 1; j++) {
                Pos pos = new Pos(i, j);
                PointType type = map.point(pos).getType();

                float x = j * Config.CELL_SIZE;
                float y = (Config.MAP_ROWS - i - 1) * Config.CELL_SIZE;


                if (type == PointType.FOOD)
                {
                    batch.draw(foodTexture, x, y, Config.CELL_SIZE, Config.CELL_SIZE);
                }
            }
        }
    }
}
