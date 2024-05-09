import java.util.*;
public class AStarTest {
  public static void main(String[] args) {
      // Define grid dimensions
      int width = 100;
      int height = 100;

      // Create a grid
      Grid grid = new Grid(width, height);

      // Generate some random obstacles
      grid.generateRandomObstacles(15);

      // Define start and goal nodes
      Node start = new Node(0, 0, null);
      Node goal = new Node(99, 99, null);

      // Find the path using A*
      List<Node> path = AStar.aStar(grid, start, goal);

      // Print the path
      if (path != null) {
          System.out.println("Path found:");
          for (Node node : path) {
              System.out.println("(" + node.x + ", " + node.y + ")");
          }
      } else {
          System.out.println("No path found.");
      }
  }
}