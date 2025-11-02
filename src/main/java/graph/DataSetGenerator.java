package graph;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.*;

public class DataSetGenerator {
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        generateAllDatasets();
    }

    public static void generateAllDatasets() {
        generateDataset("small_cyclic_1", 8, 12, true, 0.3);
        generateDataset("small_dag_1", 7, 10, false, 0.0);
        generateDataset("small_mixed_1", 9, 15, true, 0.2);

        generateDataset("medium_cyclic_1", 15, 25, true, 0.4);
        generateDataset("medium_dag_1", 12, 20, false, 0.0);
        generateDataset("medium_mixed_1", 18, 30, true, 0.3);

        generateDataset("large_cyclic_1", 30, 60, true, 0.5);
        generateDataset("large_dag_1", 25, 45, false, 0.0);
        generateDataset("large_mixed_1", 40, 80, true, 0.4);
    }

    private static void generateDataset(String name, int n, int edgeCount, boolean allowCycles, double cycleProbability) {
        Map<String, Object> graphData = new HashMap<>();
        graphData.put("directed", true);
        graphData.put("n", n);
        graphData.put("source", 0);
        graphData.put("weight_model", "edge");

        List<Map<String, Object>> edges = new ArrayList<>();

        if (allowCycles) {
            generateMixedGraph(edges, n, edgeCount, cycleProbability);
        } else {
            generateDAG(edges, n, edgeCount);
        }

        graphData.put("edges", edges);

        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("data/" + name + ".json");
            file.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, graphData);
            System.out.println("Generated: " + name + ".json (n=" + n + ", edges=" + edges.size() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateMixedGraph(List<Map<String, Object>> edges, int n, int edgeCount, double cycleProbability) {
        generateDAG(edges, n, edgeCount / 2);

        int remainingEdges = edgeCount - edges.size();
        for (int i = 0; i < remainingEdges; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);

            if (random.nextDouble() < cycleProbability && u != v) {
                edges.add(createEdge(u, v, random.nextInt(10) + 1));
            } else {
                int min = Math.min(u, v);
                int max = Math.max(u, v);
                if (random.nextBoolean()) {
                    edges.add(createEdge(min, max, random.nextInt(10) + 1));
                } else {
                    edges.add(createEdge(max, min, random.nextInt(10) + 1));
                }
            }
        }
    }

    private static void generateDAG(List<Map<String, Object>> edges, int n, int edgeCount) {
        for (int i = 0; i < edgeCount; i++) {
            int u = random.nextInt(n - 1);
            int v = u + 1 + random.nextInt(n - u - 1);
            edges.add(createEdge(u, v, random.nextInt(10) + 1));
        }
    }

    private static Map<String, Object> createEdge(int u, int v, int w) {
        Map<String, Object> edge = new HashMap<>();
        edge.put("u", u);
        edge.put("v", v);
        edge.put("w", w);
        return edge;
    }
}