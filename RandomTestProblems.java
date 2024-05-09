import java.util.List;
import java.util.Random;

public class RandomTestProblems {

    public static void main(String[] args) {
        int gridSize = 100;
        int step = 10;

        for (int obstaclePercentage = 10; obstaclePercentage <= 90; obstaclePercentage += step) {
            Grid grid = generateRandomGrid(gridSize, obstaclePercentage);
            Node startNode = new Node(0, 0, null);
            Node goalNode = new Node(gridSize - 1, gridSize - 1, null);

            long startTime = System.currentTimeMillis();
            List<Node> path = AStar.aStar(grid, startNode, goalNode);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Obstacle Percentage: " + obstaclePercentage + "%");
            System.out.println("Path Length: " + (path != null ? path.size() : "No path found"));
            System.out.println("Execution Time: " + executionTime + " milliseconds\n");
        }
    }

    private static Grid generateRandomGrid(int size, int obstaclePercentage) {
        Grid grid = new Grid(size, size);
        Random random = new Random();

        int totalCells = size * size;
        int obstacleCount = totalCells * obstaclePercentage / 100;

        for (int i = 0; i < obstacleCount; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid.setObstacle(x, y, true);
        }

        return grid;
    }
}
