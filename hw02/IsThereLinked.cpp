/*************************************************************************
 *
 * hw02: Implement IsThere() for Array and Linked implementations
 * 
 * File Name:   IsThereLinked.cpp
 * Name:        Jonathan De Leon
 * Course:      CPTR 242
 * Date:        April 16, 2018
 * Time Taken:  30 min
 * 
 */
#include "ItemType.h"
#include "UnsortedLinked.h"

// Boolean IsThere(ItemType item)
//  Function:	    Determines if item is in the list.
//  Precondition:	List has been initialized.
// 	Postcondition:	Function value = there exist an item in the list whose key is the same as item's.	
bool UnsortedLinked::IsThere(ItemType item) const {
    NodeType* location = listData;
    while (location != NULL) {
        if (item.ComparedTo(location->info) == EQUAL) {
            return true;
        }
        location = location->next;
    }

    return false;
}
