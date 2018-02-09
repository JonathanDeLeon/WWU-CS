# Binary Tree

This program was built in Python for *CPTR 454 Design & Analysis of Algorithms* at Walla Walla University. It was the programming portion of Homework 4.
The assignment was to write a program for computing the internal path length of an extended binary tree. 

> 


Some things to note:
* Binary Tree is printed horizontally level by level
* Extended Binary Tree: A binary tree in which special nodes are added wherever a null subtree was present in the original tree.
* A node's path length is the number of links (or branches, edges) required to get back to the root. The root has path length zero and the maximum path length in a tree is called the tree's height. The sum of the path lengths of a tree's internal nodes is called the internal path length. Nodes which are not leaves are called internal nodes.

To run the program in terminal:
```
$ python internal_path_length.py
```

Example output:
```

        NULL
    13
        NULL
10
            NULL
        9
            NULL
    7
            NULL
        5
                NULL
            3
                NULL

Internal Path Length:  9
```
Above tree is the same as:
```
              10
            /    \
          7        13
        /  \      /  \
       5    9   NUL  NUL
     / \    / \
    3  NUL NUL NUL
   / \  
 NUL  NUL 
Internal Path Length:  9
```
