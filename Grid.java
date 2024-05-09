import java.util.*;
public class Grid {
  private int width;  // Width of the grid
  private int height; // Height of the grid
  private boolean[][] obstacles;  // 2D boolean array representing obstacles

  public Grid(int width, int height) {
      this.width = width;
      this.height = height;
      this.obstacles = new boolean[width][height]; // Initialize obstacles array
  }

  public boolean isObstacle(int x, int y) {
      if (x >= 0 && x < width && y >= 0 && y < height) { // Check if within grid bounds
          return obstacles[x][y];
      } else {
          return false; // Can be adjusted to handle out-of-bounds behavior if needed
      }
  }

  public void generateRandomObstacles(int obstacleCount) {
      Random random = new Random();

      for (int i = 0; i < obstacleCount; i++) {
          int x = random.nextInt(width);
          int y = random.nextInt(height);

          obstacles[x][y] = true; // Mark the cell as an obstacle
      }
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
