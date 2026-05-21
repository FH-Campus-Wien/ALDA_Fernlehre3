package at.ac.hcw.maxflow;

import java.util.Map;
import java.util.Set;

public class FordFulkerson<V> {
    public MaxFlowResult<V> computeMaxFlow(FlowNetwork<V> network, V source, V sink) {
        throw new UnsupportedOperationException("Diese Methode muss implementiert werden.");
    }

    private boolean depthFirstSearch(
            FlowNetwork<V> network,
            V current,
            V sink,
            Set<V> visited,
            Map<V, Edge<V>> parentByVertex
    ) {
        throw new UnsupportedOperationException("Diese Methode muss implementiert werden.");
    }

    private int determineBottleneck(V source, V sink, Map<V, Edge<V>> parentByVertex) {
        throw new UnsupportedOperationException("Diese Methode muss implementiert werden.");
    }

    private void augment(V source, V sink, Map<V, Edge<V>> parentByVertex, int bottleneck) {
        throw new UnsupportedOperationException("Diese Methode muss implementiert werden.");
    }

    private String formatPath(V source, V sink, Map<V, Edge<V>> parentByVertex, int bottleneck) {
        throw new UnsupportedOperationException("Diese Methode muss implementiert werden.");
    }
}
