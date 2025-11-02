package graph.scc;

import graph.common.Graph;
import graph.common.Metrics;
import java.util.*;

public class SCCFinder {
    private final Graph graph;
    private final Metrics metrics;
    private boolean[] visited;
    private Stack<Integer> stack;
    private List<List<Integer>> components;

    public SCCFinder(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public SCCResult findSCCs() {
        int n = graph.getNodeCount();
        visited = new boolean[n];
        stack = new Stack<>();
        components = new ArrayList<>();

        metrics.startTimer();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
        }

        Graph transpose = createTransposeGraph();

        Arrays.fill(visited, false);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfsSecondPass(transpose, node, component);
                components.add(component);
            }
        }

        metrics.stopTimer();

        return new SCCResult(components, buildCondensationGraph(components));
    }

    private void dfsFirstPass(int node) {
        metrics.incrementDfsVisits();
        visited[node] = true;

        for (Graph.Edge edge : graph.getEdges(node)) {
            if (!visited[edge.v]) {
                dfsFirstPass(edge.v);
            }
        }

        stack.push(node);
    }

    private void dfsSecondPass(Graph transpose, int node, List<Integer> component) {
        metrics.incrementDfsVisits();
        visited[node] = true;
        component.add(node);

        for (Graph.Edge edge : transpose.getEdges(node)) {
            if (!visited[edge.v]) {
                dfsSecondPass(transpose, edge.v, component);
            }
        }
    }

    private Graph createTransposeGraph() {
        Graph transpose = new Graph(graph.getNodeCount(), true);
        for (int u = 0; u < graph.getNodeCount(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                transpose.addEdge(edge.v, edge.u, edge.weight);
            }
        }
        return transpose;
    }

    private Graph buildCondensationGraph(List<List<Integer>> components) {
        int compCount = components.size();
        Graph condensation = new Graph(compCount, true);

        int[] compIndex = new int[graph.getNodeCount()];
        for (int i = 0; i < components.size(); i++) {
            for (int node : components.get(i)) {
                compIndex[node] = i;
            }
        }

        for (int u = 0; u < graph.getNodeCount(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                int compU = compIndex[u];
                int compV = compIndex[edge.v];
                if (compU != compV) {
                    condensation.addEdge(compU, compV, edge.weight);
                }
            }
        }

        return condensation;
    }

    public static class SCCResult {
        public final List<List<Integer>> components;
        public final Graph condensationGraph;

        public SCCResult(List<List<Integer>> components, Graph condensationGraph) {
            this.components = components;
            this.condensationGraph = condensationGraph;
        }
    }
}