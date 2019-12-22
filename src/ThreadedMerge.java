/**
 * Implementation of Parallel MergeSort
 */
public class ThreadedMerge {
    
    /**
     * Merge sort algorithm by splitting array in half and merging back together
     *
     * @param array the array to short
     */
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
    
    /**
     * Combines the left and right halves into the results array
     *
     * @param left   the left side of the array
     * @param right  the right side of the array
     * @param result the merged array
     */
    // Combines the contents of sorted left/right arrays into output array a.
    // Assumes that left.length + right.length == a.length.
    private static void merge(int[] left, int[] right, int[] result) {
        int i1 = 0;
        int i2 = 0;
        for(int i = 0; i < result.length; i++) {
            if(i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
                result[i] = left[i1];
                i1++;
            }
            else {
                result[i] = right[i2];
                i2++;
            }
        }
    }
    
    /**
     * Parallel merge sort.
     *
     * @param array       the array to sort
     * @param threadCount the number of threads
     */
    public void parallelMergeSort(int[] array, int threadCount) {
        if(threadCount <= 1) {
            mergeSort(array);
        }
        else if(array.length >= 2) {
            // Split the array in half
            int[] left = new int[array.length / 2];
            int[] right = new int[array.length - left.length];
            System.arraycopy(array, 0, left, 0, left.length);
            System.arraycopy(array, left.length, right, 0, right.length);
            
            // Create the two halves of the array
            Thread lThread = new Thread(new TaskMerge(left, threadCount / 2));
            Thread rThread = new Thread(new TaskMerge(right, threadCount / 2));
            lThread.start();
            rThread.start();
            
            try {
                lThread.join();
                rThread.join();
            }
            catch(InterruptedException ie) {
                System.out.println("Cannot join threads: " + ie);
            }
            
            // merge them back together
            merge(left, right, array);
        }
    }
    
    private class TaskMerge implements Runnable {
        private int[] array;
        private int threadCount;
        
        /**
         * Instantiates a new Task merge.
         *
         * @param array       the array
         * @param threadCount the thread count
         */
        public TaskMerge(int[] array, int threadCount) {
            this.array = array;
            this.threadCount = threadCount;
        }
    
        /**
         * Runnable method
         */
        @Override
        public void run() {
            parallelMergeSort(array, threadCount);
        }
    }
}
