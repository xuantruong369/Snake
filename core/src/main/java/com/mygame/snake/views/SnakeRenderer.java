package com.mygame.snake.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygame.snake.configs.Config;
import com.mygame.snake.models.PointType;
import com.mygame.snake.models.Pos;
import com.mygame.snake.models.Snake;

import java.util.*;
/**
 * Renderer cho rắn
 */
public class SnakeRenderer {
    private Texture bodyBottomLeft;
    private Texture bodyBottomRight;
    private Texture bodyHorizntal;
    private Texture bodyTopLeft;
    private Texture bodyTopRight;
    private Texture bodyVertical;

    private Texture headDown;
    private Texture headLeft;
    private Texture headRight;
    private Texture headUp;

    private Texture tailDown;
    private Texture tailLeft;
    private Texture tailRight;
    private Texture tailUp;

    public SnakeRenderer()
    {
        bodyBottomLeft = new Texture(Gdx.files.internal(Config.Textures.BODY_BOTTOM_LEFT));
        bodyBottomLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bodyBottomRight = new Texture(Gdx.files.internal(Config.Textures.BODY_BOTTOM_RIGHT));
        bodyBottomRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bodyHorizntal = new Texture(Gdx.files.internal(Config.Textures.BODY_HORIZONTAL));
        bodyHorizntal.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bodyTopLeft = new Texture(Gdx.files.internal(Config.Textures.BODY_TOP_LEFT));
        bodyTopLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bodyTopRight = new Texture(Gdx.files.internal(Config.Textures.BODY_TOP_RIGHT));
        bodyTopRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        bodyVertical = new Texture(Gdx.files.internal(Config.Textures.BODY_VERTICAL));
        bodyVertical.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        headDown = new Texture(Gdx.files.internal(Config.Textures.HEAD_DOWN));
        //
        System.out.println("headDown: " + headDown.getTextureData().getFormat());
        // Lấy dữ liệu ảnh
        headDown.getTextureData().prepare();
        Pixmap pixmap = headDown.getTextureData().consumePixmap();

        // Lấy màu pixel tại (x, y)
        int pixel = pixmap.getPixel(0, 0); // thử góc trên trái

        int r = (pixel >> 24) & 0xff;
        int g = (pixel >> 16) & 0xff;
        int b = (pixel >> 8) & 0xff;
        int a = pixel & 0xff;

        System.out.println("R=" + r + " G=" + g + " B=" + b + " A=" + a);
        //
        headDown.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        headLeft = new Texture(Gdx.files.internal(Config.Textures.HEAD_LEFT));
        headLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        headRight = new Texture(Gdx.files.internal(Config.Textures.HEAD_RIGHT));
        headRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        headUp = new Texture(Gdx.files.internal(Config.Textures.HEAD_UP));
        headUp.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tailDown = new Texture(Gdx.files.internal(Config.Textures.TAIL_DOWN));
        tailDown.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tailLeft = new Texture(Gdx.files.internal(Config.Textures.TAIL_LEFT));
        tailLeft.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tailRight = new Texture(Gdx.files.internal(Config.Textures.TAIL_RIGHT));
        tailRight.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        tailUp = new Texture(Gdx.files.internal(Config.Textures.TAIL_UP));
        tailUp.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }
    /**
     * Vẽ rắn
     */
    public void render(SpriteBatch batch, Snake snake) {
        Deque<Pos> bodies = snake.getBodies();
        
        Iterator<Pos> iterator = bodies.iterator();
        int index = 0;
        
        while (iterator.hasNext()) {
            Pos pos = iterator.next();
            
            Texture texture;
            PointType type = snake.getMap().point(pos).getType();
            if (index == 0) {
                // Đầu
               texture = getHeadForType(type);
                
            } 
            else if (index == snake.len() - 1)
            {
                texture = getTailForType(getTailType(bodies));
            }
            else {
                // Thân
                texture = getBodyForType(type);
            }
            
            float x = pos.getY() * Config.CELL_SIZE;
            float y = (Config.MAP_ROWS - pos.getX() - 1) * Config.CELL_SIZE;
            

            batch.draw(texture, x, y, Config.CELL_SIZE, Config.CELL_SIZE);
            
            index++;
        }
        
    }


    private Texture getHeadForType(PointType type)
    {
        switch (type) {
            case HEAD_D:
                return headDown;
            case HEAD_L:
                return headLeft;
            case HEAD_R:
                return headRight;
            case HEAD_U:
                return headUp;
            default:
                System.out.println("Anh head is null");
                return null;
        }
    }

    private Texture getBodyForType(PointType type)
    {
        switch (type) {
            case BODY_LU:
                return bodyTopLeft;
            case BODY_DL:
                return bodyBottomLeft;
            case BODY_HOR:
                return bodyHorizntal;
            case BODY_RD:
                return bodyBottomRight;
            case BODY_UR:
                return bodyTopRight;
            case BODY_VER:
                return bodyVertical;
            default:
                System.out.println("Anh body is null");
                return null;
        }
    }

    private Texture getTailForType(PointType type)
    {
        switch (type) {
            case TAIL_DOWN:
                return tailDown;
            case TAIL_LEFT:
                return tailLeft;
            case TAIL_RIGHT:
                return tailRight;
            case TAIL_UP:
                return tailUp;
            default:
                System.out.println("Anh tail is null");
                return null;
        }
    }

    public PointType getTailType(Deque<Pos> bodies) {
        Pos tail = bodies.getLast();
        Pos beforeTail = null;

        Iterator<Pos> it = bodies.descendingIterator();
        it.next(); // skip tail
        if (it.hasNext()) {
            beforeTail = it.next();
        }

        if (beforeTail == null) return PointType.TAIL_RIGHT;

        if (beforeTail.getX() < tail.getX()) return PointType.TAIL_DOWN;
        if (beforeTail.getX() > tail.getX()) return PointType.TAIL_UP;
        if (beforeTail.getY() < tail.getY()) return PointType.TAIL_RIGHT;
        return PointType.TAIL_LEFT;
    }
}