package at.ac.hcw;

import at.ac.hcw.maxflow.FordFulkerson;
import at.ac.hcw.maxflow.MaxFlowResult;

public class MaxFlowCli {
    public static void main(String[] args) {
        FordFulkerson<String> fordFulkerson = new FordFulkerson<>();
        MaxFlowResult<String> result = fordFulkerson.computeMaxFlow(
                SampleNetworks.defaultNetwork(),
                SampleNetworks.SOURCE,
                SampleNetworks.SINK
        );

        System.out.println("Maximaler Fluss: " + result.maxFlow());
        System.out.println("Verwendete augmentierende Pfade:");
        for (String path : result.augmentingPaths()) {
            System.out.println(" - " + path);
        }
    }
}
