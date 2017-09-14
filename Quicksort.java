/**
 * Class for sorting the WordCount objects efficiently.
 * 
 * @author Anna Rafferty
 * @author Yitong Chen
 */
public class Quicksort {
    // initialize a basic MIN_SIZE
    public static int MIN_SIZE = 10;

    /** 
     * Sorts an array in decreasing order using quicksort.
     * Quicksort code is based off of Carrano and Henry. 
     * array will be sorted in place.
     */
    public static void quicksort(WordCount[] array) {
        quicksort(array, 0, array.length - 1);
    }
    
    /** 
     * Helper method:
     * Sorts the part of the array a between index first and index last
     *  in decreasing order using quicksort.
     */
    private static void quicksort(WordCount[] a, int first, int last) {
        if (last - first + 1 < MIN_SIZE) {
            insertionSort(a, first, last);
        } else {
            // create the partition: Smaller | Pivot | Larger
            int pivotIndex = partition (a, first, last);
            // sort subarrays Smaller and Larger
            quicksort(a, first, pivotIndex - 1);
            quicksort(a, pivotIndex + 1, last);
        }
    }
    
    /** 
     * Orders two given array entries into descending order
     * so that a[i] <= a[j].
     * @param a an array of objects
     * @param i an integer >= 0 and < array.length
     * @param j an integer >= 0 and < array.length
     */
    private static void order (WordCount[] a, int i, int j) {
        if (a[i].getCount() < a[j].getCount()) {
            swap(a, i, j);
        }
    }
    
    /** 
     * Swaps the array entries array[i] and array[j]. 
     */
    private static void swap (WordCount[] array, int i, int j) {
        WordCount temp = array[i];
        array [i] = array[j];
        array [j] = temp;
    }
    
    /** 
     * Sorts the first, middle, and last entries of an array
     * into descending order.
     * @param a an array of ints
     * @param first the integer index of the first array entry;
     * first >= 0 and < a.length
     * @param mid the integer index of the middle array entry
     * @param last the integer index of the last array entry;
     * last - first >= 2, last < a.length 
     */
    private static void sortFirstMiddleLast (WordCount[] a, int first, int mid, int last){
        order(a, first, mid); // make a[first] >= a[mid]
        order(a, mid, last); // make a[mid] >= a[last]
        order(a, first, mid); // make a[first] >= a[mid]
    }
    
    /**
     * Partitions the array a so that the entries in [first, last]
     * are rearranged into a "smaller" part and a larger part.
     * The return value is the location of the pivot - all the entries
     * located to the left of that index are greater than the value at the
     * pivot index, and all the entries to the right of that index are
     * less than the pivot.
     */
    private static int partition (WordCount[] a, int first, int last) {
        int mid = (first + last) / 2;
        sortFirstMiddleLast (a, first, mid, last);
        // Assertion: The pivot is a[mid]; a[first] >= pivot and
        // a[last] <= pivot, so do not compare these two array entries
        // with pivot.
        // move pivot to next-to-last position in array
        swap(a, mid, last - 1);
        int pivotIndex = last - 1;
        WordCount pivot = a[pivotIndex];
        // determine subarrays Smaller = a[first..endSmaller]
        // and Larger = a[endSmaller+1..last-1]
        // such that entries in Smaller are >= pivot and
        // entries in Larger are <= pivot; initially, these subarrays are empty
        int indexFromLeft = first + 1;
        int indexFromRight = last - 2;
        boolean done = false;
        while (!done) {
            // starting at beginning of array, leave entries that are > pivot;
            // locate first entry that is <= pivot; you will find one,
            // since last entry is <= pivot
            while (a[indexFromLeft].getCount() > pivot.getCount()) {
                indexFromLeft++;
            }
            // starting at end of array, leave entries that are < pivot;
            // locate first entry that is >= pivot; you will find one,
            // since first entry is >= pivot
            while (a[indexFromRight].getCount() < pivot.getCount()) {
                indexFromRight--;
            }

            if (indexFromLeft < indexFromRight) {
                swap(a, indexFromLeft, indexFromRight);
                indexFromLeft++;
                indexFromRight--;
            } else {
                done = true;
            }
        } 


        // place pivot between Smaller and Larger subarrays
        swap (a, pivotIndex, indexFromLeft);
        pivotIndex = indexFromLeft;
        return pivotIndex;
    }

    
    /**
     * Sorts the specified array in decreasing order using insertion sort.
     */
    public static void insertionSort(WordCount[] a) {
        insertionSort(a, 0, a.length-1);
    }
    
    /**
     * Helper method for insertion sort to enable insertion sorting only part
     * of an array.
     */
    private static void insertionSort(WordCount[] array, int first, int last) {    
        for(int i = first+1; i <= last; i++) {
            //i is the index in the array we're going to find the right place for
            int j = i;
            while(j > first && array[j-1].getCount() < array[j].getCount()) {
                WordCount numToSwapOut = array[j];
                array[j] = array[j-1];
                array[j-1] = numToSwapOut;
                j--;
            }
        }
    }
}
