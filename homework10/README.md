# Quadratic Formula

This program was built in Python for *CPTR 454 Design & Analysis of Algorithms* at Walla Walla University. It was the programming portion of Homework 10.
The assignment was to write a program to solve the quadratic equation ax<sup>2</sup> + bx + c = 0

## Algorithm
Algorithm first adds a key to a node and then checks if that node is full. Full node is when the # of `keys == 3`.
If the node is full, algorithm performs a middle split: moves middle key to parent node if exists, if not create one,
and moves last key into a new node while reassigning children nodes. Finally reassign root node of tree, if necessary.