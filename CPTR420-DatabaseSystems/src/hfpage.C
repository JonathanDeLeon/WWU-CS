// **********************************************
//     Heap File Page Class
//     $Id: hfpage.C,v 1.2 1997/01/07 04:57:42 flisakow Exp $
// **********************************************

#include <iostream>
#include <stdlib.h>
#include <memory.h>

#include "hfpage.h"
#include "heapfile.h"
#include "buf.h"
#include "db.h"

#include "bitmap.h"

// **********************************************************
// page class constructor
void HFPage::init(PageId pageNo)
{
    nextPage = prevPage = INVALID_PAGE;

    recSze  = 0;                 // The first record inserted determines
                                 // the record size used for this page.
    maxRecs = 0;

    recCnt  = 0;                 // no records in use
    memset(bitmap, '\0', BITMAP_SIZE);

    curPage   = pageNo;
}

// **********************************************************
// dump page utlity
void HFPage::dumpPage()
{
    int i;

    cout << "dumpPage, this: " << this << endl;
    cout << "curPage= " << curPage << ", nextPage= " << nextPage << endl;

    cout << "recSze= " << recSze << ", recCnt= " << recCnt
         << ", maxRecs= " << maxRecs << endl;

    if (maxRecs) {
        for (i=0; i < maxRecs; i++) {
            if (is_set(bitmap,i))
                cout << "slot[" << i << "].offset= " << (i * recSze)
                     << ", slot["<< i << "].length=" << recSze << endl; 
        }
    } else {
        cout << "Page is empty.\n";
    }
}

// **********************************************************
void HFPage::setNextPage(PageId pageNo)
{
    nextPage = pageNo;
}

void HFPage::setPrevPage(PageId pageNo)
{
    prevPage = pageNo;
}

// **********************************************************
PageId HFPage::getNextPage()
{
    return nextPage;
}

PageId HFPage::getPrevPage()
{
    return prevPage;
}

// **********************************************************
// Add a new record to the page. Returns OK if everything went OK
// otherwise, returns DONE if sufficient space does not exist
// RID of the new record is returned via rid parameter.

Status HFPage::insertRecord(char *recPtr, int recLen, RID& rid)
{
    RID tmpRid;

    if (maxRecs) {
          // check that this record is the same size as the
          // rest on this page.
        if (recLen != recSze)
            return DONE;

          // Do we have any room?
        if (recCnt == maxRecs)
            return DONE;
    } else {
        // This is the first record inserted onto this page, 
        // initialize the size of records we should expect.
        recSze = recLen;
        maxRecs = (MAX_SPACE - DPFIXED) / recLen;

          // Only allow as many records as will fit in the bitmap
        if (maxRecs > MAX_RECS_PER_PAGE)
            maxRecs = MAX_RECS_PER_PAGE;

        if (maxRecs == 0)
            return DONE;
    }

    // Find an empty slot

    int i;
    for (i=0; i < maxRecs; i++) {
        if (is_clr(bitmap,i))
            break;
    }

    // at this point we have found an empty slot.

    set_bit(bitmap,i);
    recCnt++;
    memcpy(&data[i*recSze],recPtr,recSze); // copy data onto the data page

    tmpRid.pageNo = curPage;
    tmpRid.slotNo = i;
    rid = tmpRid;

    return OK;
}

// **********************************************************
// Delete a record from a page. Returns OK if everything went okay.
Status HFPage::deleteRecord(const RID& rid)
{
    int slotNo = rid.slotNo;

    // first check if the record being deleted is actually valid
    if (is_set(bitmap,slotNo)) {
        // valid slot
        clr_bit(bitmap,slotNo);        // mark slot as free.
        recCnt--;
        return OK;
    } else {
        return MINIBASE_FIRST_ERROR( HEAPFILE, INVALID_SLOTNO );
    }
}

