#!/usr/bin/env python3
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

    def __init__(self, state, prev_states=None, cost_method=None):
        self.state = state
        self.free_tile = self.state.index(None)
        self.width = 2  # 2x2 board; total of 4 elements
        self.prev_states = prev_states or []
        self.cost = cost_method(self.state) if cost_method is not None else None

    def expand_children(self, cost_method=None):
        '''
        Yield a python generator with expanded children states
        '''
        parents = self.prev_states + [self]
        # Move tile up
        if self.free_tile - self.width >= 0:
            yield PuzzleState(self.move(self.free_tile - self.width), parents, cost_method)

        # Move tile down
        if self.free_tile + self.width < len(self.state):
            yield PuzzleState(self.move(self.free_tile + self.width), parents, cost_method)

        # Move tile left if row numbers are equal
        if (self.free_tile - 1) // self.width == (self.free_tile) // self.width:
            yield PuzzleState(self.move(self.free_tile - 1), parents, cost_method)

        # Move tile right if row numbers are equal
        if (self.free_tile + 1) // self.width == (self.free_tile) // self.width:
            yield PuzzleState(self.move(self.free_tile + 1), parents, cost_method)

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
        to_string = "\n".join(str(self.state[start: start + self.width])
                              for start in range(0, len(self.state), self.width))
        if self.cost is not None:
            to_string += "\nCost|Heuristic: " + str(self.cost)
        return to_string

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

    def solve_BFS(self):
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

    def solve_uniform_cost_search(self):
        '''
        Use Uniform Cost Search and return solution PuzzleState
        Cost is the number of steps needed to get to the state
        '''
        queue = collections.deque([self.initial])

        while queue:
            node = queue.pop()
            if node.goal:
                return node

            for puzzle in node.expand_children(cost_method=lambda state: depth(len(node.prev_states))):
                queue.appendleft(puzzle)
            # Sort queue by cost
            queue = collections.deque(sorted(queue, key=lambda state: state.cost, reverse=True))

    def solve_simple_heuristic(self):
        '''
        Use Branch and Bound and return solution PuzzleState
        Cost is the number of steps needed to get to the state plus number of tiles out of place
        '''
        queue = collections.deque([self.initial])

        while queue:
            node = queue.pop()
            if node.goal:
                return node

            for puzzle in node.expand_children(
                    cost_method=lambda state: depth(len(node.prev_states)) + simple_heuristic(state)):
                queue.appendleft(puzzle)
            # Sort queue by cost
            queue = collections.deque(sorted(queue, key=lambda state: state.cost, reverse=True))

    def solve_complex_heuristic(self):
        '''
        Use Branch and Bound and return solution PuzzleState
        Cost is the number of steps needed to get to the state plus sum of moves needed for each tile
        '''
        queue = collections.deque([self.initial])

        while queue:
            node = queue.pop()
            if node.goal:
                return node

            for puzzle in node.expand_children(
                    cost_method=lambda state: depth(len(node.prev_states)) + complex_heuristic(state)):
                queue.appendleft(puzzle)
            # Sort queue by cost
            queue = collections.deque(sorted(queue, key=lambda state: state.cost, reverse=True))

    def solve_dynamic_programming(self):
        '''
        Use A* Search or Branch and Bound with Dynamic Programming and return solution PuzzleState
        Cost is the number of steps needed to get to the state and sum of moves needed for each tile
        '''
        self.initial.cost = 0
        queue = collections.deque([self.initial])
        visited = set()

        while queue:
            node = queue.pop()
            visited.add(node)
            if node.goal:
                return node

            for puzzle in node.expand_children(
                    cost_method=lambda state: depth(len(node.prev_states)) + complex_heuristic(state)):
                if puzzle in visited:
                    for element in iter(visited):
                        if element == puzzle:
                            visited_puzzle = element
                            break
                    if puzzle.cost < visited_puzzle.cost:
                        visited.remove(visited_puzzle)
                        queue.appendleft(puzzle)
                else:
                    queue.appendleft(puzzle)
            # Sort queue by cost
            queue = collections.deque(sorted(queue, key=lambda state: state.cost, reverse=True))


def depth(distance):
    '''
    distance from start node
    '''
    return 1 + (distance if distance is not None else 0)


def simple_heuristic(state):
    '''
    heuristic: number of tiles out of places
    '''
    h = 0
    for i, v in enumerate(state):
        if v is None:
            continue
        if i != (v - 1):
            h += 1
    return h


def complex_heuristic(state):
    '''
    heuristic: sum of moves needed for each tile
    calculate Manhattan distance
    '''
    # dictionary of { value : index }
    goal = {1: 0, 2: 1,
            3: 2, None: 3}
    width = 2
    h = 0
    for i, v in enumerate(state):
        if v is None:
            continue
        if i != goal.get(v, i):
            # |x1 - x2| + |y1 - y2|
            h += abs((i % width) - (goal.get(v) % width)) + abs((i // width) - (goal.get(v) // width))
    return h


def pretty_print_children(puzzle):
    '''
    pretty print puzzle with all of its children
    '''
    if puzzle is not None:
        for state in puzzle.prev_states:
            print(state, end='\n\n')
    print(puzzle)


if __name__ == "__main__":
    # Use initial state found in book 
    puzzle = PuzzleState([3, 1, 2, None])

    print("\n====================================")
    print("Initial Puzzle State")
    print(puzzle)

    print("====================================")
    raw = input("Press Enter to continue to BFS Solution...")
    print("BFS Solution")
    solution = PuzzleSolver(puzzle).solve_BFS()
    pretty_print_children(solution)

    print("====================================")
    raw = input("Press Enter to continue to Uniform Cost Solution...")
    print("Branch and Bound with Cost")
    solution = PuzzleSolver(puzzle).solve_uniform_cost_search()
    pretty_print_children(solution)

    print("====================================")
    raw = input("Press Enter to continue to Branch and Bound with simple heuristics...")
    print("Branch and Bound with Simple Heuristic")
    solution = PuzzleSolver(puzzle).solve_simple_heuristic()
    pretty_print_children(solution)

    print("====================================")
    raw = input("Press Enter to continue to Branch and Bound with complex heuristics...")
    print("Branch and Bound with Complex Heuristic")
    solution = PuzzleSolver(puzzle).solve_complex_heuristic()
    pretty_print_children(solution)

    print("====================================")
    raw = input("Press Enter to continue to Branch and Bound with dynamic programming...")
    print("Branch and Bound with Dynamic Programming")
    solution = PuzzleSolver(puzzle).solve_dynamic_programming()
    pretty_print_children(solution)

    print("====================================", end='\n\n')
