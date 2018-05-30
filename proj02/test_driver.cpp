//
// Created by Jonathan De Leon on 5/23/18.
//

#include <iostream>
#include <cassert>
#include "PQType.h"
#include "Application.h"

/*
 * Return true if queue has its priorities maintained in the data structure
 */
bool TestPriorityMaintained(PQType<Application> queue) {
    if (queue.IsEmpty()) {
        // Empty queue maintains order
        return true;
    }

    Application prev;
    Application current;

    queue.Dequeue(prev);
    if (queue.IsEmpty()) {
        // Queue's length == 1
        return true;
    }

    // If previous root was LESS than current root, priority was not maintained
    // Recall: Priority queue dequeues highest priority in FIFO fashion
    while (!queue.IsEmpty()) {
        queue.Dequeue(current);
        if (prev < current) {
            return false;
        }
        prev = current;
    }
    return true;
}

/*
 * Test inserting application to an empty queue
 */
void TestEnqueueOnEmpty() {
    PQType<Application> queue(2);
    Application application;
    Applicant applicant;

    // Check if initial queue is empty
    assert(queue.IsEmpty());

    // Insert one application into queue
    application.Initialize(applicant);
    queue.Enqueue(application);
    assert(!queue.IsEmpty());
}

/*
 * Test general insertion to a priority queue
 */
void TestEnqueueGeneral() {
    PQType<Application> queue(4);
    Application application;
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(1, "Test 1", 18, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 2 Priority", 25, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 3", 17, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 4 Priority", 17, true, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 5 Priority", 18, false, 3);
    application.Initialize(applicant);
    queue.Enqueue(application);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
}

/*
 * Test special case when removing last application
 */
void TestDequeueOneApplicant() {
    PQType<Application> queue(2);
    Application application;
    Application applicantOutput;
    Applicant applicant;

    // Add application to the queue
    application.Initialize(applicant);
    queue.Enqueue(application);

    // Dequeue application
    queue.Dequeue(applicantOutput);

    // Test empty Queue
    assert(queue.IsEmpty());

    // First application out should be the one inserted in an empty queue
    assert(application == applicantOutput);
}

/*
 * Test general dequeue operation so that priority order is maintained
 */
void TestDequeueGeneral() {
    PQType<Application> queue(5);
    Application application;
    Application david;
    Application jeff;
    Applicant applicant;

    // Add applicants to the queue
    // Note: jeff and david applicants 'variables'
    applicant.Initialize(2, "jeff", 18, false, 0);
    jeff.Initialize(applicant);
    queue.Enqueue(jeff);

    applicant.Initialize(2, "test 1", 17, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(2, "test 2", 14, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(2, "david", 25, false, 0);
    david.Initialize(applicant);
    queue.Enqueue(david);

    applicant.Initialize(2, "test 3", 15, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    // Dequeue two applicants: should be david & jeff
    queue.Dequeue(application);
    assert(application == david);
    queue.Dequeue(application);
    assert(application == jeff);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
}

/*
 * Test combinations of insert and deletes and that priority is maintained
 */
void TestGeneralInsertAndDelete() {
    PQType<Application> queue(5);
    Application application;
    Applicant applicant;

    // Add/Remove applicants to/from the queue
    applicant.Initialize(3, "test 1", 15, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(3, "test 2 priority", 15, false, 1);
    application.Initialize(applicant);
    queue.Enqueue(application);

    queue.Dequeue(application);

    assert(TestPriorityMaintained(queue));

    applicant.Initialize(3, "test 3 priority", 15, false, 1);
    application.Initialize(applicant);
    queue.Enqueue(application);

    assert(TestPriorityMaintained(queue));

    applicant.Initialize(3, "test 4 priority", 26, true, 1);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(3, "test 5", 24, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    queue.Dequeue(application);
    queue.Dequeue(application);
    queue.Dequeue(application);

    assert(TestPriorityMaintained(queue));
}
/*
 * Test queue's MakeEmpty and check that Dequeue of empty queue throws EmptyPQ exception
 */
void TestMakeEmpty() {
    PQType<Application> queue(3);
    Application application;
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(4, "test 1 priority", 26, true, 1);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(4, "test 2", 18, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    applicant.Initialize(4, "test 3", 19, false, 0);
    application.Initialize(applicant);
    queue.Enqueue(application);

    // Test empty Queue
    assert(!queue.IsEmpty());
    queue.MakeEmpty();
    assert(queue.IsEmpty());

    // Check empty queue throws exception
    try {
        queue.Dequeue(application);
        assert(false);
    } catch (EmptyPQ ex) {
        assert(true);
    }
}