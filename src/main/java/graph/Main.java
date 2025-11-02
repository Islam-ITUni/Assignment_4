package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java graph.Main <input_json_file>");
            return;
        }

        try {
            String filename = args[0];
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> graphData = mapper.readValue(
                    new File("data/" + filename),
                    new TypeReference<Map<String, Object>>() {}
            );

            int n = (Integer) graphData.get("n");
            List<Map<String, Object>> edges = (List<Map<String, Object>>) graphData.get("edges");
            int source = (Integer) graphData.get("source");

            Graph graph = new Graph(n, true);
            for (Map<String, Object> edge : edges) {
                int u = (Integer) edge.get("u");
                int v = (Integer) edge.get("v");
                int w = (Integer) edge.get("w");
                graph.addEdge(u, v, w);
            }

            Metrics metrics = new Metrics();

            System.out.println("=== Strongly Connected Components ===");
            SCCFinder sccFinder = new SCCFinder(graph, metrics);
            SCCFinder.SCCResult sccResult = sccFinder.findSCCs();

            System.out.println("Found " + sccResult.components.size() + " SCCs:");
            for (int i = 0; i < sccResult.components.size(); i++) {
                System.out.println("SCC " + i + ": " + sccResult.components.get(i) +
                        " (size: " + sccResult.components.get(i).size() + ")");
            }
            System.out.println("SCC Metrics - Time: " + metrics.getElapsedTime() + "ns, " +
                    "DFS Visits: " + metrics.getDfsVisits());

            metrics.reset();

            System.out.println("\n=== Topological Sort on Condensation Graph ===");
            TopologicalSort topoSort = new TopologicalSort(sccResult.condensationGraph, metrics);
            TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();

            if (topoResult.isDAG) {
                System.out.println("Topological Order: " + topoResult.order);
                System.out.println("Topo Sort Metrics - Time: " + metrics.getElapsedTime() + "ns, " +
                        "Queue Operations: " + metrics.getQueueOperations());

                metrics.reset();

                System.out.println("\n=== Shortest Paths in Condensation DAG ===");
                DAGShortestPath dagSP = new DAGShortestPath(sccResult.condensationGraph, metrics);
                DAGShortestPath.ShortestPathResult spResult =
                        dagSP.shortestPathFromSource(0, topoResult.order);

                System.out.println("Shortest distances from source component 0:");
                for (int i = 0; i < spResult.distances.length; i++) {
                    if (spResult.distances[i] != Integer.MAX_VALUE) {
                        System.out.println("  to component " + i + ": " + spResult.distances[i]);
                    } else {
                        System.out.println("  to component " + i + ": unreachable");
                    }
                }

                System.out.println("Shortest Path Metrics - Time: " + metrics.getElapsedTime() + "ns, " +
                        "Edge Relaxations: " + metrics.getEdgeRelaxations());

                metrics.reset();

                System.out.println("\n=== Longest Path in Condensation DAG ===");
                DAGShortestPath.LongestPathResult lpResult =
                        dagSP.longestPathFromSource(0, topoResult.order);
                DAGShortestPath.CriticalPathResult criticalPath = lpResult.findCriticalPath();

                System.out.println("Critical Path through components: " + criticalPath.path);
                System.out.println("Critical Path Length: " + criticalPath.length);
                System.out.println("Longest Path Metrics - Time: " + metrics.getElapsedTime() + "ns, " +
                        "Edge Relaxations: " + metrics.getEdgeRelaxations());

            } else {
                System.out.println("Graph is not a DAG, cannot compute topological order");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}