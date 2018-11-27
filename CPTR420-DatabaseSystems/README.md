# Introduction to Database Systems (CPTR 420)
## Project \#3 Description

 The goal of this project is for you to work in groups of two to
  write C++ code to implement the sort-merge-join algorithm in an
  existing database framework.
  
 You must implement the `sortMerge` constructor shown below, which
  joins two relations  *R* and *S*, represented by heapfiles `filename1` and `filename2` using the sort-merge join algorithm.  You are to concatenate each matching pair of records and write it to the heapfile `filename3`.
  
  
```
  class sortMerge {
  public:

    sortMerge(
      char       *filename1,        // Name of heapfile for relation R.
      int         len_in1,          // # of columns in R.
      AttrType    in1[],            // Array containing field types of R.
      short       t1_str_sizes[],   // Array containing size of columns in R.
      int         join_col_in1,     // The join column number of R.
      char       *filename2,        // Name of heapfile for relation S
      int         len_in2,          // # of columns in S.
      AttrType    in2[],            // Array containing field types of S.
      short       t2_str_sizes[],   // Array containing size of columns in S.
      int         join_col_in2,     // The join column number of S.
      char*       filename3,        // Name of heapfile for merged results
      int         amt_of_mem,       // Number of pages available for sorting
      TupleOrder  order,            // Sorting order: Ascending or Descending
      Status&     s                 // Status of constructor
    );
   ~sortMerge();
};
```

## Implementation Notes

The following notes will help you to accomplish this task:

* You will need to make use of the `Sort`, `HeapFile`, and `Scan` classes.
* Call the existing `Sort` constructor to sort the input heapfiles, which means your primary responsibility will be to implement the merging phase of the algorithm.
* Compare the join columns of two tuples, use the `tupleCmp` function declared in `sort.h`.
* Once a scan is opened on a `heapfile`, the scan cursor can be positioned to any record by calling the scan method `position` with a record id argument.  The next call to `getNext` method of Scan will proceed from the new cursor position.
*The error layer for the `sortMerge` class is called `JOINS`.

## Grading
This project will be graded out of 100 points:

* 50 points are awarded for the design and coding of your algorithm
* 30 points are awarded for successfully completing the execution test (run ``make; ./SortMerg`` in the `src` directory to run this test.
* 20 points are awarded for code formatting and documentation (i.e. comments).