// **********************************************************
// returns RID of first record on page
Status HFPage::firstRecord(RID& firstRid)
{
    RID tmpRid;
    int i;

    // find the first non-empty slot

    for (i=0; i < maxRecs; i++) {
        if (is_set(bitmap,i))
            break;
    }

    if (i == maxRecs)
        return DONE;

      // found a non-empty slot
    tmpRid.pageNo = curPage;
    tmpRid.slotNo = i;
    firstRid = tmpRid;

    return OK;
}

// **********************************************************
// returns RID of next record on the page
// returns DONE if no more records exist on the page; otherwise OK
Status HFPage::nextRecord (RID curRid, RID& nextRid)
{
    RID tmpRid;
    int i; 

      // find the next non-empty slot
    for (i=curRid.slotNo+1; i < maxRecs; i++) {
        if (is_set(bitmap,i))
            break;
    }

    if (i == maxRecs)
        return DONE;

      // found a non-empty slot
    tmpRid.pageNo = curPage;
    tmpRid.slotNo = i;
    nextRid = tmpRid;

    return OK;
}

// **********************************************************
// returns length and copies out record with RID rid
Status HFPage::getRecord(RID rid, char *recPtr, int& recLen)
{
    int slotNo = rid.slotNo;

    if (is_set(bitmap,slotNo)) {

         // copy out the record
        recLen = recSze;           // return length of record
        memcpy(recPtr, &(data[slotNo*recSze]), recSze);

        return OK;
    } else {
        return MINIBASE_FIRST_ERROR( HEAPFILE, INVALID_SLOTNO );
    }
}

// **********************************************************
// returns length and pointer to record with RID rid.  The difference
// between this and getRecord is that getRecord copies out the record
// into recPtr, while this function returns a pointer to the record
// in recPtr.
Status HFPage::returnRecord(RID rid, char*& recPtr, int& recLen)
{
    int slotNo = rid.slotNo;

    if (is_set(bitmap,slotNo)) {

        recLen = recSze;                  // return length of record
        recPtr = &(data[slotNo*recSze]);  // return pointer to record

        return OK;
    } else {
        return MINIBASE_FIRST_ERROR( HEAPFILE, INVALID_SLOTNO );
    }
}

// **********************************************************
// Returns the amount of available space on the heap file page.
// You will have to compare it with the size of the record to
// see if the record will fit on the page.
int HFPage::available_space(void)
{
    if (maxRecs)
        return (maxRecs - recCnt) * recSze;
    else
        return(MAX_SPACE - DPFIXED);
}

// **********************************************************
// Returns true if the HFPage is empty, and false otherwise.
// It scans the slot directory looking for a non-empty slot.
bool HFPage::empty(void)
{
    if (maxRecs == 0)
        return true;

    int i;

      // look for an empty slot
    for (i=0; i < maxRecs; i++)
        if (is_set(bitmap,i))
            return false;

    return true;
}

// **********************************************************
// Start from the begining of the slot directory.
// We maintain two offsets into the directory.
//   o first free slot       (ffs) 
//   o current scan position (current_scan_posn)
// Used slots after empty slots are copied to the ffs.
// Shifting the whole array is done rather than filling
// the holes since the array may be sorted...
void HFPage::compact_slot_dir(void)
{
   int  current_scan_posn =  0;
   int  first_free_slot   = -1;   // An invalid position.
   bool move = false;             // Move a record? -- initially false

   while (current_scan_posn < maxRecs) {
       if (is_clr(bitmap,current_scan_posn) && (move == false)) {
           move = true;
           first_free_slot = current_scan_posn;
       } else if (is_set(bitmap,current_scan_posn) && (move == true)) {
//         cout << "Moving " << current_scan_posn << " --> "
//              << first_free_slot << endl;

           set_bit(bitmap,first_free_slot);
           clr_bit(bitmap,current_scan_posn);

             // Need to copy the record as well
           memcpy( &data[first_free_slot*recSze],
                   &data[current_scan_posn*recSze], recSze); 

             // Now make the first_free_slot point to the next free slot.
           first_free_slot++;

           while (is_set(bitmap,first_free_slot))
               first_free_slot++;
       }
       current_scan_posn++;
    }
}

// **********************************************************
