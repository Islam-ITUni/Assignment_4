package graph.common;

import java.util.*;

public class Graph {
    private final int n;
    private final List<List<Edge>> adj;
    private final boolean directed;

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int weight) {
        adj.get(u).add(new Edge(u, v, weight));
        if (!directed) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public List<Edge> getEdges(int u) {
        return adj.get(u);
    }

    public int getNodeCount() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public static class Edge {
        public final int u;
        public final int v;
        public final int weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return u + "->" + v + "(" + weight + ")";
        }
    }
}