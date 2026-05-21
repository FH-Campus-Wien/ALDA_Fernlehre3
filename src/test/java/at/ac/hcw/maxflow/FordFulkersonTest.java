package at.ac.hcw.maxflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FordFulkersonTest {
    @Test
    void computesMaximumFlowForSampleNetwork() {
        FlowNetwork<String> network = new FlowNetwork<>();
        network.addEdge("s", "a", 10);
        network.addEdge("s", "c", 10);
        network.addEdge("a", "b", 4);
        network.addEdge("a", "c", 2);
        network.addEdge("a", "d", 8);
        network.addEdge("c", "d", 9);
        network.addEdge("d", "b", 6);
        network.addEdge("b", "t", 10);
        network.addEdge("d", "t", 10);

        FordFulkerson<String> algorithm = new FordFulkerson<>();
        MaxFlowResult<String> result = algorithm.computeMaxFlow(network, "s", "t");

        assertEquals(19, result.maxFlow());
        assertEquals(5, result.augmentingPaths().size());
    }

    @Test
    void returnsZeroWhenSinkCannotBeReached() {
        FlowNetwork<String> network = new FlowNetwork<>();
        network.addEdge("s", "a", 7);
        network.addEdge("b", "t", 5);

        FordFulkerson<String> algorithm = new FordFulkerson<>();
        MaxFlowResult<String> result = algorithm.computeMaxFlow(network, "s", "t");

        assertEquals(0, result.maxFlow());
        assertEquals(0, result.augmentingPaths().size());
    }

    @Test
    void rejectsMissingSourceOrSink() {
        FlowNetwork<String> network = new FlowNetwork<>();
        network.addEdge("s", "a", 3);

        FordFulkerson<String> algorithm = new FordFulkerson<>();

        assertThrows(IllegalArgumentException.class, () -> algorithm.computeMaxFlow(network, "s", "t"));
    }

    @Test
    void rejectsNegativeCapacities() {
        FlowNetwork<String> network = new FlowNetwork<>();

        assertThrows(IllegalArgumentException.class, () -> network.addEdge("s", "t", -1));
    }

    @Test
    void usesResidualEdgesToRerouteFlow() {
        FlowNetwork<String> network = new FlowNetwork<>();
        network.addEdge("s", "a", 3);
        network.addEdge("s", "b", 2);
        network.addEdge("a", "b", 2);
        network.addEdge("a", "t", 2);
        network.addEdge("b", "t", 3);

        FordFulkerson<String> algorithm = new FordFulkerson<>();
        MaxFlowResult<String> result = algorithm.computeMaxFlow(network, "s", "t");

        assertEquals(5, result.maxFlow());
        assertTrue(
                result.augmentingPaths().stream().anyMatch(path -> path.contains("s -> b -> a -> t")),
                "Es sollte ein augmentierender Pfad ueber eine Residualkante verwendet werden."
        );
    }
}
