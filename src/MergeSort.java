/**
 * Implementation of Serial MergeSort.
 */
public class MergeSort {
    
    /**
     * Merge two arrays into result array
     *
     * @param first  left array
     * @param second right array
     * @param result resulting array
     */
    private static void merge(int[] first, int[] second, int[] result) {
        int iFirst = 0;
        int iSecond = 0;
        
        int j = 0;
        
        // merge two arrays
        while(iFirst < first.length && iSecond < second.length) {
            if(first[iFirst] < second[iSecond]) {
                result[j] = first[iFirst];
                iFirst++;
            }
            else {
                result[j] = second[iSecond];
                iSecond++;
            }
            
            j++;
        }
        
        // copy two arrays into result array
        System.arraycopy(first, iFirst, result, j, first.length - iFirst);
        System.arraycopy(second, iSecond, result, j, second.length - iSecond);
    }
    
    /**
     * Serial Merge Sort.
     *
     * @param array the array to sort
     */
    public void mergeSort(int[] array) {
        int[] temp = array.clone();
        mergeSort(temp, 0);
    }
    
    /**
     * Merge sort algorithm
     *
     * @param array the array to sort
     * @param x     override function
     */
    public void mergeSort(int[] array, int x) {
        if(array.length > 1) {
            // Split the array in half
            int[] first = new int[array.length / 2];
            int[] second = new int[array.length - first.length];
            System.arraycopy(array, 0, first, 0, first.length);
            System.arraycopy(array, first.length, second, 0, second.length);
            
            // Sort each half
            mergeSort(first, x);
            mergeSort(second, x);
            
            // Merge the halves together
            merge(first, second, array);
        }
    }
}
