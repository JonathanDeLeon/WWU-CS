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


class EntityState:
    WEST, EAST = 0, 1

    def __init__(self, name=None, state=None):
        self.name = name
        self.state = state or self.WEST

    def isEast(self):
        return self.state == self.EAST

    def isWest(self):
        return self.state == self.WEST

    def __str__(self):
        return (str(self.name), "WEST" if self.state == self.WEST else "EAST")
