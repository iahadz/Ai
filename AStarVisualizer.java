import processing.core.*;
import java.util.*;

public class AStarVisualizer extends PApplet {

    private static final int CELL_SIZE = 30; // Size of each grid cell
    private Grid grid;
    private Node startNode, goalNode;
    private List<Node> path;
    private boolean showPathFinding = false; // Flag to control when to run A*
    private enum Mode { START, GOAL, OBSTACLE }
    private Mode currentMode = Mode.START;

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        int rows = height / CELL_SIZE;
        int cols = width / CELL_SIZE;
        grid = new Grid(cols, rows);
        grid.generateRandomObstacles(40); // Add some random obstacles
        // Set the start and goal nodes to null initially
        startNode = null;
        goalNode = null;
    }

    public void draw() {
        background(255); // White background

        // Draw grid
        stroke(150); // Light gray grid lines
        for (int x = 0; x <= grid.getWidth(); x++) {
            line(x * CELL_SIZE, 0, x * CELL_SIZE, height);
        }
        for (int y = 0; y <= grid.getHeight(); y++) {
            line(0, y * CELL_SIZE, width, y * CELL_SIZE);
        }

        // Draw obstacles
        fill(0); // Black for obstacles
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (grid.isObstacle(x, y)) {
                    rect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw start and goal
        if (startNode != null) {
            fill(0, 0, 255); // Blue for start
            ellipse(startNode.x * CELL_SIZE + CELL_SIZE / 2, startNode.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
        }
        if (goalNode != null) {
            fill(255, 0, 0); // Red for goal
            ellipse(goalNode.x * CELL_SIZE + CELL_SIZE / 2, goalNode.y * CELL_SIZE + CELL_SIZE / 2, CELL_SIZE, CELL_SIZE);
        }

        // Draw path
        if (showPathFinding && path != null) {
            stroke(0, 255, 0); // Green for path
            strokeWeight(5);
            for (int i = 0; i < path.size() - 1; i++) {
                Node n = path.get(i);
                Node next = path.get(i + 1);
                line(n.x * CELL_SIZE + CELL_SIZE / 2, n.y * CELL_SIZE + CELL_SIZE / 2,
                     next.x * CELL_SIZE + CELL_SIZE / 2, next.y * CELL_SIZE + CELL_SIZE / 2);
            }
        }
    }

    public void mousePressed() {
        int x = mouseX / CELL_SIZE;
        int y = mouseY / CELL_SIZE;

        if (currentMode == Mode.START && startNode == null) {
            startNode = new Node(x, y);
            grid.setObstacle(x, y, false);
            currentMode = Mode.GOAL;
        } else if (currentMode == Mode.GOAL && goalNode == null) {
            goalNode = new Node(x, y);
            grid.setObstacle(x, y, false);
            currentMode = Mode.OBSTACLE;
        } else if (currentMode == Mode.OBSTACLE) {
            grid.toggleObstacle(x, y);
        }

        showPathFinding = false; // Clear the path if the grid changes
        path = null;
        redraw();
    }

    public void keyPressed() {
        if (key == ' ' && startNode != null && goalNode != null) {
            showPathFinding = true;
            path = AStar.aStar(grid, startNode, goalNode); 
            redraw();
        } else if (key == 'c' || key == 'C') {
            setup();
            showPathFinding = false;
            path = null;
            redraw();
        }
    }
    public static void main(String[] args) {
        PApplet.main("AStarVisualizer");
    }
}
