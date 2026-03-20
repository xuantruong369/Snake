package com.mygame.snake.bots;

import java.util.*;

import com.mygame.snake.models.Direc;
import com.mygame.snake.models.Pos;
import com.mygame.snake.models.Snake;

public class FloodFill extends BaseSolver{

    private PathSolver pathSolver;

    public FloodFill(Snake snake)
    {
        super(snake);
        this.pathSolver = new PathSolver(snake);
    }

    @Override
    public Direc nextDirec() {
        // --- BƯỚC 1: TÌM ĐƯỜNG NGẮN NHẤT TỚI THỨC ĂN ---
        pathSolver.setSnake(snake);
        Deque<Direc> pathToFood = pathSolver.shortestPathToFood();

        if (!pathToFood.isEmpty()) {
            // Giả lập ăn thử để kiểm tra an toàn sau khi ăn
            Snake sCopy = copiedSnake(snake);
            moveSnakePath(sCopy, pathToFood);
            
            // Nếu bản đồ đầy sau khi ăn thì cứ đi (thắng)
            if (sCopy.getMap().isFull()) {
                return pathToFood.getFirst();
            }

            // KIỂM TRA AN TOÀN BẰNG FLOOD FILL
            // Diện tích trống sau khi ăn phải lớn hơn chiều dài rắn
            int spaceAfterEating = countAvailableSpace(sCopy.head(), sCopy);
            
            if (spaceAfterEating > sCopy.getBodies().size()) {
                // Kiểm tra thêm xem sau khi ăn có đường về đuôi không
                pathSolver.setSnake(sCopy);
                if (pathSolver.longestPathToTail().size() > 1) {
                    return pathToFood.getFirst();
                }
            }
        }

        // --- BƯỚC 2: TÌM ĐƯỜNG DÀI NHẤT VỀ ĐUÔI (STALLING) ---
        // Nếu không ăn được an toàn, rắn sẽ đuổi theo đuôi mình để chờ cơ hội
        pathSolver.setSnake(snake);
        Deque<Direc> pathToTail = pathSolver.longestPathToTail();
        
        if (pathToTail.size() > 1) {
            return pathToTail.getFirst();
        }

        // --- BƯỚC 3: CHẾ ĐỘ SINH TỒN (SURVIVAL MODE) ---
        // Nếu không thấy thức ăn và cũng mất dấu đuôi, đi vào vùng trống lớn nhất
        return getBestSurvivalDirec();
    }

    /**
     * Thuật toán Flood Fill (BFS) để đếm ô trống khả dụng
     */
    private int countAvailableSpace(Pos starPos, Snake currentSnake)
    {
        Set<Pos> visited = new HashSet<>();
        Queue<Pos> queue = new LinkedList<>();

        if (!currentSnake.getMap().isSafe(starPos))
        {
            return 0;
        }

        queue.add(starPos);
        visited.add(starPos);
        int count = 0;

        while (!queue.isEmpty()) {
            Pos curr = queue.poll();
            count++;

            for (Pos adj : curr.allAdj()) {
                // isSafe kiểm tra cả va chạm tường và va chạm thân rắn
                if (currentSnake.getMap().isSafe(adj) && !visited.contains(adj)) {
                    visited.add(adj);
                    queue.add(adj);
                }
            }
        }
        return count;
    }

    
    /**
     * Tìm hướng đi dẫn đến vùng không gian rộng lớn nhất.
     */
    private Direc getBestSurvivalDirec() {
        Pos head = snake.head();
        Direc bestDirec = snake.getDirec();
        int maxSpace = -1;

        for (Pos adj : head.allAdj()) {
            if (map.isSafe(adj)) {
                int space = countAvailableSpace(adj, snake);
                if (space > maxSpace) {
                    maxSpace = space;
                    bestDirec = head.direcTo(adj);
                }
            }
        }
        return bestDirec;
    }

    /**
     * Tạo bản sao sâu (deep copy) của rắn để mô phỏng nước đi.
     */
    private Snake copiedSnake(Snake originalSnake) {
        // Giả sử Snake có constructor copy hoặc phương thức copy
        Snake copy = new Snake(originalSnake.getMap().copy());
        // Sao chép các thuộc tính quan trọng khác như danh sách body, hướng đi...
        // Tùy vào cấu trúc class Snake của bạn, hãy đảm bảo bản copy không ảnh hưởng bản gốc
        copy.setDead(originalSnake.isDead());
        copy.setDirecNext(originalSnake.getDirecNext());
        return copy; 
    }

    /**
     * Di chuyển rắn giả lập dọc theo một lộ trình.
     */
    private void moveSnakePath(Snake snake, Deque<Direc> path) {
        for (Direc direc : path) {
            snake.move(direc);
        }
    }

    @Override
    public boolean ready() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ready'");
    }
    
}
