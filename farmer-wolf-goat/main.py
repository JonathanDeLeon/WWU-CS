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
    WEST, EAST = 0, 1

    def __init__(self, state=None):
        self.state = state or self.WEST

    def is_east(self):
        return self.state == self.EAST

    def is_west(self):
        return self.state == self.WEST

    def __eq__(self, other):
        return self.state == other.state

    def __str__(self):
        return "WEST" if self.state == self.WEST else "EAST"


class PuzzleState:
    def __init__(self, state=None):
        self.state = state

    def empty(self):
        if not self.state:
            return True
        return False

    def valid_state(self):
        '''
        :return True if farmer is with goat or if goat is not alone with wolf or cabbage
        '''
        if self.empty():
            return False

        try:
            if self.state['farmer'] == self.state['goat']:
                return True
            elif self.state['goat'] == self.state['wolf']:
                return False
            elif self.state['goat'] == self.state['cabbage']:
                return False
        except:
            return False
        else:
            return True

    def goal(self):
        '''
        :return True if all entities in state are east of the river
        '''
        if self.empty():
            return False
        try:
            for entity in self.state.values():
                if entity.is_west:
                    return False
        except:
            return False
        else:
            return True

    def __eq__(self, other):
        return cmp(self.state, other.state)

    def __str__(self):
        return str(self.state)


class Puzzle:
    initial = {'farmer': Entity(), 'wolf': Entity(), 'goat': Entity(), 'cabbage': Entity()}
    path = []

    def __init__(self):
        self.path.append(PuzzleState(self.initial))

    def find_solution(self):
        next_state = self.path[-1].copy()

        return next_state


puzzle = PuzzleState()
