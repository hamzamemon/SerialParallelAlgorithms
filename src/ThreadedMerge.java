
public class ThreadedMerge {
    
    public static void parallelMergeSort(int[] array, int threadCount) {
        if(threadCount <= 1) {
            mergeSort(array);
        }
        else if(array.length >= 2) {
            // Split the array in half
            int[] left = new int[array.length / 2];
            int[] right = new int[array.length - left.length];
            System.arraycopy(array, 0, left, 0, left.length);
            System.arraycopy(array, left.length, right, 0, right.length);
            
            Thread lThread = new Thread(new TaskMerge(left, threadCount / 2));
            Thread rThread = new Thread(new TaskMerge(right, threadCount / 2));
            lThread.start();
            rThread.start();
            
            try {
                lThread.join();
                rThread.join();
            }
            catch(InterruptedException ie) {
            }
            
            // merge them back together
            merge(left, right, array);
        }
    }
    
    // Arranges the elements of the given array into sorted order
    // using the "merge sort" algorithm, which splits the array in half,
    // recursively sorts the halves, then merges the sorted halves.
    // It is O(N log N) for all inputs.
    private static void mergeSort(int[] array) {
        if(array.length >= 2) {
            int[] left = new int[array.length / 2];
            int[] right = new int[array.length - left.length];
            System.arraycopy(array, 0, left, 0, left.length);
            System.arraycopy(array, left.length, right, 0, right.length);
            
            // sort the halves
            mergeSort(left);
            mergeSort(right);
            
            // merge them back together
            merge(left, right, array);
        }
    }
    
    // Combines the contents of sorted left/right arrays into output array a.
    // Assumes that left.length + right.length == a.length.
    private static void merge(int[] left, int[] right, int[] a) {
        int i1 = 0;
        int i2 = 0;
        for(int i = 0; i < a.length; i++) {
            if(i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                a[i] = left[i1];
                i1++;
            }
            else {
                a[i] = right[i2];
                i2++;
            }
        }
    }
}
