/****************************************************************************
 *
 * Howemork.06: Implement a two recursive functions on a linked unsorted list.
 *
 * File Name:  minSort.cpp
 * Name:       ?
 * Course:     CPTR 242
 * Date:       ?
 * Time Taken: ?
 *
 */
#include "ItemType.h"
#include "unsorted.h"

NodeType* MinLoc(NodeType* list, NodeType*& minPtr) {
    // TODO Add recursive MinLoc.
    if (list == NULL) {
        return minPtr;
    } else if (list->info.ComparedTo(minPtr->info) == LESS) {
        minPtr = list;
    }
    return MinLoc(list->next, minPtr);
}

void Sort(NodeType* list) {
    // TODO Add recursive sort method that uses MinLoc.
    if (list != NULL) {
        NodeType* temp = new NodeType;
        NodeType* min = list;
        MinLoc(list, min);
        temp->info = list->info;
        list->info = min->info;
        min->info = temp->info;
        Sort(list->next);
    }
}
