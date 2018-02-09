#!/bin/bash
# Jonathan De Leon
# CPTR 454 Design & Analysis of Algorithms
# HW 4: 5.3.10
# February 4, 2018
#
# Assignment:
#  Write a program for computing the internal path length of an extended binary tree

import random

# Class to represent a node in a Binary Tree
class Node:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


# Class to represent a Binary Tree
class BinaryTree:
    def __init__(self):
        self.root = None

    def add(self, val):
        if self.root is None:
            self.root = Node(val)
        else:
            self._add(val, self.root)

    # Create node with value and add it to tree
    # left <= root < right ; duplicates allowed
    def _add(self, val, node):
        if val <= node.val:
            if node.left is not None:
                self._add(val, node.left)
            else:
                node.left = Node(val)
        else:
            if node.right is not None:
                self._add(val, node.right)
            else:
                node.right = Node(val)

    def internalPathLength(self):
        if self.root is None:
            return 0
        else:
            return self._internalPathLength(self.root)

    # Recursively sum the path length of each node
    # Internal nodes are nodes with at least one child
    def _internalPathLength(self, node, length=0):
        if node is None:
            return 0
        else:
            return (length+self._internalPathLength(node.left, length+1)+self._internalPathLength(node.right, length+1))

    # Below is printing pretty binary trees from 
    # http://krenzel.org/articles/printing-trees
    # Recursively print binary tree horizontally level by level
    def printTree(self, node, depth=0):
        ret = ""

        if node is None:
            return

        # Print right branch
        if node.right is not None:
            ret += self.printTree(node.right, depth + 1)
        else:
			ret += "\n" + ("    "*(depth+1)) + "NULL"

        # Print own value
        ret += "\n" + ("    "*depth) + str(node.val)

        # Print left branch
        if node.left is not None:
            ret += self.printTree(node.left, depth + 1)
        else:
			ret += "\n" + ("    "*(depth+1)) + "NULL"

        return ret

    def __str__(self, node=None, depth=0):
        if self.root is None:
            return "Tree is empty"
        else:
            return self.printTree(self.root)

        
# 1) Create binary tree
# 2) Add 6 random values to the tree and print tree
# 3) Calculate tree internal path length
def main():
    tree = BinaryTree()
    tree.add(10)
    for rand in range(5):
        tree.add(random.randint(1,20))

    print(tree)
    print"\nInternal Path Length: ",tree.internalPathLength()

main()
