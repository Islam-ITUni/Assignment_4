package graph;

import graph.common.Graph;
import graph.common.Metrics;
import graph.scc.SCCFinder;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class SCCTest {
    
    @Test
    public void testSimpleSCC() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult result = sccFinder.findSCCs();
        
        assertEquals(3, result.components.size());
        
        boolean foundCycleComponent = false;
        for (List<Integer> component : result.components) {
            if (component.contains(0) && component.contains(1) && component.contains(2)) {
                foundCycleComponent = true;
                assertEquals(3, component.size());
                break;
            }
        }
        assertTrue(foundCycleComponent);
    }
    
    @Test
    public void testSingleNodeSCC() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        
        Metrics metrics = new Metrics();
        SCCFinder sccFinder = new SCCFinder(graph, metrics);
        SCCFinder.SCCResult result = sccFinder.findSCCs();
        
        assertEquals(3, result.components.size());
        for (List<Integer> component : result.components) {
            assertEquals(1, component.size());
        }
    }
}