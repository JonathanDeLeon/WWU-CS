//
// Created by Jonathan De Leon on 5/23/18.
//

#include <iostream>
#include <assert.h>
#include "PQType.h"
#include "Applicant.h"

/*
 * Return true if queue has its priorities maintained in the data structure
 */
bool TestPriorityMaintained(PQType<Applicant> queue) {
    if (queue.IsEmpty()) {
        // Empty queue maintains order
        return true;
    }

    Applicant prev;
    Applicant current;

    queue.Dequeue(prev);
    if (queue.IsEmpty()) {
        // Queue's length == 1
        return true;
    }

    // If previous root was LESS than current root, priority was not maintained
    // Recall: Priority queue dequeues highest priority in FIFO fashion
    while (!queue.IsEmpty()) {
        queue.Dequeue(current);
        if (prev.ComparedTo(current) == LESS) {
            return false;
        }
        prev = current;
    }
    return true;
}

/*
 * Test inserting applicant to an empty queue
 */
void TestEnqueueOnEmpty() {
    PQType<Applicant> queue(2);
    Applicant applicant;

    // Check if initial queue is empty
    assert(queue.IsEmpty());

    // Insert one applicant into queue
    applicant.Initialize(10);
    queue.Enqueue(applicant);
    assert(!queue.IsEmpty());
}

/*
 * Test general insertion to a priority queue
 */
void TestEnqueueGeneral() {
    PQType<Applicant> queue(3);
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(10);
    queue.Enqueue(applicant);
    applicant.Initialize(5);
    queue.Enqueue(applicant);
    applicant.Initialize(8);
    queue.Enqueue(applicant);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
}

/*
 * Test special case when removing last applicant
 */
void TestDequeueOneApplicant() {
    PQType<Applicant> queue(2);
    Applicant applicant;
    Applicant applicantOutput;

    // Add applicant to the queue
    applicant.Initialize(10);
    queue.Enqueue(applicant);

    // Dequeue applicant
    queue.Dequeue(applicantOutput);

    // Test empty Queue
    assert(queue.IsEmpty());

    // First applicant out should be the one inserted in an empty queue
    assert(applicant.ComparedTo(applicantOutput) == EQUAL);
}

/*
 * Test general dequeue operation so that priority order is maintained
 */
void TestDequeueGeneral() {
    PQType<Applicant> queue(5);
    Applicant applicant;
    Applicant david;
    Applicant jeff;

    // Add applicants to the queue
    // Note: jeff and david applicants 'variables'
    jeff.Initialize(10);
    queue.Enqueue(jeff);
    applicant.Initialize(5);
    queue.Enqueue(applicant);
    applicant.Initialize(8);
    queue.Enqueue(applicant);
    david.Initialize(15);
    queue.Enqueue(david);
    applicant.Initialize(3);
    queue.Enqueue(applicant);

    // Dequeue two applicants: should be david & jeff
    queue.Dequeue(applicant);
    assert(applicant.ComparedTo(david) == EQUAL);
    queue.Dequeue(applicant);
    assert(applicant.ComparedTo(jeff) == EQUAL);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
}

/*
 * Test combinations of insert and deletes and that priority is maintained
 */
void TestGeneralInsertAndDelete() {
    PQType<Applicant> queue(5);
    Applicant applicant;

    // Add/Remove applicants to/from the queue
    applicant.Initialize(5);
    queue.Enqueue(applicant);
    applicant.Initialize(8);
    queue.Enqueue(applicant);

    queue.Dequeue(applicant);
    assert(TestPriorityMaintained(queue));

    applicant.Initialize(8);
    queue.Enqueue(applicant);
    assert(TestPriorityMaintained(queue));

    applicant.Initialize(12);
    queue.Enqueue(applicant);
    applicant.Initialize(1);
    queue.Enqueue(applicant);
    queue.Dequeue(applicant);
    queue.Dequeue(applicant);
    queue.Dequeue(applicant);
    assert(TestPriorityMaintained(queue));
}
/*
 * Test queue's MakeEmpty and check that Dequeue of empty queue throws EmptyPQ exception
 */
void TestMakeEmpty() {
    PQType<Applicant> queue(3);
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(10);
    queue.Enqueue(applicant);
    applicant.Initialize(5);
    queue.Enqueue(applicant);
    applicant.Initialize(8);
    queue.Enqueue(applicant);

    // Test empty Queue
    assert(!queue.IsEmpty());
    queue.MakeEmpty();
    assert(queue.IsEmpty());

    // Check empty queue throws exception
    try {
        queue.Dequeue(applicant);
        assert(false);
    } catch (EmptyPQ ex) {
        assert(true);
    }
}

