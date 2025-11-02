package graph.topo;

import graph.common.Graph;
import graph.common.Metrics;
import java.util.*;

public class TopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public TopologicalSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public TopoResult topologicalOrder() {
        int n = graph.getNodeCount();
        int[] inDegree = new int[n];

        metrics.startTimer();

        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueueOperations();
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueueOperations();
            topoOrder.add(u);

            for (Graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.v]--;
                if (inDegree[edge.v] == 0) {
                    queue.offer(edge.v);
                    metrics.incrementQueueOperations();
                }
            }
        }

        metrics.stopTimer();

        return new TopoResult(topoOrder, topoOrder.size() == n);
    }

    public static class TopoResult {
        public final List<Integer> order;
        public final boolean isDAG;

        public TopoResult(List<Integer> order, boolean isDAG) {
            this.order = order;
            this.isDAG = isDAG;
        }
    }
}