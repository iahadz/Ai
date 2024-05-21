import java.util.*;
public class Grid {
  private final int width;  // Width of the grid
  private final int height; // Height of the grid
  private final boolean[][] obstacles;  // 2D boolean array representing obstacles

  public Grid(int width, int height) {
      this.width = width;
      this.height = height;
      this.obstacles = new boolean[width][height]; // Initialize obstacles array
  }

  public boolean isObstacle(int x, int y) {
      return x >= 0 && x < width && y >= 0 && y < height && obstacles[x][y];
  }

  public void generateRandomObstacles(int obstacleCount) {
      java.util.Random random = new java.util.Random();

      for (int i = 0; i < obstacleCount; i++) {
          int x = random.nextInt(width);
          int y = random.nextInt(height);

          obstacles[x][y] = true; // Mark the cell as an obstacle
      }
  }
  public List<Node> getNeighbors(Node node) {
    List<Node> neighbors = new ArrayList<>();
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
            // Skip the current node itself
            if (dx == 0 && dy == 0) {
                continue;
            }

            int newX = node.x + dx;
            int newY = node.y + dy;

            // Check if the new coordinates are within the grid bounds and not an obstacle
            if (isValid(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
    }
    return neighbors;
}

private boolean isValid(int x, int y) {
  return x >= 0 && x < width && y >= 0 && y < height && !obstacles[x][y];
}
  public int getWidth() {
      return width;
  }

  public int getHeight() {
      return height;
  }

  public void resetObstacles() {
    for (int i = 0; i < width; i++) {
        for (int j = 0; j < height; j++) {
            obstacles[i][j] = false;
        }
    }
}

public void setObstacle(int x, int y, boolean isObstacle) {
  if (x >= 0 && x < width && y >= 0 && y < height) {
      obstacles[x][y] = isObstacle;
  } else {
      System.out.println("Error: Coordinates out of bounds.");
  }
}
}



