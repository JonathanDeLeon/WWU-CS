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

Implementing the data structure gave me more knowledge on how the data structure works. It is one thing to read about the implementation
and another to actually implement it yourself. I always prefer the latter as it gives me a deeper understanding on 
how it can be implemented and used in different scenarios.

I chose to use a priority queue since the beginning. Fortunately, I had previously learned about this data structure in many of my other
classes before learning about it in this class. I knew its power and also its limitations. One of the limitations is the instability of Heaps.
Heaps use the Heapsort algorithm to re-organize the tree. It puts the maximum element at the top of the Heap and has no regard about the insertion
order of the element. To mitigate this, I gave every new applicant an ID that is used to make sure the priority queue looks at priority and then looks at
insertion order. This would also work in a multi-threaded environment if there are multiple applicants being created at the same time.

In regards to testing, this was something new that I have not done in the past. To be honest, I didn't like it at first because I wanted to get straight to 
the implementation. However, I do see the benefits of writing tests first. It will take some time to get used to as I start to write test-driven applications. 
Nonetheless, it was a fun experience.