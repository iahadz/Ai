import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AStarGUI extends JFrame {

    private static final int GRID_SIZE = 10; // Size of the grid
    private static final int CELL_SIZE = 50; // Size of each cell in pixels
    private static final int PADDING = 10; // Padding around the grid
    private static final Color COLOR_EMPTY = Color.WHITE;
    private static final Color COLOR_START = Color.GREEN; // Start node color
    private static final Color COLOR_GOAL = Color.RED; // Goal node color
    private static final Color COLOR_PATH = Color.GREEN;
    private static final Color COLOR_OBSTACLE = Color.BLACK;

    private Grid grid;
    private List<Node> path;
    private double totalPathCost;
    private Node start;
    private Node goal;

    private JPanel gridPanel;
    private JPanel infoPanel;
    private JLabel pathLengthLabel;
    private JLabel fnValuesLabel;
    private JLabel totalFnLabel;

    private JButton startButton;

    public AStarGUI() {
        setTitle("A* Pathfinding Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
        add(gridPanel, BorderLayout.CENTER);

        infoPanel = new JPanel(new GridLayout(3, 1));
        pathLengthLabel = new JLabel("Path Length: ");
        fnValuesLabel = new JLabel("f(n) values: ");
        totalFnLabel = new JLabel("Total f(n): ");
        infoPanel.add(pathLengthLabel);
        infoPanel.add(fnValuesLabel);
        infoPanel.add(totalFnLabel);
        add(infoPanel, BorderLayout.SOUTH);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGrid();
                startAStar();
                updateInfoPanel();
                repaint();
            }
        });
        add(startButton, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void resetGrid() {
        grid = new Grid(GRID_SIZE, GRID_SIZE);
        grid.generateRandomObstacles(25); // % obstacle density

        start = new Node(0, 0, null, 0, 0);
        goal = new Node(GRID_SIZE - 1, GRID_SIZE - 1, null, 0, 0);
        grid.setObstacle(start.x, start.y, false);
        grid.setObstacle(goal.x, goal.y, false);
    }

    private void startAStar() {
        path = AStar.aStar(grid, start, goal);
        totalPathCost = path != null ? AStar.calculateDistance(path.get(path.size() -1)) : 0;
    }

    private void drawGrid(Graphics g) {
        // Draw the grid
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                int xPos = x * CELL_SIZE;
                int yPos = y * CELL_SIZE;

                // Determine color for each cell
                if (grid.isObstacle(x, y)) {
                    g.setColor(COLOR_OBSTACLE);
                } else if (path != null && pathContainsNode(x, y)) {
                    g.setColor(COLOR_PATH);
                } else if (x == start.x && y == start.y) {
                    g.setColor(COLOR_START); // Start node color
                } else if (x == goal.x && y == goal.y) {
                    g.setColor(COLOR_GOAL); // Goal node color
                } else {
                    g.setColor(COLOR_EMPTY); // Default empty cell color
                }

                // Fill cell with color
                g.fillRect(xPos, yPos, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(xPos, yPos, CELL_SIZE, CELL_SIZE);

                // Draw f(n) value for each cell
                Node current = new Node(x, y, null, 0, 0);
                double fn = AStar.calculateFCost(current, goal);
                String fnValue = String.format("%.1f", fn);
                g.setColor(Color.BLACK);
                g.drawString(fnValue, xPos + 5, yPos + 15);
            }
        }
    }

    private void updateInfoPanel() {
        // Update path length label
        pathLengthLabel.setText("Path Length: " + (path != null ? path.size() - 1 : 0)); // Exclude start node

        // Update f(n) values label
        StringBuilder sb = new StringBuilder("f(n) values: ");
        if (path != null) {
            for (Node node : path) {
                if (!(node.x == start.x && node.y == start.y) ) {
                  double a = AStar.calculateDistance(node);
                  double b = AStar.calculateHeuristic(node, goal);
                    double fn = AStar.calculateFCost(node, goal);
                                    sb.append("(").append(node.x).append(",").append(node.y).append("): ").append("=(").append(String.format("%.1f",a)).append("+").append(String.format("%.1f",b)).append(")= ").append(String.format("%.1f ", fn)).append(", ");
                }
            }
        }
        fnValuesLabel.setText(sb.toString());

        // Update total f(n) label
        totalFnLabel.setText("Path cost : " + String.format("%.1f", totalPathCost));
    }

    private boolean pathContainsNode(int x, int y) {
        if (path == null) return false;
        for (Node node : path) {
            if (node.x == x && node.y == y) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AStarGUI();
            }
        });
    }
}
