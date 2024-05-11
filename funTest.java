import java.util.List;

public class funTest {

    public static void main(String[] args) {
        // Define the grid size and obstacle percentage
        int gridSize = 5;
        int obstaclePercentage = 20;

        // Create a grid with obstacles
        Grid grid = generateRandomGrid(gridSize, obstaclePercentage);

        // Define the start and goal nodes
        Node startNode = new Node(0, 0, null, 0, 0);
        Node goalNode = new Node(gridSize - 1, gridSize - 1, null, 0, 0);

        // Find the path using A* algorithm and measure time taken
        long startTime = System.nanoTime();
        List<Node> path = AStar.aStar(grid, startNode, goalNode);
        long endTime = System.nanoTime();

        // Calculate path cost
        double pathCost = calculatePathCost(path);

        // Display the path and statistics
        System.out.println("Path:");
        if (path != null) {
            for (Node node : path) {
                System.out.println("(" + node.x + ", " + node.y + ")");
            }
            System.out.println("Path Cost: " + pathCost);
            System.out.println("Time Taken: " + (endTime - startTime) + " nanoseconds");
        } else {
            System.out.println("No path found.");
        }
    }

    private static Grid generateRandomGrid(int size, int obstaclePercentage) {
        Grid grid = new Grid(size, size);
        java.util.Random random = new java.util.Random();

        int totalCells = size * size;
        int obstacleCount = totalCells * obstaclePercentage / 100;

        for (int i = 0; i < obstacleCount; i++) {
            int x = random.nextInt(size);
            int y = random.nextInt(size);
            grid.setObstacle(x, y, true);
        }

        return grid;
    }

    private static double calculatePathCost(List<Node> path) {
        if (path == null || path.isEmpty()) {
            return 0;
        }

        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            cost += calculateDistance(current, next);
        }

        return cost;
    }

    private static double calculateDistance(Node current, Node neighbor) {
        // Use Manhattan distance for grid-based movement
        return Math.abs(current.x - neighbor.x) + Math.abs(current.y - neighbor.y);
    }
}
