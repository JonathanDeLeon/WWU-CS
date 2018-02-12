#!/usr/bin/env python
# Jonathan De Leon
# CPTR 454 Design & Analysis of Algorithms
# HW 5: 6.3.10
# February 11, 2018
#
# Assignment:
# Write a program for constructing a 2-3 tree for a given list of n integers.

# Class to represent a node 
class Node:
    def __init__(self, val):
        self.keys = []
        self.children = []


# Class to represent a 2-3 Tree
class TwoThreeTree:
    def __init__(self):
        self.root = None

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

        
# 1) Create TwoThree tree
def main():
    tree = TwoThreeTree()

main()
