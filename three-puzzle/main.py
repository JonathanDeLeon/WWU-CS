#!/usr/bin/env python
# Jonathan De Leon
# CPTR 430 Artificial Intelligence
# Homework 3
# April 23, 2018
#
# Problem:
# 3-Puzzle solution using BFS, Branch and Bound with heuristics and with dynamic programming
import collections


class PuzzleState:
    '''
    Class to represent the state of the 3-Puzzle
    '''

    def __init__(self, state, prev_states=None, cost=None):
        self.state = state
        self.free_tile = self.state.index(None)
        self.width = 2  # 2x2 board; total of 4 elements
        self.prev_states = prev_states or []
        self.cost = cost

    def expand_children(self):
        '''
        Yield a python generator with expanded children states
        '''
        # Move tile up
        if self.free_tile - self.width >= 0:
            yield PuzzleState(self.move(self.free_tile - self.width), self.prev_states + [self])

        # Move tile down
        if self.free_tile + self.width < len(self.state):
            yield PuzzleState(self.move(self.free_tile + self.width), self.prev_states + [self])

        # Move tile left if row numbers are equal
        if (self.free_tile - 1) // self.width == (self.free_tile) // self.width:
            yield PuzzleState(self.move(self.free_tile - 1), self.prev_states + [self])

        # Move tile right if row numbers are equal
        if (self.free_tile + 1) // self.width == (self.free_tile) // self.width:
            yield PuzzleState(self.move(self.free_tile + 1), self.prev_states + [self])

    def move(self, dest_tile):
        '''
        Create a new list state swapping the free tile and destination tile
        '''
        state = self.state[:]
        state[self.free_tile], state[dest_tile] = state[dest_tile], state[self.free_tile]
        return state

    @property
    def goal(self):
        '''
        Puzzle is at goal if numbers are increasing with free tile 'None' last element
        '''
        return self.state == [1, 2, 3, None]

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        '''
        Pretty print the state of the puzzle
        '''
        return "\n".join(str(self.state[start: start + self.width])
                         for start in range(0, len(self.state), self.width))

    def __eq__(self, other):
        '''
        Needed to use a set()
        '''
        return self.state == other.state

    def __hash__(self):
        '''
        Needed to use a set()
        '''
        h = 0
        for index, value in enumerate(self.state):
            if value is None:
                value = 0
            h ^= value << index
        return h


class PuzzleSolver:
    '''
    The 3-Puzzle solver.
    'initial' is the root PuzzleState
    '''

    def __init__(self, initial):
        self.initial = initial

    def solve(self):
        '''
        Use BFS and return solution PuzzleState
        '''
        queue = collections.deque([self.initial])
        visited = set()

        while queue:
            node = queue.pop()
            visited.add(node)
            if node.goal:
                return node

            for puzzle in node.expand_children():
                if puzzle not in visited and puzzle not in queue:
                    queue.appendleft(puzzle)


if __name__ == "__main__":
    # Use initial state found in book 
    puzzle = PuzzleState([3, 1, 2, None])
    solution = PuzzleSolver(puzzle).solve()
    if solution is not None:
        for state in solution.prev_states:
            print(state, end='\n\n')
    print(solution)
    # print("\n====================================")
    # print("Initial root node")
    # print(puzzle.initial)
    # print("====================================")
    # print("Solution")
    # print(puzzle.find_solution())
    # print("====================================")
    # print("List of visited nodes before finding solution")
    # print(puzzle.visited)
    # print("====================================", end='\n\n')
