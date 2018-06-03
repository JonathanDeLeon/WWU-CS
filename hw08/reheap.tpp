/****************************************************************************
 *
 * Howemork.08: Implement ReheapDown and ReheapUp functions using iteratation.
 *
 * File Name:  reheap.tpp
 * Name:       Jonathan De Leon
 * Course:     CPTR 242
 * Date:       6/3/18
 * Time Taken: 1 hour
 *
 */
#include "Heap.h"

using namespace std;

template<class ItemType>
void HeapType<ItemType>::ReheapDownIterative(int root, int bottom) {
// TODO Implement a Iterative ReheapDown Function.
    int maxChild;
    int leftChild = root * 2 + 1;
    int rightChild = root * 2 + 2;

    while (leftChild <= bottom) {
        if (leftChild == bottom) {
            maxChild = leftChild;
        } else {
            if (elements[leftChild] <= elements[rightChild])
                maxChild = rightChild;
            else
                maxChild = leftChild;
        }
        if (elements[root] < elements[maxChild]) {
            Swap(elements[root], elements[maxChild]);
            root = maxChild;
            leftChild = root * 2 + 1;
            rightChild = root * 2 + 2;
        } else {
            break;
        }
    }
}

template<class ItemType>
void HeapType<ItemType>::ReheapUpIterative(int root, int bottom) {
// TODO Implement a Iterative ReheapUp Function.
    int parent;

    while (bottom > root) {
        parent = (bottom - 1) / 2;
        if (elements[parent] < elements[bottom]) {
            Swap(elements[parent], elements[bottom]);
            bottom = parent;
        } else {
            break;
        }
    }
}
