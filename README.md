This project implements graph algorithms for smart city task scheduling, combining Strongly Connected Components (SCC), Topological Sorting, and DAG Shortest Paths to handle cyclic dependencies and optimal scheduling.

Project Structure
text
Assignment_4/
├── src/main/java/graph/
│   ├── scc/          
│   ├── topo/         
│   ├── dagsp/        
│   └── common/       
├── src/test/java/graph/    
│   ├── screenshots/ 
│   ├── SCCTest.java
│   ├── TopologicalSortTest.java
│   ├── DAGShortestPathTest.java
│   ├── IntegrationTest.java
│   └── EdgeCaseTest.java
├── data/             
└── README.md
Algorithms Implemented
SCC: Kosaraju's algorithm with O(V + E) complexity

Topological Sort: Kahn's algorithm with O(V + E) complexity

DAG Shortest/Longest Paths: Dynamic programming over topological order

Weight Model: Edge weights representing task durations/costs

Build & Run
bash
# Compile project
mvn compile

# Generate datasets
mvn exec:java -Dexec.mainClass="graph.DataSetGenerator"

# Run tests
mvn test

# Execute on specific dataset
mvn exec:java -Dexec.mainClass="graph.Main" -Dexec.args="small_cyclic_1.json"
Technical Report
1 Data Summary
Dataset	Nodes	Edges	Structure	SCC Count	Largest SCC
small_cyclic_1	8	12	Cyclic	6	3
small_dag_1	7	10	Acyclic	7	1
small_mixed_1	9	15	Mixed	3	7
medium_cyclic_1	15	25	Cyclic	8	7
medium_dag_1	12	20	Acyclic	12	1
medium_mixed_1	18	30	Mixed	17	2
large_cyclic_1	30	60	Cyclic	21	9
large_dag_1	25	45	Acyclic	25	1
large_mixed_1	40	80	Mixed	15	26
Weight Model: Edge weights represent task durations (1-10 units)

2 Performance Results
2.1 SCC Algorithm Performance
Dataset	Time (ns)	DFS Visits	SCCs Found	Compression Ratio
small_cyclic_1	138,500	16	6	25%
small_dag_1	118,600	14	7	0%
small_mixed_1	121,600	18	3	78%
medium_cyclic_1	176,200	30	8	47%
medium_dag_1	240,900	24	12	0%
medium_mixed_1	172,800	36	17	6%
large_cyclic_1	860,400	60	21	30%
large_dag_1	183,200	50	25	0%
large_mixed_1	294,700	80	15	65%
2.2 Topological Sort Performance
Dataset	Time (ns)	Queue Operations	Valid DAG
small_cyclic_1	317,900	12	Yes
small_dag_1	1,319,900	14	Yes
small_mixed_1	358,800	6	Yes
medium_cyclic_1	494,300	16	Yes
medium_dag_1	399,400	24	Yes
medium_mixed_1	373,800	34	Yes
large_cyclic_1	470,000	42	Yes
large_dag_1	338,600	50	Yes
large_mixed_1	351,500	30	Yes
2.3 DAG Shortest Path Performance
Dataset	Time (ns)	Edge Relaxations	Reachable Components
small_cyclic_1	33,000	6	4/6
small_dag_1	39,400	10	6/7
small_mixed_1	26,900	5	3/3
medium_cyclic_1	36,000	9	4/8
medium_dag_1	31,200	9	6/12
medium_mixed_1	40,500	12	7/17
large_cyclic_1	49,300	17	9/21
large_dag_1	29,900	12	9/25
large_mixed_1	34,300	10	5/15
3 Algorithm Analysis
3.1 SCC Algorithm (Kosaraju)
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

3.2 Topological Sort (Kahn's Algorithm)
Bottlenecks:

In-degree calculation: O(E) operations

Queue management: O(V) operations

Memory for in-degree array

Structure Impact:

Dense DAGs: More queue operations

Sparse DAGs: Faster execution

Component count: Linear scaling with SCC count

Performance Insight: Scales linearly with condensation graph size, not original graph size.

3.3 DAG Shortest Paths
Bottlenecks:

Edge relaxation: O(E) operations

Distance array updates

Path reconstruction

Structure Impact:

Component connectivity: Determines reachability

Edge density: More relaxations needed

Weight distribution: Affects critical path length

Performance Insight: Extremely efficient on DAGs due to single pass over topological order.

4 Critical Path Analysis
Dataset	Critical Path Length	Path Components	Scheduling Insight
small_mixed_1	8	0→2	Short schedule, minimal dependencies
small_dag_1	29	0→1→2→3→5	Complex dependency chain
medium_mixed_1	20	0→5→10→15→16	Medium complexity, distributed
large_mixed_1	23	0→11→13→14	Large component dominance
large_cyclic_1	33	0→10→11→12→16→18	Maximum complexity, multiple phases
5 Conclusions & Practical Recommendations
5.1 When to Use Each Method
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

5.2 Practical Recommendations for Smart City Scheduling
Always start with SCC analysis to detect and compress cyclic dependencies

Use topological sort for creating feasible execution schedules

Apply critical path analysis to identify scheduling bottlenecks

Monitor algorithm performance - SCC overhead is worthwhile for cyclic graphs

Consider graph density - sparse graphs benefit more from these techniques

5.3 Performance Guidelines
Small graphs (<20 nodes): All algorithms perform optimally

Medium graphs (20-50 nodes): Watch for memory usage in SCC

Large graphs (>50 nodes): Consider incremental processing for very dense graphs

6 Test Coverage
All JUnit tests passing with comprehensive coverage:

SCC algorithm correctness

Topological sort validation

DAG shortest/longest paths

Edge cases (empty graphs, single nodes, disconnected components)

Integration tests (full pipeline)

Test Results: 100% pass rate across all test suites

