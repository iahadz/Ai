import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class AStar {
    public static List<Node> aStar(Grid grid, Node start, Node goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>((n1, n2) -> Double.compare(n1.f, n2.f));
        HashSet<Node> visited = new HashSet<>();

        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            visited.add(current);

            for (Node neighbor : getSuccessors(grid, current)) {
                if (visited.contains(neighbor)) {
                    continue; // Skip if already visited
                }

                double tentativeG = current.g + calculateDistance(current, neighbor);

                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;
                    neighbor.parent = current;
                    openSet.add(neighbor);
                }
            }
        }

        return null; // No path found
    }

    private static List<Node> getSuccessors(Grid grid, Node current) {
        List<Node> successors = new ArrayList<>();
        int x = current.x;
        int y = current.y;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue; // Don't include the current position
                }

                int newX = x + dx;
                int newY = y + dy;

                if (isValid(grid, newX, newY)) {
                    successors.add(new Node(newX, newY, current));
                }
            }
        }

        return successors;
    }

    private static boolean isValid(Grid grid, int x, int y) {
        return x >= 0 && x < grid.getWidth() && y >= 0 && y < grid.getHeight() && !grid.isObstacle(x, y);
    }

    private static double calculateDistance(Node current, Node neighbor) {
        // Use Euclidean or Manhattan distance as appropriate
        return Math.sqrt(Math.pow(current.x - neighbor.x, 2) + Math.pow(current.y - neighbor.y, 2)); 
    }

    private static double calculateHeuristic(Node neighbor, Node goal) {
        // Use Manhattan distance for heuristic
        return Math.abs(neighbor.x - goal.x) + Math.abs(neighbor.y - goal.y); 
    }

    private static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        return path;
    }
}
