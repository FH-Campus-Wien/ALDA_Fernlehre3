package at.ac.hcw.maxflow;

import java.util.List;

public record MaxFlowResult<V>(int maxFlow, List<String> augmentingPaths) {
    public MaxFlowResult {
        augmentingPaths = List.copyOf(augmentingPaths);
    }
}
