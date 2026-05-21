package at.ac.hcw.maxflow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FlowNetwork<V> {
    private final Map<V, List<Edge<V>>> adjacency = new LinkedHashMap<>();

    public void addVertex(V vertex) {
        adjacency.computeIfAbsent(Objects.requireNonNull(vertex), ignored -> new ArrayList<>());
    }

    public void addEdge(V from, V to, int capacity) {
        addVertex(from);
        addVertex(to);

        Edge<V> forward = new Edge<>(from, to, capacity, false);
        Edge<V> backward = new Edge<>(to, from, 0, true);
        forward.setResidualEdge(backward);
        backward.setResidualEdge(forward);

        adjacency.get(from).add(forward);
        adjacency.get(to).add(backward);
    }

    public Collection<V> vertices() {
        return Collections.unmodifiableSet(adjacency.keySet());
    }

    public List<Edge<V>> outgoingEdgesOf(V vertex) {
        List<Edge<V>> edges = adjacency.get(vertex);
        if (edges == null) {
            throw new IllegalArgumentException("Unbekannter Knoten: " + vertex);
        }
        return Collections.unmodifiableList(edges);
    }

    public List<Edge<V>> edges() {
        List<Edge<V>> result = new ArrayList<>();
        for (List<Edge<V>> edges : adjacency.values()) {
            for (Edge<V> edge : edges) {
                if (!edge.isResidual()) {
                    result.add(edge);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public void resetFlows() {
        for (List<Edge<V>> edges : adjacency.values()) {
            for (Edge<V> edge : edges) {
                edge.resetFlow();
            }
        }
    }
}
