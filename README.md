\# Assignment 4: Smart City Scheduling with Graph Algorithms



\## Overview

This project implements graph algorithms for smart city task scheduling, combining Strongly Connected Components (SCC), Topological Sorting, and DAG Shortest Paths to handle cyclic dependencies and optimal scheduling.



\## Project Structure

Assignment\_4/

├── src/main/java/graph/

│ ├── scc/ 

│ ├── topo/ 

│ ├── dagsp/ 

│ └── common/

├── src/test/java/graph/ 

│ ├── screenshots/ 

│ ├── SCCTest.java

│ ├── TopologicalSortTest.java

│ ├── DAGShortestPathTest.java

│ ├── IntegrationTest.java

│ └── EdgeCaseTest.java

├── data/ 

└── README.md



text



\## Algorithms Implemented

\- \*\*SCC\*\*: Kosaraju's algorithm with O(V + E) complexity

\- \*\*Topological Sort\*\*: Kahn's algorithm with O(V + E) complexity  

\- \*\*DAG Shortest/Longest Paths\*\*: Dynamic programming over topological order

\- \*\*Weight Model\*\*: Edge weights representing task durations/costs



\## Build \& Run

```bash

\# Compile project

mvn compile



\# Generate datasets

mvn exec:java -Dexec.mainClass="graph.DataSetGenerator"



\# Run tests

mvn test



\# Execute on specific dataset

mvn exec:java -Dexec.mainClass="graph.Main" -Dexec.args="small\_cyclic\_1.json"

Technical Report

1\. Data Summary

Dataset	Nodes	Edges	Structure	SCC Count	Largest SCC

small\_cyclic\_1	8	12	Cyclic	6	3

small\_dag\_1	7	10	Acyclic	7	1

small\_mixed\_1	9	15	Mixed	3	7

medium\_cyclic\_1	15	25	Cyclic	8	7

medium\_dag\_1	12	20	Acyclic	12	1

medium\_mixed\_1	18	30	Mixed	17	2

large\_cyclic\_1	30	60	Cyclic	21	9

large\_dag\_1	25	45	Acyclic	25	1

large\_mixed\_1	40	80	Mixed	15	26

Weight Model: Edge weights represent task durations (1-10 units)



2\. Performance Results

SCC Algorithm Performance

Dataset	Time (ns)	DFS Visits	SCCs Found	Compression Ratio

small\_cyclic\_1	138,500	16	6	25%

small\_dag\_1	118,600	14	7	0%

small\_mixed\_1	121,600	18	3	78%

medium\_cyclic\_1	176,200	30	8	47%

medium\_dag\_1	240,900	24	12	0%

medium\_mixed\_1	172,800	36	17	6%

large\_cyclic\_1	860,400	60	21	30%

large\_dag\_1	183,200	50	25	0%

large\_mixed\_1	294,700	80	15	65%

Topological Sort Performance

Dataset	Time (ns)	Queue Operations	Valid DAG

small\_cyclic\_1	317,900	12	Yes

small\_dag\_1	1,319,900	14	Yes

small\_mixed\_1	358,800	6	Yes

medium\_cyclic\_1	494,300	16	Yes

medium\_dag\_1	399,400	24	Yes

medium\_mixed\_1	373,800	34	Yes

large\_cyclic\_1	470,000	42	Yes

large\_dag\_1	338,600	50	Yes

large\_mixed\_1	351,500	30	Yes

DAG Shortest Path Performance

Dataset	Time (ns)	Edge Relaxations	Reachable Components

small\_cyclic\_1	33,000	6	4/6

small\_dag\_1	39,400	10	6/7

small\_mixed\_1	26,900	5	3/3

medium\_cyclic\_1	36,000	9	4/8

medium\_dag\_1	31,200	9	6/12

medium\_mixed\_1	40,500	12	7/17

large\_cyclic\_1	49,300	17	9/21

large\_dag\_1	29,900	12	9/25

large\_mixed\_1	34,300	10	5/15

3\. Algorithm Analysis

SCC Algorithm (Kosaraju)

Bottlenecks:



Graph transposition: O(E) operations



Two DFS passes: O(V + E) each



Memory usage for stack and visited arrays



Structure Impact:



Dense graphs: More DFS visits, better compression



Sparse graphs: Faster but less compression



Cyclic structures: Significant node reduction (up to 78%)



Pure DAGs: No compression (each node = own component)



Performance Insight: Most efficient on highly cyclic graphs where compression provides maximum benefit.



Topological Sort (Kahn's Algorithm)

Bottlenecks:



In-degree calculation: O(E) operations



Queue management: O(V) operations



Memory for in-degree array



Structure Impact:



Dense DAGs: More queue operations



Sparse DAGs: Faster execution



Component count: Linear scaling with SCC count



Performance Insight: Scales linearly with condensation graph size, not original graph size.



DAG Shortest Paths

Bottlenecks:



Edge relaxation: O(E) operations



Distance array updates



Path reconstruction



Structure Impact:



Component connectivity: Determines reachability



Edge density: More relaxations needed



Weight distribution: Affects critical path length



Performance Insight: Extremely efficient on DAGs due to single pass over topological order.



4\. Critical Path Analysis

Dataset	Critical Path Length	Path Components	Scheduling Insight

small\_mixed\_1	8	0→2	Short schedule, minimal dependencies

small\_dag\_1	29	0→1→2→3→5	Complex dependency chain

medium\_mixed\_1	20	0→5→10→15→16	Medium complexity, distributed

large\_mixed\_1	23	0→11→13→14	Large component dominance

large\_cyclic\_1	33	0→10→11→12→16→18	Maximum complexity, multiple phases

5\. Conclusions \& Practical Recommendations

When to Use Each Method:

SCC Compression:



Use when: Cyclic dependencies detected in task scheduling



Best for: Smart city services with mutual dependencies



Avoid when: Pure DAG structure (adds overhead)



Topological Sorting:



Use when: Task dependencies are acyclic or after SCC compression



Best for: Determining optimal task execution order



Essential for: Resource scheduling and deadlock prevention



DAG Shortest/Longest Paths:



Use when: Need optimal schedules or critical path identification



Best for: Project planning and bottleneck analysis



Critical for: Time estimation and resource allocation



Practical Recommendations for Smart City Scheduling:

Always start with SCC analysis to detect and compress cyclic dependencies



Use topological sort for creating feasible execution schedules



Apply critical path analysis to identify scheduling bottlenecks



Monitor algorithm performance - SCC overhead is worthwhile for cyclic graphs



Consider graph density - sparse graphs benefit more from these techniques



Performance Guidelines:

Small graphs (<20 nodes): All algorithms perform optimally



Medium graphs (20-50 nodes): Watch for memory usage in SCC



Large graphs (>50 nodes): Consider incremental processing for very dense graphs



6\. Test Coverage

All JUnit tests passing with comprehensive coverage:



SCC algorithm correctness



Topological sort validation



DAG shortest/longest paths



Edge cases (empty graphs, single nodes, disconnected components)



Integration tests (full pipeline)



Test Results: 100% pass rate across all test suites

