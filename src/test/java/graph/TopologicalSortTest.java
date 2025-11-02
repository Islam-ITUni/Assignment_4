package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.topo.TopologicalSort;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TopologicalSortTest {
    
    @Test
    public void testSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult result = topoSort.topologicalOrder();
        
        assertTrue(result.isDAG);
        assertEquals(4, result.order.size());
        
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < result.order.size(); i++) {
            position.put(result.order.get(i), i);
        }
        
        assertTrue(position.get(0) < position.get(1));
        assertTrue(position.get(0) < position.get(2));
        assertTrue(position.get(1) < position.get(3));
        assertTrue(position.get(2) < position.get(3));
    }
}