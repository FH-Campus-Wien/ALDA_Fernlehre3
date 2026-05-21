package at.ac.hcw;

import at.ac.hcw.maxflow.FlowNetwork;

public final class SampleNetworks {
    public static final String SOURCE = "s";
    public static final String SINK = "t";

    private SampleNetworks() {
    }

    public static FlowNetwork<String> defaultNetwork() {
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
        return network;
    }
}
