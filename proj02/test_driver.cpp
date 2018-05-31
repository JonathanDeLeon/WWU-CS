//
// Created by Jonathan De Leon on 5/23/18.
//

#include <iostream>
#include <cassert>
#include "PQType.h"
#include "Application.h"

/*
 * Helper function to reset applicant priority data to default values
 */
void ResetApplicantPriorities(Applicant &applicant) {
    applicant.SetMarried(false);
    applicant.SetChildren(0);
    applicant.SetAge(21);
}

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
    PQType<Application> queue(1);
    Application application;
    Applicant applicant;

    // Check if initial queue is empty
    assert(queue.IsEmpty());

    // Insert one application into queue
    application.Initialize(1, applicant);
    queue.Enqueue(application);
    assert(!queue.IsEmpty());
    std::cout << "TestEnqueueOnEmpty successfully ran." << std::endl;
}

/*
 * Test general insertion to a priority queue
 */
void TestEnqueueGeneral() {
    PQType<Application> queue(5);
    Application application;
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(1, "Test 1", "", 18);
    application.Initialize(1, applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 2 Priority", "", 25);
    application.Initialize(2, applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 3", "", 17);
    application.Initialize(3, applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 4 Priority", "", 17);
    applicant.SetMarried(true);
    application.Initialize(4, applicant);
    queue.Enqueue(application);

    applicant.Initialize(1, "Test 5 Priority", "", 18);
    applicant.SetChildren(3);
    application.Initialize(5, applicant);
    queue.Enqueue(application);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
    std::cout << "TestEnqueueGeneral successfully ran." << std::endl;
}

/*
 * Test special case when removing last application
 */
void TestDequeueOneApplicant() {
    PQType<Application> queue(1);
    Application application;
    Application applicantOutput;
    Applicant applicant;

    // Add application to the queue
    application.Initialize(1, applicant);
    queue.Enqueue(application);

    // Dequeue application
    queue.Dequeue(applicantOutput);

    // Test empty Queue
    assert(queue.IsEmpty());

    // First application out should be the one inserted in an empty queue
    assert(application == applicantOutput);
    std::cout << "TestDequeueOneApplicant successfully ran." << std::endl;
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
    applicant.Initialize(2, "jeff", "", 18);
    jeff.Initialize(1, applicant);
    queue.Enqueue(jeff);

    applicant.Initialize(2, "test 2", "", 17);
    application.Initialize(2, applicant);
    queue.Enqueue(application);

    applicant.Initialize(2, "test 3", "", 14);
    application.Initialize(3, applicant);
    queue.Enqueue(application);

    applicant.Initialize(2, "david", "", 25);
    david.Initialize(4, applicant);
    queue.Enqueue(david);

    applicant.Initialize(2, "test 5", "", 15);
    application.Initialize(5, applicant);
    queue.Enqueue(application);

    // Dequeue two applicants: should be david & jeff
    queue.Dequeue(application);
    assert(application == david);
    queue.Dequeue(application);
    assert(application == jeff);

    // Check if priority is maintained in the data structure
    assert(TestPriorityMaintained(queue));
    std::cout << "TestDequeueGeneral successfully ran." << std::endl;
}

/*
 * Test combinations of insert and deletes and that priority is maintained
 */
void TestGeneralInsertAndDelete() {
    PQType<Application> queue(5);
    Application application;
    Applicant applicant;

    // Add/Remove applicants to/from the queue
    applicant.Initialize(3, "test 1", "", 15);
    application.Initialize(1, applicant);
    queue.Enqueue(application);

    applicant.Initialize(3, "test 2 priority", "", 15);
    applicant.SetChildren(1);
    application.Initialize(2, applicant);
    queue.Enqueue(application);

    queue.Dequeue(application);

    assert(TestPriorityMaintained(queue));

    applicant.Initialize(3, "test 3 priority", "", 15);
    applicant.SetChildren(1);
    application.Initialize(3, applicant);
    queue.Enqueue(application);

    assert(TestPriorityMaintained(queue));

    applicant.Initialize(3, "test 4 priority", "", 26);
    applicant.SetMarried(true);
    application.Initialize(4, applicant);
    queue.Enqueue(application);

    applicant.Initialize(3, "test 5", "", 24);
    application.Initialize(5, applicant);
    queue.Enqueue(application);

    queue.Dequeue(application);
    queue.Dequeue(application);
    queue.Dequeue(application);

    assert(TestPriorityMaintained(queue));
    std::cout << "TestGeneralInsertAndDelete successfully ran." << std::endl;
}
/*
 * Test queue's MakeEmpty and check that Dequeue of empty queue throws EmptyPQ exception
 */
void TestMakeEmpty() {
    PQType<Application> queue(3);
    Application application;
    Applicant applicant;

    // Add applicants to the queue
    applicant.Initialize(4, "test 1 priority", "", 26);
    applicant.SetMarried(true);
    applicant.SetChildren(1);
    application.Initialize(1, applicant);
    queue.Enqueue(application);

    applicant.Initialize(4, "test 2", "", 18);
    application.Initialize(2, applicant);
    queue.Enqueue(application);

    applicant.Initialize(4, "test 3", "", 19);
    application.Initialize(3, applicant);
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
        std::cout << "TestMakeEmpty successfully ran." << std::endl;
    }
}
int main() {
    TestEnqueueOnEmpty();
    TestEnqueueGeneral();
    TestDequeueOneApplicant();
    TestDequeueGeneral();
    TestGeneralInsertAndDelete();
    TestMakeEmpty();
    std::cout << "\nAll tests successfully ran." << std::endl;
    return 0;
}
