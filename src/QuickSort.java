/**
 * Implementation of Serial QuickSort.
 */
public class QuickSort {
    
    /**
     * Serial QuickSort.
     *
     * @param array the array to sort
     */
    public void serialSort(int[] array) {
        int[] temp = array.clone();
        sort(temp, 0, array.length - 1);
    }
    
    /**
     * Partition the array by taking the last element as the pivot, places it correctly,
     * and moves all the smaller elements to the left and all the larger elements to the right of pivot
     *
     * @param array the array to sort
     * @param low   the starting index
     * @param high  the ending index
     *
     * @return the pivot
     */
    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for(int j = low; j < high; j++) {
            if(array[j] <= pivot) {
                i++;
                
                // Swap arr[i] and arr[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        
        // Swap arr[i+1] and arr[high] (or pivot)
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        
        return i + 1;
    }
    
    /**
     * QuickSort algorithm
     *
     * @param array the array to be sorted
     * @param low   the starting index
     * @param high  the ending index
     */
    private void sort(int[] array, int low, int high) {
        if(low < high) {
            // pi is partitioning index, arr[pi] is now at right place
            int pi = partition(array, low, high);
            
            // Recursively sort elements before partition and after partition
            sort(array, low, pi - 1);
            sort(array, pi + 1, high);
        }
    }
}
