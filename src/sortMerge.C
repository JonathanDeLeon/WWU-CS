
#include <string.h>
#include <assert.h>
#include "sortMerge.h"

// Error Protocall:

enum ErrCodes {};

static const char* ErrMsgs[] = 	{};

static error_string_table ErrTable( JOINS, ErrMsgs );

struct _rec {
    int	key;
    char	filler[4];
};

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
    Sort* sort1 = new Sort(filename1, filename1_sorted, len_in1, in1, t1_str_sizes, join_col_in1, order, amt_of_mem, s);
    Sort* sort2 = new Sort(filename2, filename2_sorted, len_in2, in2, t2_str_sizes, join_col_in2, order, amt_of_mem, s);

    Scan* scan1 = sorted1->openScan(s);
    Scan* scan2 = sorted2->openScan(s);

    RID rid;
    int sortlen = amt_of_mem*MINIBASE_PAGESIZE; 	// The byte size of memory allocated.
    char* _sort_area = new char[sortlen]; 		// Allocated memory.


    int recsize = 0;
    for(int i = 0; i < sizeof(t1_str_sizes)/sizeof(t1_str_sizes[0]); i++) {
        recsize += t1_str_sizes[i];
    }

    struct _rec rec1;
    memcpy(rec1.filler,"    ", 4);

    HeapFile* outFile = new HeapFile(filename3, s);

    char rec[recsize];

    scan1->getNext(rid, rec, recsize);
    rec1.key = (*((struct _rec*)&rec)).key;
    s = outFile->insertRecord((char*)&rec1,sizeof(rec1),rid);



    delete scan1;
    s = sorted1->deleteFile();

    delete scan2;
    s = sorted2->deleteFile();
}

sortMerge::~sortMerge()
{
}
