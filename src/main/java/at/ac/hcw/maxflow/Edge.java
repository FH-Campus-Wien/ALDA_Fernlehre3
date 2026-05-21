package at.ac.hcw.maxflow;

public class Edge<V> {
    private final V from;
    private final V to;
    private final int capacity;
    private final boolean residual;
    private int flow;
    private Edge<V> residualEdge;

    Edge(V from, V to, int capacity, boolean residual) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.residual = residual;
    }

    public V from() {
        return from;
    }

    public V to() {
        return to;
    }

    public int capacity() {
        return capacity;
    }

    public int flow() {
        return flow;
    }

    public int remainingCapacity() {
        return capacity - flow;
    }

    public boolean isResidual() {
        return residual;
    }

    void augment(int bottleneck) {
        flow += bottleneck;
        residualEdge.flow -= bottleneck;
    }

    void resetFlow() {
        flow = 0;
    }

    void setResidualEdge(Edge<V> residualEdge) {
        this.residualEdge = residualEdge;
    }
}
