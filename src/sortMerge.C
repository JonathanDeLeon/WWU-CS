
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
    char* filename1_sorted = "sorted1";
    char* filename2_sorted = "sorted2";
    HeapFile* sorted1 = new HeapFile(filename1_sorted, s);
    HeapFile* sorted2 = new HeapFile(filename2_sorted, s);
    HeapFile* outFile = new HeapFile(filename3, s);
    Sort* sort1 = new Sort(filename1, filename1_sorted, len_in1, in1, t1_str_sizes, join_col_in1, order, amt_of_mem, s);
    Sort* sort2 = new Sort(filename2, filename2_sorted, len_in2, in2, t2_str_sizes, join_col_in2, order, amt_of_mem, s);

    Scan* scan1 = sorted1->openScan(s);
    Scan* scan2 = sorted2->openScan(s);

    RID rid;
    RID rid1;
    RID rid2;
    RID ridTmp;

    int recsize1 = 0;
    for(int i = 0; i < len_in1; i++) {
        recsize1 += t1_str_sizes[i];
    }

    int recsize2 = 0;
    for(int i = 0; i < len_in2; i++) {
        recsize2 += t2_str_sizes[i];
    }

    char* rec1[len_in1];
    for(int i = 0; i < len_in1; i++) {
        rec1[i] = new char[t1_str_sizes[i]];
    }
//    char* rec1 = new char[recsize1];

    char* rec2[len_in2];
    for(int i = 0; i < len_in2; i++) {
        rec2[i] = new char[t2_str_sizes[i]];
    }

    int results = 0;

    s = scan1->getNext(rid1, (char*)&rec1, recsize1);
//    s = outFile->insertRecord((char*)&rec1, recsize1, rid1);
//    cout << "Post-assignment: '";
//    cout << *(int*)(rec1);
//    cout << "'" << endl;

    if(s == OK) {
        s = scan2->getNext(rid2, (char*)&rec2, recsize2);
        ridTmp = rid2;
    }
    while(s == OK) {
        if(*(int*)(rec1) == *(int*)(rec2)) {
            cout << *(int*)(rec1) << " == " << *(int*)(rec2) << endl;
            //TODO: Append records
            s = outFile->insertRecord((char*)&rec1, recsize1+recsize2, rid);
            s = scan2->getNext(rid2, (char*)&rec2, recsize2);
        }
        else if(*(int*)(rec1) > *(int*)(rec2))  {
            cout << *(int*)(rec1) << " > " << *(int*)(rec2) << endl;
            s = scan2->getNext(rid2, (char*)&rec2, recsize2);
            ridTmp = rid2;
        }
        else {
            cout << *(int*)(rec1) << " < " << *(int*)(rec2) << endl;
            s = scan1->getNext(rid1, (char*)&rec1, recsize1);
            if(s == OK) {
                s = scan2->position(ridTmp);
                s = scan2->getNext(rid2, (char*)&rec2, recsize2);
            }
        }
    }


    delete scan1;
    s = sorted1->deleteFile();

    delete scan2;
    s = sorted2->deleteFile();
}

sortMerge::~sortMerge()
{
}
