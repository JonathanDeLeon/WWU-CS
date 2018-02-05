# Binary Tree

This program was built in Python for *CPTR 454 Design & Analysis of Algorithms* at Walla Walla University. It was the programming portion of Homework 4.
The assignment was to write a program for computing the internal path length of an extended binary tree.

Some things to note:
* Binary Tree is printed horizontally level by level
* If the value2 is on the next level and above value1, then value2 > value1
* If the value2 is on the next level and below value1, then value2 < value1
>Note: See sample output below

To run the program in terminal:
```
$ python internal_path_length.py
```

Example output:
```
    17
        16
10
        4
            4
    3

Internal Path Length:  9
```
Above tree is the same as:
```
        10
      /    \
    3        17
     \      /
      4    16
       \
        4
Internal Path Length:  9
```
