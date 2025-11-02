package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class EdgeCaseTest {
    
    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(0, true);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertEquals(0, sccResult.components.size());
    }
    
    @Test
    public void testSingleNodeNoEdges() {
        Graph graph = new Graph(1, true);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertEquals(1, sccResult.components.size());
        assertEquals(1, sccResult.components.get(0).size());
        
        TopologicalSort topoSort = new TopologicalSort(graph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        assertTrue(topoResult.isDAG);
        assertEquals(1, topoResult.order.size());
    }
    
    @Test
    public void testDisconnectedGraph() {
        Graph graph = new Graph(3, true);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertEquals(3, sccResult.components.size());
        
        TopologicalSort topoSort = new TopologicalSort(sccResult.condensationGraph, metrics);
        TopologicalSort.TopoResult topoResult = topoSort.topologicalOrder();
        
        assertTrue(topoResult.isDAG);
        assertEquals(3, topoResult.order.size());
    }
    
    @Test
    public void testSelfLoop() {
        Graph graph = new Graph(2, true);
        graph.addEdge(0, 0, 1);
        graph.addEdge(0, 1, 1);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult sccResult = sccFinder.findSCCs();
        
        assertTrue(sccResult.components.size() >= 1);
    }
}