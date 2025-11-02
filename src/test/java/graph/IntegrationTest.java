package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IntegrationTest {
    
    @Test
    public void testFullPipelineCyclicGraph() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        
        Metrics metrics = new Metrics();
        
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertEquals(3, sccResult.components.size());
        
        TopologicalSort topoSort = new TopologicalSort(sccResult.condensationGraph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        assertTrue(topoResult.isDAG);
        
        DAGShortestPath dagSP = new DAGShortestPath(sccResult.condensationGraph, metrics);
        DAGShortestPath.ShortestPathResult spResult = dagSP.shortestPathFromSource(0, topoResult.order);
        
        assertTrue(spResult.distances[0] >= 0);
    }
    
    @Test
    public void testFullPipelineDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        Metrics metrics = new Metrics();
        
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertEquals(4, sccResult.components.size());
        
        TopologicalSort topoSort = new TopologicalSort(sccResult.condensationGraph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        assertTrue(topoResult.isDAG);
        
        DAGShortestPath dagSP = new DAGShortestPath(sccResult.condensationGraph, metrics);
        DAGShortestPath.LongestPathResult lpResult = dagSP.longestPathFromSource(0, topoResult.order);
        DAGShortestPath.CriticalPathResult criticalPath = lpResult.findCriticalPath();
        
        assertTrue(criticalPath.length >= 0);
    }
}