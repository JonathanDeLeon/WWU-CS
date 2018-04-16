#!/usr/bin/env python
# Jonathan De Leon
# CPTR 430 Artificial Intelligence
# Homework 2
# April 15, 2018
#
# Problem:
# A farmer with a wolf, a goat, and a container of cabbage are on the west bank of the river.
# On the river is a boat in which the farmer and one of the other three (wolf, goat, or cabbage)
# can fit. If the wolf is left alone with the goat, the wolf will eat the goat.
# If the goat is left alone with the container of cabbage, the goat will eat the cabbage.
# Your goal is to transfer everyone to the other side of the river safely.

class Entity:
    '''
    Entity class that represents Farmer, Wolf, Goat, and Cabbage
    '''
    WEST, EAST = 0, 1

    def __init__(self, name, location=None):
        self.name = name
        self.location = location or self.WEST

    def new_location(self):
        return self.EAST if self.is_west() else self.WEST

    def move_copy(self):
        return Entity(self.name, self.new_location())

    def is_east(self):
        return self.location == self.EAST

    def is_west(self):
        return self.location == self.WEST

    def __eq__(self, other):
        return self.location == other.location

    def __repr__(self):
        return "WEST" if self.location == self.WEST else "EAST"

    def __str__(self):
        return str(self.name) + ": " + self.__repr__()


class PuzzleState:
    '''
    Class to represent the state of a the 'river crossing' Puzzle
    '''

    def __init__(self, state=None):
        self.state = state

    def empty(self):
        if not self.state:
            return True
        return False

    def valid_state(self, state=None):
        '''
        :return True if farmer is with goat or if goat is not alone with wolf or cabbage
        '''
        if not state:
            if self.empty():
                return False
            state = self.state

        try:
            if state['farmer'] == state['goat']:
                return True
            elif state['goat'] == state['wolf']:
                return False
            elif state['goat'] == state['cabbage']:
                return False
        except:
            return False
        else:
            return True

    def entities_at_pos(self, location):
        '''
        :return list of entities at a specified location
        '''
        entities = []
        for entity in self.state.values():
            if entity.location == location:
                entities.append(entity)
        return entities

    def valid_children_generator(self):
        '''
        :return python generator with valid branched/children states with new entity locations
        '''
        farmer = self.state.get('farmer')
        for entity in self.entities_at_pos(farmer.location):
            state = self.state.copy()
            # Farmer can cross river on his own and with another entity
            if entity.name != 'farmer':
                state['farmer'] = farmer.move_copy()

            state[entity.name] = entity.move_copy()
            if self.valid_state(state):
                yield PuzzleState(state)

    def goal(self):
        '''
        :return True if all entities in the state are east of the river
        '''
        if self.empty():
            return False
        try:
            for entity in self.state.values():
                if entity.is_west():
                    return False
        except:
            return False
        else:
            return True

    def __eq__(self, other):
        return self.state == other.state

    def __repr__(self):
        return str(self.state)

    def __str__(self):
        return self.__repr__()


class Puzzle:
    initial = {'farmer': Entity('farmer'), 'wolf': Entity('wolf'), 'goat': Entity('goat'), 'cabbage': Entity('cabbage')}
    path = []
    visited = []

    def __init__(self):
        self.path.append(PuzzleState(self.initial))

    def find_solution(self):
        '''
        :return find state with a solution using DFS or a stack(LIFO)
        '''
        next_state = self.path.pop()
        while next_state and not next_state.goal():
            self.visited.append(next_state)
            for puzzle in next_state.valid_children_generator():
                if self.unique_state(puzzle):
                    self.path.append(puzzle)
            next_state = self.path.pop() if len(self.path) > 0 else None
        return next_state

    def unique_state(self, puzzle):
        '''
        :return True if PuzzleState has not already been seen; uses __eq__ in PuzzleState
        '''
        if puzzle in self.visited or puzzle in self.path:
            return False
        return True


if __name__ == "__main__":
    puzzle = Puzzle()
    print("\n====================================", )
    print("Initial root node")
    print(puzzle.initial)
    print("====================================")
    print("Solution")
    print(puzzle.find_solution())
    print("====================================")
    print("List of visited nodes before finding solution")
    print(puzzle.visited)
    print("====================================\n")
