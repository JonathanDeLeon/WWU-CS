/*************************************************************************
 *
 * hw02: Implement IsThere() for Array and Linked implementations
 * 
 * File Name:   IsThereArray.cpp
 * Name:        Jonathan De Leon
 * Course:      CPTR 242
 * Date:        April 16, 2018
 * Time Taken:  30 min
 * 
 */
#include "ItemType.h"
#include "UnsortedArray.h"

// Boolean IsThere(ItemType item)
//  Function:	    Determines if item is in the list.
//  Precondition:	List has been initialized.
// 	Postcondition:	Function value = there exist an item in the list whose key is the same as item's.	
bool UnsortedArray::IsThere(ItemType item) const {
    int location = 0;
    while (location < length) {
        if (item.ComparedTo(info[location]) == EQUAL) {
            return true;
        }
        location++;
    }
    return false;
}