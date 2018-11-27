#include "testLists.h"
#include <iostream>


/**
 *  Elements in a sorted list must be sorted
 */
bool testForSorted(SortedType list) {
    if (list.GetLength() > 1){
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
bool testForContains(SortedType modified, SortedType original) {
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
 *  Key should be first item in second split list
 */
bool isKeyInList(SortedType list, ItemType item) {
    if (list.GetLength() > 0) {
        list.ResetList();
        ItemType tempItem = list.GetNextItem();
        if (tempItem.ComparedTo(item) == EQUAL) {
            return true;
        }
    }
    return false;
}

bool testMergeLists(SortedType list1, SortedType list2) {
    SortedType result;
    mergeLists(list1, list2, result);
    if ((list1.GetLength() + list2.GetLength()) != result.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    } else if (!testForSorted(result)){
        cerr << "Output is not sorted" << endl;
        return false;
    } else if (!testForContains(result, list1) || !testForContains(result, list2)) {
        cerr << "Output does not have same elements as input" << endl;
        return false;
    }
    return true;
}


bool testSplitLists(SortedType list, ItemType item) {
    SortedType list1;
    SortedType list2;
    splitLists(list, item, list1, list2);
    if ((list1.GetLength() + list2.GetLength()) != list.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    } else if (!testForSorted(list1) || !testForSorted(list2)){
        cerr << "Output is not sorted" << endl;
        return false;
    } else if (!testForContains(list, list1) || !testForContains(list, list2)) {
        cerr << "Output does not have same elements as input" << endl;
        return false;
    } else if (!isKeyInList(list2, item)) {
        cerr << "Key is not in the second list" << endl;
        return false;
    }
    return true;
}
