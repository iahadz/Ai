import java.util.Objects;

public class Node {
    public int x;  // X-coordinate of the node in the grid
    public int y;  // Y-coordinate of the node in the grid
    public Node parent;  // Parent node in the path from start
    public double g;  // Cost from the start node to this node
    public double h;  // Heuristic estimate of the cost from this node to the goal
    public double f;  // Total cost estimate (g + h)

    public Node(int x, int y, Node parent, double g, double h) {
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.g = g;
        this.h = h;
        this.f = g + h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

