# Farmer Wolf Goat Cabbage Problem

Assignment #2 for *CPTR 430 Artificial Intelligence*.

## Problem

A farmer with a wolf, a goat, and a container of cabbage are on the west bank of the river.
On the river is a boat in which the farmer and one of the other three (wolf, goat, or cabbage)
can fit. If the wolf is left alone with the goat, the wolf will eat the goat.
If the goat is left alone with the container of cabbage, the goat will eat the cabbage.
Your goal is to transfer everyone to the other side of the river safely.

## Getting Started

This assignment was done in python 3.6. It is also implemented using a DFS strategy or list with
LIFO operations (stack).

```python
python3 main.py
```

## Output

Solution is a python dictionary with the appropriate entity states stated either *WEST* or *EAST* for
each of the four entities (Farmer, Wolf, Goat, Cabbage).

### Sample
```python
====================================
Initial root node
{'cabbage': WEST, 'wolf': WEST, 'goat': WEST, 'farmer': WEST}
====================================
Solution
{'cabbage': EAST, 'wolf': EAST, 'goat': EAST, 'farmer': EAST}
====================================
List of visited nodes before finding solution
[{'cabbage': WEST, 'wolf': WEST, 'goat': WEST, 'farmer': WEST}, {'cabbage': WEST, 'wolf': WEST, 'goat': EAST, 'farmer': EAST}, {'cabbage': WEST, 'wolf': WEST, 'goat': EAST, 'farmer': WEST}, {'cabbage': WEST, 'wolf': EAST, 'goat': EAST, 'farmer': EAST}, {'cabbage': WEST, 'wolf': EAST, 'goat': WEST, 'farmer': WEST}, {'cabbage': EAST, 'wolf': EAST, 'goat': WEST, 'farmer': EAST}, {'cabbage': EAST, 'wolf': EAST, 'goat': WEST, 'farmer': WEST}]
====================================
```