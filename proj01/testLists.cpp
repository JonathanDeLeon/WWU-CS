<<<<<<< Updated upstream
#include "testLists.h"
#include "ItemType.h"
#include "SortedType.h"

=======
#include <iostream>
#include "testLists.h"
>>>>>>> Stashed changes

bool testMergeLists(SortedType list1, SortedType list2) {
    SortedType result;
    mergeLists(list1, list2, result);
    if (result.GetLength() == 0) {
        cerr << "Output does not have any elements" << endl;
        return false;
    } else if ((list1.GetLength() + list2.GetLength()) != result.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    }
    return true;
}


bool testSplitLists(SortedType list, ItemType item) {
    // TODO Write test for SplitLists
    SortedType list1;
    SortedType list2;
    splitLists(list, item, list1, list2);
    if ((list1.GetLength() + list2.GetLength()) != list.GetLength()) {
        cerr << "Output does not have expected length" << endl;
        return false;
    }
    return true;
}
