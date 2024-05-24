import java.util.*;

public class AStar {

  public static List<Node> aStar(Grid grid, Node start, Node goal) {
    // Check if goal is reachable
    if (grid.isObstacle(goal.x, goal.y)) {
        return null; // Goal is unreachable
    }

    // Fringe (Priority Queue) for unexpanded nodes, sorted by f-cost
    PriorityQueue<Node> fringe = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
    // Hash set for closed set (visited nodes)
    Set<Node> closedSet = new HashSet<>();

    // Add the start node to the fringe
    start.g = 0; 
    start.h = calculateHeuristic(start, goal);
    start.f = start.g + start.h;
    fringe.add(start);

    // Main A* search loop
    while (!fringe.isEmpty()) {
        // Get the node with the lowest f-cost from the fringe
        Node current = fringe.poll();

        // Goal reached, reconstruct the path
        if (current.equals(goal)) {
            return reconstructPath(current);
        }

        // Move current node to closed set
        closedSet.add(current);

        // Explore neighbors
        for (Node neighbor : getNeighbors(grid, current)) {
            // Skip if already visited or an obstacle
            if (closedSet.contains(neighbor) || grid.isObstacle(neighbor.x, neighbor.y)) {
                continue;
            }

            // Calculate tentative g-cost through the current node
            double tentativeG = current.g + calculateDistance(current, neighbor);

            // Check if the neighbor is in the fringe or has a lower g-cost than before
            if (!fringe.contains(neighbor) || tentativeG < neighbor.g) {
                neighbor.parent = current;
                neighbor.g = tentativeG;
                neighbor.h = calculateHeuristic(neighbor, goal);
                neighbor.f = neighbor.g + neighbor.h;

                // Add the neighbor to the fringe if it's not already there
                if (!fringe.contains(neighbor)) {
                    fringe.add(neighbor);
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
    // public static double calculateHeuristic(Node node, Node goal) {
    //     int dx = Math.abs(node.x - goal.x);
    //     int dy = Math.abs(node.y - goal.y);
    //     return Math.sqrt(dx * dx + dy * dy); 
    // }
      //Calculate Octile Distance heuristic
    public static double calculateHeuristic(Node node,  Node goal) {
      int dx = Math.abs(goal.x - node.x);
      int dy = Math.abs(goal.y - node.y);
      return (dx + dy) + (Math.sqrt(2) - 2) * Math.min(dx, dy);
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
      // Create Grid with Obstacles
      Grid grid = new Grid(10, 10);
      grid.generateRandomObstacles(25); // % obstacle density

      //  Define Start and Goal
      Node start = new Node(0, 0,null,0,0);
      Node goal = new Node(9, 9,null,0,0);
      grid.setObstacle(start.x, start.y, false); // Ensure start isn't blocked
      grid.setObstacle(goal.x, goal.y, false);   // Ensure goal isn't blocked

      //  Perform A* Search
      List<Node> path = AStar.aStar(grid, start, goal);

      //  Visualize Results
      printGridWithPath(grid, path, start, goal);

      //  Print F(n) Values & Unexpanded Nodes
      if (path != null) {
          System.out.println("\nF(n) values along the path:");
          for (Node node : path) {
              double fn = AStar.calculateFCost(node, goal);
              double a = AStar.calculateDistance(node);
              double b = AStar.calculateHeuristic(node, goal);
              System.out.printf("Node (%d, %d): %.1f + %.1f = %.1f\n", node.x, node.y, a, b, fn);
          }
           if (path != null) {
               double totalPathCost = calculateDistance(path.get(path.size() - 1));
                 System.out.println("\nFull Path Cost: " + totalPathCost);
                  } else {
                   System.out.println("\nNo path found.");
                 }

          // Print unexpanded nodes
          PriorityQueue<Node> remainingFringe = getUnexpandedNodes(grid, path, start);
          if (!remainingFringe.isEmpty()) {
              System.out.println("\nUnexpanded nodes (in no particular order):");
              while (!remainingFringe.isEmpty()) {
                  Node unexpandedNode = remainingFringe.poll();
                  System.out.printf("Node (%d, %d) ,", unexpandedNode.x, unexpandedNode.y);
              }
          }
          
      }
  }
 
  
  // function to print the grid with path and start/goal marked
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
  //function to get unexpanded nodes 
  private static PriorityQueue<Node> getUnexpandedNodes(Grid grid, List<Node> path, Node start) {
      PriorityQueue<Node> fringe = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
      Set<Node> expandedNodes = new HashSet<>(path);
      expandedNodes.add(start);

      for (Node node : path) {
          for (Node neighbor : grid.getNeighbors(node)) { // Use grid.getNeighbors here
              if (!expandedNodes.contains(neighbor)) {
                  fringe.add(neighbor);
              }
          }
      }
      return fringe;
  }
}
