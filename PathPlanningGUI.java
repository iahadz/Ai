import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PathPlanningGUI extends JFrame {
    private static final int GRID_SIZE = 10; // Size of the grid
    private static final int CELL_SIZE = 50; // Size of each cell in pixels

    private Grid grid;
    private Node startNode;
    private Node goalNode;
    private List<Node> path;

    public PathPlanningGUI() {
        setTitle("Path Planning Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        grid = new Grid(GRID_SIZE, GRID_SIZE);
        startNode = new Node(0, 0, null);
        goalNode = new Node(GRID_SIZE - 1, GRID_SIZE - 1, null);

        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw start node
        g.setColor(Color.GREEN);
        int startX = startNode.x * CELL_SIZE;
        int startY = startNode.y * CELL_SIZE;
        g.fillOval(startX + CELL_SIZE / 4, startY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);

        // Draw goal node
        g.setColor(Color.RED);
        int goalX = goalNode.x * CELL_SIZE;
        int goalY = goalNode.y * CELL_SIZE;
        g.fillOval(goalX + CELL_SIZE / 4, goalY + CELL_SIZE / 4, CELL_SIZE / 2, CELL_SIZE / 2);

        // Draw path
        if (path != null) {
            g.setColor(Color.BLUE);
            for (Node node : path) {
                int pathX = node.x * CELL_SIZE + CELL_SIZE / 2;
                int pathY = node.y * CELL_SIZE + CELL_SIZE / 2;
                g.fillOval(pathX - 5, pathY - 5, 10, 10);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PathPlanningGUI();
            }
        });
    }
}
