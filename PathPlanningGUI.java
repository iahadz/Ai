import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class PathPlanningGUI extends JFrame {
    private static final int GRID_SIZE = 5; // Size of the grid
    private static final int CELL_SIZE = 50; // Size of each cell in pixels

    private Grid grid;
    private Node startNode;
    private Node goalNode;
    private List<Node> path;

    public PathPlanningGUI() {
        setTitle("Path Planning Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        grid = generateRandomGrid(GRID_SIZE, 20); // Initial grid with 20% obstacles
        startNode = new Node(0, 0, null, 0, 0);
        goalNode = new Node(GRID_SIZE - 1, GRID_SIZE - 1, null, 0, 0);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Generate new random grid with obstacles
                grid = generateRandomGrid(GRID_SIZE, 20); // Adjust obstacle percentage as needed
                // Reset start and goal nodes
                startNode = new Node(0, 0, null, 0, 0);
                goalNode = new Node(GRID_SIZE - 1, GRID_SIZE - 1, null, 0, 0);
                // Find path
                path = AStar.aStar(grid, startNode, goalNode);
                repaint();
            }
        });
        controlPanel.add(startButton);

        getContentPane().add(controlPanel, BorderLayout.NORTH);
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE + 50); // Extra 50 for control panel
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw grid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                int x = i * CELL_SIZE;
                int y = j * CELL_SIZE;

                if (grid.isObstacle(i, j)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE); // Draw obstacle square
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw start node
        g.setColor(Color.BLUE); // Set color to blue for start state
        int startX = startNode.x * CELL_SIZE + CELL_SIZE / 2;
        int startY = startNode.y * CELL_SIZE + CELL_SIZE / 2;
        g.fillOval(startX , startY, 10, 10);

        // Draw goal node
        g.setColor(Color.GREEN); // Set color to green for goal state
        int goalX = goalNode.x * CELL_SIZE + CELL_SIZE / 2;
        int goalY = goalNode.y * CELL_SIZE + CELL_SIZE / 2;
        g.fillOval(goalX - 5, goalY - 5, 10, 10);

        // Draw path
        if (path != null) {
            g.setColor(Color.BLUE);
            for (Node node : path) {
                int pathX = node.x * CELL_SIZE + CELL_SIZE / 2;
                int pathY = node.y * CELL_SIZE + CELL_SIZE / 2;
                g.fillOval(pathX - 1, pathY - 1, 3, 3);
            }
        }
    }

    private Grid generateRandomGrid(int size, int obstaclePercentage) {
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

    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
              PathPlanningGUI gui = new PathPlanningGUI();
  
              // Create a 5x5 grid with obstacles
              gui.grid = new Grid(5, 5);
              gui.grid.setObstacle(1, 2, true);
              gui.grid.setObstacle(2, 2, true);
              gui.grid.setObstacle(3, 2, true);
              gui.grid.setObstacle(3, 3, true);
  
              // Set start and goal nodes
              gui.startNode = new Node(0, 0, null, 0, 0);
              gui.goalNode = new Node(4, 4, null, 0, 0);
  
              // Find path
              gui.path = AStar.aStar(gui.grid, gui.startNode, gui.goalNode);
              gui.repaint();
          }
      });
    }}
  
