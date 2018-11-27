# Project 2 Report

The task of this project is to design and implement village housing waiting list.

We must collect enough information about each application to support the priority waitlist.
Priority is granted to applicants who are married and/or have children and/or are 25 years of age or older.
Housing offers are made to other applicants on a first-come, first-served basis. 

## Data Structure Evaluation (Phase 1)

This phase is to evaluate possible data structures for the project.

| Data Structure | Strengths | Weaknesses |
| -------------- | --------- | ---------- |
| Priority Queue | The project by definition is a priority queue. Queue is FIFO or first-come, first-served basis with priority given to specific applicants. | If implemented as a linked-list, there is `O(N)` efficiency for deallocating the data structure. 
| Doubly Linked Lists | Allows us to traverse the list forward and backwards. Helps us delete nodes faster with `O(1)` compared to single linked lists. | More memory for storing the `prev` and `next` pointers. Most likely unnecessary due to the scope of the project.
| Circular Linked List | Useful for implementation of queue. We would only have to maintain one pointer. Gives us access to both ends of the list. | Deallocating is `O(N)`. |
| Stacks | Stacks have good efficiency algorithms of `O(1)` all around except for the destructor | For this project, we would have to use two stacks so that we can implement the FIFO operations required. Too much overhead for space. |