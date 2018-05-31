# Project 2 Report

The task of this project is to design and implement village housing waiting list.

We must collect enough information about each application to support the priority waitlist.
Priority is granted to applicants who are married and/or have children and/or are 25 years of age or older.
Housing offers are made to other applicants on a first-come, first-served basis. 

## Implementation (Phase 4)

The program was implemented using the Priority Queue and Heap data structures provided by the book.
Those are viewed in the `PQType.h` and `Heap.h` files. I added a copy constructor to the `PQType` class
to be used by the helper functions in the tests and main methods.

In addition, two classes `Application` and `Applicant` were implemented with the idea that applicants
can have multiple applications for different housing spaces (i.e. one for Birch apartments, another for Mtn View, etc).
Although the functionality for different housing was not required and thus not implemented, it can be done with 
the current data models.

The two classes override some binary operators to be used by the priority queue data structures.
Priority goes as follows:
1. 25 or older || Married status || Has Children
2. Insertion order denoted by the application id (FIFO operation)

### Compile And Run

A command line menu is provided to either:
* Create a new application
* Grant housing to the next applicant in the queue
* Print the waiting list
* Exit the program

```bash
g++ -std=c++11 main.cpp PQType.cpp Heap.cpp Application.cpp Applicant.cpp && ./a.out
```

## Write Up (Phase 5)
Coming soon...