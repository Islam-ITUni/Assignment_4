## Project Overview
This project implements graph algorithms for smart city task scheduling.

## Project Structure
Assignment_4/
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

## Algorithms Implemented
- SCC: Kosaraju's algorithm with O(V + E) complexity
- Topological Sort: Kahn's algorithm with O(V + E) complexity
- DAG Shortest/Longest Paths: Dynamic programming over topological order
- Weight Model: Edge weights representing task durations

## Build and Run
```bash
mvn compile
mvn exec:java -Dexec.mainClass="graph.DataSetGenerator"
mvn test
mvn exec:java -Dexec.mainClass="graph.Main" -Dexec.args="small_cyclic_1.json"
Technical Report
Data Summary
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
Performance Results
SCC Algorithm Performance
Dataset	Time (ns)	DFS Visits	SCCs Found	Compression Ratio
small_cyclic_1	138500	16	6	25%
small_dag_1	118600	14	7	0%
small_mixed_1	121600	18	3	78%
medium_cyclic_1	176200	30	8	47%
medium_dag_1	240900	24	12	0%
medium_mixed_1	172800	36	17	6%
large_cyclic_1	860400	60	21	30%
large_dag_1	183200	50	25	0%
large_mixed_1	294700	80	15	65%
Topological Sort Performance
Dataset	Time (ns)	Queue Operations	Valid DAG
small_cyclic_1	317900	12	Yes
small_dag_1	1319900	14	Yes
small_mixed_1	358800	6	Yes
medium_cyclic_1	494300	16	Yes
medium_dag_1	399400	24	Yes
medium_mixed_1	373800	34	Yes
large_cyclic_1	470000	42	Yes
large_dag_1	338600	50	Yes
large_mixed_1	351500	30	Yes
DAG Shortest Path Performance
Dataset	Time (ns)	Edge Relaxations	Reachable Components
small_cyclic_1	33000	6	4/6
small_dag_1	39400	10	6/7
small_mixed_1	26900	5	3/3
medium_cyclic_1	36000	9	4/8
medium_dag_1	31200	9	6/12
medium_mixed_1	40500	12	7/17
large_cyclic_1	49300	17	9/21
large_dag_1	29900	12	9/25
large_mixed_1	34300	10	5/15
Critical Path Analysis
Dataset	Critical Path Length	Path Components	Scheduling Insight
small_mixed_1	8	0→2	Short schedule, minimal dependencies
small_dag_1	29	0→1→2→3→5	Complex dependency chain
medium_mixed_1	20	0→5→10→15→16	Medium complexity, distributed
large_mixed_1	23	0→11→13→14	Large component dominance
large_cyclic_1	33	0→10→11→12→16→18	Maximum complexity, multiple phases
Algorithm Analysis
SCC Algorithm (Kosaraju)
Bottlenecks: Graph transposition, two DFS passes, memory usage

Structure Impact: Best compression on cyclic graphs (up to 78%)

Performance: Most efficient on highly cyclic graphs

Topological Sort (Kahn's Algorithm)
Bottlenecks: In-degree calculation, queue management

Structure Impact: Scales with condensation graph size

Performance: Linear scaling with component count

DAG Shortest Paths
Bottlenecks: Edge relaxation, distance updates

Structure Impact: Connectivity determines reachability

Performance: Extremely efficient due to single topological pass

Conclusions
When to Use Each Method
Algorithm	Use Case	Best For	Avoid When
SCC Compression	Cyclic dependencies	Mutual dependencies	Pure DAG structure
Topological Sort	Acyclic dependencies	Execution ordering	Uncompressed cyclic graphs
DAG Shortest Paths	Optimal scheduling	Critical path analysis	Non-DAG structures
Practical Recommendations
Start with SCC analysis for cyclic dependency detection

Use topological sort for execution ordering

Apply critical path analysis for bottleneck identification

Consider graph density for algorithm selection

Performance Guidelines
Small graphs (<20 nodes): All algorithms perform optimally

Medium graphs (20-50 nodes): Monitor memory usage

Large graphs (>50 nodes): Consider incremental processing

Test Coverage
SCC algorithm correctness

Topological sort validation

DAG shortest/longest paths

Edge cases and integration tests

All tests passing (100% success rate)
