# ALDA Assignment 3: Ford-Fulkerson

Implement the Ford-Fulkerson algorithm for computing a maximum flow in a directed flow network.

## Task

Complete the methods in `src/main/java/at/ac/hcw/maxflow/FordFulkerson.java`.

Your implementation must:

- find augmenting paths using depth-first search,
- determine the bottleneck capacity of each path,
- update forward and residual edges correctly,
- return the maximum flow value,
- record the augmenting paths in the result.

The provided tests also check edge cases such as unreachable sinks, missing source or sink vertices, negative capacities, and residual-edge rerouting.

## Requirements

- Java 21
- Gradle wrapper included in this repository

## Run

Run all tests:

```bash
./gradlew test
```

Run the console demo:

```bash
./gradlew runCli
```

Run the JavaFX visualization:

```bash
./gradlew run
```
