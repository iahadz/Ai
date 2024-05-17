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
    private static double calculateDistance(Node node1, Node node2) {
        int dx = Math.abs(node1.x - node2.x);
        int dy = Math.abs(node1.y - node2.y);
        return (dx == 1 && dy == 1) ? Math.sqrt(2) : 1;
    }

    // Calculate Euclidean distance heuristic
    private static double calculateHeuristic(Node node, Node goal) {
        int dx = Math.abs(node.x - goal.x);
        int dy = Math.abs(node.y - goal.y);
        return Math.sqrt(dx * dx - dy * dy); 
    }

    // Reconstruct the path from the goal node back to the start
    private static List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current); // Add nodes in reverse order to get path from start to goal
            current = current.parent;
        }
        return path;
    }
    

      public static void main(String[] args) {
          // 1. Create the Grid
          Grid grid = new Grid(10, 10); // 10x10 grid
          grid.generateRandomObstacles(20); // Add 20 random obstacles
  
          // 2. Define Start and Goal Nodes
          Node start = new Node(0, 0, null, 0, 0);
          Node goal = new Node(9, 9, null, 0, 0);
  
          // Ensure start and goal are not on obstacles
          grid.setObstacle(start.x, start.y, false);
          grid.setObstacle(goal.x, goal.y, false);
  
          // 3. Run A* Search
          List<Node> path = AStar.aStar(grid, start, goal);
  
          // 4. Print Results
          printGrid(grid, path);
      }
  
      // Function to print the grid and path (if found)
      private static void printGrid(Grid grid, List<Node> path) {
          for (int y = 0; y < grid.getHeight(); y++) {
              for (int x = 0; x < grid.getWidth(); x++) {
                  if (grid.isObstacle(x, y)) {
                      System.out.print("O "); // Obstacle
                  } else if (path != null && path.contains(new Node(x, y, null, 0, 0))) {
                      System.out.print("X "); // Path node
                  } else {
                      System.out.print("- "); // Empty space
                  }
              }
              System.out.println();
          }
          if (path != null) {
            System.out.println("Path found! Length: " + path.size());
            System.out.println("F(n) values along the path:");

            // Calculate and print F(n) for each node in the path
            for (Node node : path) {
                double fn = node.f;
                System.out.println("Node (" + node.x + ", " + node.y + "): F(n) = " + fn);
            }
        } else {
            System.out.println("No path found.");
        }}
  }
