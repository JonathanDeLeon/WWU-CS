# Project 2 Report

The task of this project is to design and implement village housing waiting list.

We must collect enough information about each application to support the priority waitlist.
Priority is granted to applicants who are married and/or have children and/or are 25 years of age or older.
Housing offers are made to other applicants on a first-come, first-served basis. 

## Test Descriptions (Phase 2)
>Before coding, write up a set of tests that can be used to ensure the data structure is implemented correctly.These test will be converted to C++ tests which you will implement as part of the project.The tests should attempt to get 100% code coverage of your data structure.The tests do not need to include the user input and output. Focus on testing your internal classes and data structure.

From the Data Structure analysis and evaluation done in [Phase 1](EVALUATION.md), I have come to the conclusion that a priority queue is best for this project. Although I will not go over the implementation details in thisphase, I will discuss the tests that will be needed for almost 100% code coverage.

---

### Enqueue

This operation adds applicants to the queue and must be tested based on two scenarios.

#### *On Empty*
Test to correctly add an applicant to an empty queue

#### *With one or more applicants*

Test to correctly add an applicant in its correct position based on priority

---

### Dequeue
This operation removes applicants from the queue to be processed and must be tested based on two scenarios.

#### *One applicant*

Test to remove last applicant from the queue

#### *More than one applicants*

Test to remove the highest priority applicant from the queue

---

### MakeEmpty
This operation should initialize queue to an empty state.
Test that the `Dequeue` operation throws an exception because the queue is empty after calling this method.

---
### PriorityMaintained
After a series of `Enqueue` and `Dequeue`, this test should check in the general sense that priorities are maintained meaning that every applicant in the queue is at its correct position based on priority. In a priority queue (heap), the applicant that is dequeued first should be greater or equal to all subsequent dequeued applicants.

## Implementing Tests (Phase 3)
>After you have written your test descriptions, start to implement your tests and create your test driver.
The test driver is simply a main function that executes your tests.
The tests should attempt to get 100% code coverage of your data structure.
The tests should be run by a test_driver.cpp.

### Compile Tests

```bash
g++ -std=c++11 test_driver.cpp PQType.h Heap.h Applicant.cpp && ./a.out
```
