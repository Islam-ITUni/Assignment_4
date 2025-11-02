package graph.dagsp;

import graph.common.Graph;
import graph.common.Metrics;
import java.util.*;

public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;

    public DAGShortestPath(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public ShortestPathResult shortestPathFromSource(int source, List<Integer> topoOrder) {
        int n = graph.getNodeCount();
        int[] dist = new int[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        metrics.startTimer();

        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    metrics.incrementEdgeRelaxations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist < dist[edge.v]) {
                        dist[edge.v] = newDist;
                        prev[edge.v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();

        return new ShortestPathResult(dist, prev, source);
    }

    public LongestPathResult longestPathFromSource(int source, List<Integer> topoOrder) {
        int n = graph.getNodeCount();
        int[] dist = new int[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        metrics.startTimer();

        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    metrics.incrementEdgeRelaxations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist > dist[edge.v]) {
                        dist[edge.v] = newDist;
                        prev[edge.v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();

        return new LongestPathResult(dist, prev, source);
    }

    public static class ShortestPathResult {
        public final int[] distances;
        public final int[] previous;
        public final int source;

        public ShortestPathResult(int[] distances, int[] previous, int source) {
            this.distances = distances;
            this.previous = previous;
            this.source = source;
        }

        public List<Integer> reconstructPath(int target) {
            if (distances[target] == Integer.MAX_VALUE) {
                return Collections.emptyList();
            }

            List<Integer> path = new ArrayList<>();
            for (int at = target; at != -1; at = previous[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static class LongestPathResult {
        public final int[] distances;
        public final int[] previous;
        public final int source;

        public LongestPathResult(int[] distances, int[] previous, int source) {
            this.distances = distances;
            this.previous = previous;
            this.source = source;
        }

        public List<Integer> reconstructPath(int target) {
            if (distances[target] == Integer.MIN_VALUE) {
                return Collections.emptyList();
            }

            List<Integer> path = new ArrayList<>();
            for (int at = target; at != -1; at = previous[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }

        public CriticalPathResult findCriticalPath() {
            int maxDist = Integer.MIN_VALUE;
            int endNode = -1;

            for (int i = 0; i < distances.length; i++) {
                if (distances[i] > maxDist && distances[i] != Integer.MIN_VALUE) {
                    maxDist = distances[i];
                    endNode = i;
                }
            }

            if (endNode == -1) {
                return new CriticalPathResult(Collections.emptyList(), 0);
            }

            return new CriticalPathResult(reconstructPath(endNode), maxDist);
        }
    }

    public static class CriticalPathResult {
        public final List<Integer> path;
        public final int length;

        public CriticalPathResult(List<Integer> path, int length) {
            this.path = path;
            this.length = length;
        }
    }
}