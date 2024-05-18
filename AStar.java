import java.util.*;

public class AStar {

    public static List<Node> aStar(Grid grid, Node start, Node goal) {
        // Check if goal is reachable
        if (grid.isObstacle(goal.x, goal.y)) {
            return null; // Goal is unreachable
        }

        // Priority queue for open set, sorted by f-cost
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        // Hash set for closed set (visited nodes)
        Set<Node> closedSet = new HashSet<>();

        // Add the start node to the open set
        openSet.add(start);

        // Main A* search loop
        while (!openSet.isEmpty()) {
            // Get the node with the lowest f-cost from the open set
            Node current = openSet.poll();

            // Goal reached, reconstruct the path
            if (current.equals(goal)) {
                return reconstructPath(current);
            }

            // Move current node to closed set
            closedSet.add(current);

            // Explore neighbors
            for (Node neighbor : getNeighbors(grid, current)) {
                // Skip if already visited
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                // Calculate tentative g-cost through the current node
                double tentativeG = current.g + calculateDistance(current, neighbor);

                // Check if the neighbor is in the open set or has a lower g-cost than before
                if (!openSet.contains(neighbor) || tentativeG < neighbor.g) {
                    neighbor.parent = current;
                    neighbor.g = tentativeG;
                    neighbor.h = calculateHeuristic(neighbor, goal);
                    neighbor.f = neighbor.g + neighbor.h;

                    // Add the neighbor to the open set if it's not already there
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        // No path found
        return null; 
    }

    // Get valid neighbors of a node
    private static List<Node> getNeighbors(Grid grid, Node current) {
        List<Node> neighbors = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // Skip the current node itself
                if (dx == 0 && dy == 0) {
                    continue;
                }

                int newX = current.x + dx;
                int newY = current.y + dy;

                if (isValid(grid, newX, newY)) {
                    neighbors.add(new Node(newX, newY, current, 0, 0)); 
                }
            }
        }
        return neighbors;
    }

    // Check if a node is valid (within grid bounds and not an obstacle)
    private static boolean isValid(Grid grid, int x, int y) {
        return x >= 0 && x < grid.getWidth() && y >= 0 && y < grid.getHeight() && !grid.isObstacle(x, y);
    }

    // Calculate distance between two nodes (1 for horizontal/vertical, √2 for diagonal)
    public static double calculateDistance(Node node1, Node node2) {
        int dx = Math.abs(node1.x - node2.x);
        int dy = Math.abs(node1.y - node2.y);
        return (dx == 1 && dy == 1) ? Math.sqrt(2) : 1;
    }
    
    public static double calculateDistance(Node node) {
      double distance = 0.0;
      Node current = node;
  
      while (current.parent != null) {
          distance += calculateDistance(current, current.parent); // Use existing distance calculation for adjacent nodes
          current = current.parent;
      }
  
      return distance;
  }
  

    // Calculate Euclidean distance heuristic
    public static double calculateHeuristic(Node node, Node goal) {
        int dx = Math.abs(node.x - goal.x);
        int dy = Math.abs(node.y - goal.y);
        return Math.sqrt(dx * dx + dy * dy); 
    }

    public static double calculateFCost(Node node, Node goal) {
      double gCost = calculateDistance(node);
      double hCost = calculateHeuristic(node, goal);
      return gCost + hCost;
  }

    // Reconstruct the path from the goal node back to the start
    public static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current); // Add nodes in reverse order to get path from start to goal
            current = current.parent;
        }
        return path;
    }
    

    public static void main(String[] args) {
      // 1. Create Grid with Obstacles
      Grid grid = new Grid(15, 15);
      grid.generateRandomObstacles(100); // 25% obstacle density

      // 2. Define Start and Goal
      Node start = new Node(0, 0, null, 0, 0);
      Node goal = new Node(14, 14, null, 0, 0);
      grid.setObstacle(start.x, start.y, false); // Ensure start isn't blocked
      grid.setObstacle(goal.x, goal.y, false);   // Ensure goal isn't blocked

      // 3. Perform A* Search
      List<Node> path = AStar.aStar(grid, start, goal);

      // 4. Visualize Results
      printGridWithPath(grid, path, start, goal);

      // 5. Print F(n) Values (optional)
      if (path != null) {
          System.out.println("\nF(n) values along the path:");
          for (Node node : path) {
              double fn = AStar.calculateFCost(node, goal);
              System.out.println("Node (" + node.x + ", " + node.y + "): F(n) = " + fn);
          }
      }
      if (path != null) {
        double totalPathCost = calculateDistance(path.get(path.size() - 1));
        System.out.println("\nFull Path Cost: " + totalPathCost);
    } else {
        System.out.println("\nNo path found.");
    }
  }

  // Helper function to print the grid with path and start/goal marked
  private static void printGridWithPath(Grid grid, List<Node> path, Node start, Node goal) {
      for (int y = 0; y < grid.getHeight(); y++) {
          for (int x = 0; x < grid.getWidth(); x++) {
              if (grid.isObstacle(x, y)) {
                  System.out.print("O "); // Obstacle
              } else if (x == start.x && y == start.y) {
                  System.out.print("S "); // Start
              } else if (x == goal.x && y == goal.y) {
                  System.out.print("G "); // Goal
              } else if (path != null && path.contains(new Node(x, y, null, 0, 0))) {
                  System.out.print("X "); // Path
              } else {
                  System.out.print("- "); // Empty
              }
          }
          System.out.println();
      }
  }
}

