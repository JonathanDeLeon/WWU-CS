package com.jonathan.project;

public class QuickSort {
    /**
     * This function takes the last element as pivot
     * and places all smaller elements to the left of the pivot
     * and all greater elements to the right of the pivot
     * @param arr
     * @param low
     * @param high
     * @return
     */
    public int parition(int arr[], int low, int high){
        //Set pivot to last element
        int pivot = arr[high];
        int index = (low - 1);
        for(int i = low; i <= high-1; i++){
            //If current element is smaller than or equal to pivot
           if(arr[i] <= pivot) {
               index++;

               //Swap arr[index] and arr[i]
               // System.out.println("Swapping "+arr[index]+" and "+arr[i]);
               int temp = arr[index];
               arr[index] = arr[i];
               arr[i] = temp;
           }
        }
        //Swap arr[index+1] and arr[high]
        // System.out.println("Swapping "+arr[index+1]+" and "+arr[high]);
        int temp = arr[index+1];
        arr[index+1] = arr[high];
        arr[high] = temp;
        return index+1;

    }

    /**
     * Main funciton that implements QuickSort
     * @param arr Array to be sorted
     * @param low Starting index
     * @param high Ending index
     */
    public void quickSort(int arr[], int low, int high){
        if(low < high){
            int index = parition(arr, low, high);
//            printArray(arr);

            // Recursively sort elements
            quickSort(arr, low, index-1);
            quickSort(arr, index+1, high);
        }
    }

    /**
     * Prints the contents of a given array
     * @param arr
     */
    public static void printArray(int arr[]){
        int n = arr.length;
        for(int i=0; i < n; i++){
            System.out.print(arr[i]+ " ");
        }
        System.out.print("\n");
    }


    public static void main(String args[]){
        int arr[] = {10, 7, 8, 9, 1, 5};
        int n = arr.length;

        System.out.println("Un-sorted array:");
        printArray(arr);

        QuickSort sort = new QuickSort();
        sort.quickSort(arr, 0, n-1);

        System.out.println("Sorted array:");
        printArray(arr);

    }
}
