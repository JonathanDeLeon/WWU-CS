# 2-3 Tree

This program was built in Python for *CPTR 454 Design & Analysis of Algorithms* at Walla Walla University. It was the programming portion of Homework 5.
The assignment was to write a program for constructing a 2-3 tree for a given list of n integers.

## Algorithm
Algorithm first adds a key to a node and then checks if that node is full, # of keys == 3 Algorithm performs a middle split, moves middle key to parent node if exists, if not create one, 
and moves last key into a new node while reassigning children nodes. Finally reassign root node of tree if necessary.

Some things to note:
* 2-3 Tree is printed level by level
* Program accepts user input. Follow instructions to create tree using default random list of integers or user created list of integers.

To run the program in terminal:
```
$ python two_three_tree.py
```

Example output:
```
('Level 0', '[9] ')
('Level 1', 'Parent [9]:[7] Parent [9]:[13, 16] ')
('Level 2', 'Parent [7]:[2, 4] Parent [7]:[8] Parent [13, 16]:[12] Parent [13, 16]:[14] Parent [13, 16]:[18, 19] ')
```
Above tree is the same as:
```
               |9|
            /       \
         |7|         |13|16| 
        /   \       /   |   \
    |2|4|   |8|  |13|  |14|  |18|19|
```
