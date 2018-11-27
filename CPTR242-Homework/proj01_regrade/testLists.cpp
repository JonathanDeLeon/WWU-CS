#include "testLists.h"
#include <iostream>

/**
 *  Elements in a sorted list must be sorted
 */
bool testForSorted(ListType list) {
    if (list.GetLength() > 1) {
        list.ResetList();
        ItemType itemPrev = list.GetNextItem();
        ItemType itemCurrent = list.GetNextItem();
        for (int i = 1; i < list.GetLength() - 1; ++i) {
            if (itemPrev.ComparedTo(itemCurrent) == GREATER) {
                return false;
            }
            itemPrev = itemCurrent;
            itemCurrent = list.GetNextItem();
        }
    }
    return true;
}

/**
 *  All elements in original should be in modified
 *  Original is always the smaller list
 */
bool testForContains(ListType modified, ListType original) {
    if (original.GetLength() > 0) {
        original.ResetList();
        ItemType item = original.GetNextItem();
        bool found = false;
        for (int i = 0; i < original.GetLength() - 1; ++i) {
            modified.GetItem(item, found);
            if (!found) {
                return false;
            }
            item = original.GetNextItem();
        }
    }
    return true;
}

/**
 *  Check if key is in a list
 */
bool isKeyInList(ListType list, const ItemType &item) {
    if (list.GetLength() > 0) {
        ItemType tempItem = item;
        bool found = false;
        list.GetItem(tempItem, found);
        if (found) {
            return true;
        }
    }
    return false;
}

/**
 *  Check if items in the list are the `RelationType` (LESS, GREATER, or EQUAL) than the passed in key
 */
bool listItemIsRelationTypeThanKey(ListType list, const ItemType &item, const RelationType &comp) {
    if (list.GetLength() > 0) {
        list.ResetList();
        ItemType tempItem = list.GetNextItem();
        for (int i = 0; i < list.GetLength() - 1; ++i) {
            if (tempItem.ComparedTo(item) == comp) {
                return true;
            }
            tempItem = list.GetNextItem();
        }
    }
    return false;
}

bool testMergeLists(ListType list1, ListType list2) {
    ListType result;
    mergeLists(list1, list2, result);
    if ((list1.GetLength() + list2.GetLength()) != result.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    } else if (!testForSorted(result)) {
        cerr << "Output is not sorted" << endl;
        return false;
    } else if (!testForContains(result, list1) || !testForContains(result, list2)) {
        cerr << "Output does not have same elements as input" << endl;
        return false;
    }
    return true;
}


bool testSplitLists(ListType list, ItemType item) {
    ListType list1;
    ListType list2;
    splitLists(list, item, list1, list2);
    if ((list1.GetLength() + list2.GetLength()) != list.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    } else if (!testForSorted(list1) || !testForSorted(list2)) {
        cerr << "Output is not sorted" << endl;
        return false;
    } else if (!testForContains(list, list1) || !testForContains(list, list2)) {
        cerr << "Output does not have same elements as input" << endl;
        return false;
    } else if (listItemIsRelationTypeThanKey(list1, item, GREATER)) {
        cerr << "There are items in the first list greater than the key" << endl;
        return false;
    } else if (listItemIsRelationTypeThanKey(list2, item, LESS)) {
        cerr << "There are items in the second list less than the key" << endl;
        return false;
    } else if (isKeyInList(list2, item)) {
        cerr << "Key is in the second list" << endl;
        return false;
    }
    return true;
}

