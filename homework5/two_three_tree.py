#!/usr/bin/env python
# Jonathan De Leon
# CPTR 454 Design & Analysis of Algorithms
# HW 5: 6.3.10
# February 11, 2018
#
# Assignment:
# Write a program for constructing a 2-3 tree for a given list of n integers.

import random

class TwoThreeTree:
    """ Class to represent a 2-3 Tree """

    class Node:
        """ Class to represent a node """

        def __init__(self):
            self.keys = []
            self.children = []
            self.parent = None

        @property
        def is_full(self):
            return self.size == 3 

        @property
        def size(self):
            return len(self.keys)

        @property
        def num_children(self):
            return len(self.children)

        def add_key(self, value):
            """
            adds value to list; if node is full, then split
            """
            if value not in self.keys:
                self.keys.append(value)
                self.keys.sort()
                if self.is_full:
                    return self.split()
            return self

        def add_child(self, new_node):
            """
            returns: an order list of child nodes
            """
            i = len(self.children) - 1
            while i >= 0 and self.children[i].keys[0] > new_node.keys[0]:
                i -= 1
            return self.children[:i + 1]+ [new_node] + self.children[i + 1:]

        def split(self):
            """
            1) If there is no parent; create a new parent with middle node
            2) If there is a parent; remove last key into a new node, move middle key to parent node
            In both: Reorganize parent/child relationships
            """
            if self.parent is None:
                parent = self.__class__()
            else:
                parent = self.parent

            # Create new child node of last key
            val = self.keys.pop(self.size-1)
            new_node_last = self.__class__()
            new_node_last.add_key(val) 

            # Don't add same child node twice
            if parent.num_children == 0:
                parent.children = parent.add_child(self)
                self.parent = parent
            parent.children = parent.add_child(new_node_last)
            new_node_last.parent = parent

            # Place middle key in parent
            mid = self.keys.pop(1)
            node = parent.add_key(mid)

            # Reorganize child nodes 
            while self.size == 1 and self.num_children > 2:
                child = self.children.pop(self.num_children-1)
                new_node_last.children = new_node_last.add_child(child)
                child.parent = new_node_last

            return node

    def __init__(self):
        self.root = self.Node()

    def insert(self, val):
        """
        Traverse through tree to find the appropriate node
        returns: node where value was inserted
        """
        node = self.root
        while node.num_children > 0:
            if val in node.keys:
                return
            i = node.size - 1
            while i > 0 and val < node.keys[i] :
                i -= 1
            if val > node.keys[i]:
                i += 1
            node = node.children[i]

        node = node.add_key(val)
        # Set new root node if there was a split on old root node
        if node and node.parent is None:
            self.root = node
        return node

    def print_order(self):
        """Print a level-order representation."""
        this_level = [self.root]
        level = 0
        while this_level:
            next_level = []
            output = ""
            for node in this_level:
                if node.children:
                    next_level.extend(node.children)
                if node.parent is not None:
                    output += "Parent "+str(node.parent.keys)+":"
                output += str(node.keys) + " "
            print("Level "+str(level), output)
            this_level = next_level
            level += 1

    def __str__(self):
        self.print_order()
        return ""

        
# Construct a 2-3 Tree
def main():
    tree = TwoThreeTree()
    for rand in range(15):
        tree.insert(random.randint(1,20))
    print tree

main()
