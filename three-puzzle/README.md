# 3-Puzzle

Assignment #3 for *CPTR 430 Artificial Intelligence*.

## Problem

The goal of this assignment was to write a program to solve the 3-Puzzle Tile in the following ways:
* Using BFS
* Branch and Bound with past cost (depth)
* Branch and Bound with simple heuristic (number of tiles out of place) and past cost
* Branch and Bound with refined heuristic (sum of moves needed for each tile) and past cost
* Branch and Bound with past cost, refined heuristic, and dynamic programming (A* Search)

## Getting Started

This assignment was done in python 3.6.

```python
python3 main.py
```

## Output

Program will output 5 different solutions to the problem using the 5 different implementations.
The goal state of the puzzle is `[1, 2, 3, None]` where *None* is the location of the free "move-able tile".
The program will ask for user input to continue on with the next algorithm until all solutions have been displayed.
