package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.dagsp.DAGShortestPath;
import graph.topo.TopologicalSort;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class DAGShortestPathTest {
    
    @Test
    public void testShortestPathSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 1);
        
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        DAGShortestPath dagSP = new DAGShortestPath(graph, metrics);
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPathFromSource(0, topoResult.order);
        
        assertEquals(0, result.distances[0]);
        assertEquals(2, result.distances[1]);
        assertEquals(1, result.distances[2]);
        assertEquals(2, result.distances[3]);
    }
    
    @Test
    public void testLongestPathSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 1);
        
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        DAGShortestPath dagSP = new DAGShortestPath(graph, metrics);
        DAGShortestPath.LongestPathResult result = dagSP.longestPathFromSource(0, topoResult.order);
        DAGShortestPath.CriticalPathResult criticalPath = result.findCriticalPath();
        
        assertEquals(5, criticalPath.length);
        assertTrue(criticalPath.path.contains(0));
        assertTrue(criticalPath.path.contains(1));
        assertTrue(criticalPath.path.contains(3));
    }
    
    @Test
    public void testUnreachableNodes() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        DAGShortestPath dagSP = new DAGShortestPath(graph, metrics);
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPathFromSource(0, topoResult.order);
        
        assertEquals(0, result.distances[0]);
        assertEquals(1, result.distances[1]);
        assertEquals(Integer.MAX_VALUE, result.distances[2]);
    }
    
    @Test
    public void testSingleNodeGraph() {
        Graph graph = new Graph(1, true);
        
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        DAGShortestPath dagSP = new DAGShortestPath(graph, metrics);
        DAGShortestPath.ShortestPathResult result = dagSP.shortestPathFromSource(0, topoResult.order);
        
        assertEquals(0, result.distances[0]);
    }
}