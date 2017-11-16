
#include <string.h>
#include <assert.h>
#include "sortMerge.h"

// Error Protocall:

enum ErrCodes {};

static const char* ErrMsgs[] = 	{};

static error_string_table ErrTable( JOINS, ErrMsgs );

sortMerge::sortMerge(
    char*           filename1,      // Name of HeapFile for relation R
    int             len_in1,        // # of columns in R.
    AttrType        in1[],          // Array containing field types of R.
    short           t1_str_sizes[], // Array containing size of columns in R
    int             join_col_in1,   // The join column of R 

    char*           filename2,      // Name of HeapFile for relation S
    int             len_in2,        // # of columns in S.
    AttrType        in2[],          // Array containing field types of S.
    short           t2_str_sizes[], // Array containing size of columns in S
    int             join_col_in2,   // The join column of S

    char*           filename3,      // Name of HeapFile for merged results
    int             amt_of_mem,     // Number of pages available
    TupleOrder      order,          // Sorting order: Ascending or Descending
    Status&         s               // Status of constructor
){
    // Creating heap files and sorting them while checking for any status errors
    char* filename1_sorted = "sorted1";
    char* filename2_sorted = "sorted2";
    HeapFile* sorted1 = new HeapFile(filename1_sorted, s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }
    HeapFile* sorted2 = new HeapFile(filename2_sorted, s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }
    HeapFile* outFile = new HeapFile(filename3, s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }
    Sort(filename1, filename1_sorted, len_in1, in1, t1_str_sizes, join_col_in1, order, amt_of_mem, s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }
    Sort(filename2, filename2_sorted, len_in2, in2, t2_str_sizes, join_col_in2, order, amt_of_mem, s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }

    // Initialize our scanners for a sorted HeapFil
    Scan* scan1 = sorted1->openScan(s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }
    Scan* scan2 = sorted2->openScan(s);
    if(s != OK){
         MINIBASE_CHAIN_ERROR(JOINS,s);
         return;
    }

    RID rid;    // RID for output file
    RID rid1;   // RID cursor position in relation R
    RID rid2;   // RID cursor position in relation S
    RID ridTmp; // RID cursor tracker position in relation S

    // Retrieve size of a record in R
    int recsize1 = 0;
    for(int i = 0; i < len_in1; i++) {
        recsize1 += t1_str_sizes[i];
    }

    // Retrieve size of a record in S
    int recsize2 = 0;
    for(int i = 0; i < len_in2; i++) {
        recsize2 += t2_str_sizes[i];
    }

    // Initialize memory to temporarily store a record
    char* rec1 = new char[recsize1];
    char* rec2 = new char[recsize2];
    char* recResult = new char[recsize1+recsize2];

    /**
      Sort-merge Join Algorithm 
      Note: To verbosely view algorithm, uncomment the three
            'cout' lines 
     **/
    s = scan1->getNext(rid1, rec1, recsize1);
    if(s == OK) {
        s = scan2->getNext(rid2, rec2, recsize2);
        ridTmp = rid2;
    }
    while(s == OK) {
        // tupleCmp from 'sort.C' 
        // returns 0 if equal, 1 if greater than, -1 if less than
        if(tupleCmp(rec1, rec2) == 0) {
           // cout << *(int*)(rec1) << " == " << *(int*)(rec2) << endl;
            memcpy(recResult, rec1, recsize1);              // memcpy moves rec1 && rec2 into recResult
            memcpy(recResult+recsize1, rec2, recsize2);
            s = outFile->insertRecord(recResult, recsize1+recsize2, rid);
            s = scan2->getNext(rid2, rec2, recsize2);
        }
        else if(tupleCmp(rec1, rec2) == 1) {
           // cout << *(int*)(rec1) << " > " << *(int*)(rec2) << endl;
            s = scan2->getNext(rid2, rec2, recsize2);
            ridTmp = rid2;
        }
        else {
           // cout << *(int*)(rec1) << " < " << *(int*)(rec2) << endl;
            s = scan1->getNext(rid1, rec1, recsize1);
            if(s == OK) {
                s = scan2->position(ridTmp);
                s = scan2->getNext(rid2, rec2, recsize2);
                s = OK;     // If we are at the end of scan2 but not scan1 keep going
            } 
        }
    }

    // Destruct scanners and HeapFiles to deallocate memory
    delete scan1;
    s = sorted1->deleteFile();
    delete scan2;
    s = sorted2->deleteFile();
}

sortMerge::~sortMerge()
{
}
