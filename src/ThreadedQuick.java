import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of Parallel QuickSort.
 */
public class ThreadedQuick {
    // number of threads to use for sorting.
    private static final int N_THREADS = 4;
    
    // multiple to use when determining when to fallback
    private static final int FALLBACK = 2;
    
    // thread pool
    private static Executor pool = Executors.newFixedThreadPool(N_THREADS);
    
    /**
     * Main method used for sorting from clients. Input is sorted in place using multiple threads.
     *
     * @param input The array to sort.
     */
    public void quicksort(int[] input) {
        int[] temp = input.clone();
        final AtomicInteger count = new AtomicInteger(1);
        pool.execute(new QuicksortRunnable(temp, 0, temp.length - 1, count));
        try {
            synchronized(count) {
                count.wait();
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sorts a section of an array using Quicksort. The method used is not technically recursive as it just creates new
     * runnables and hands them off to the ThreadPoolExecutor.
     */
    private static class QuicksortRunnable implements Runnable {
        // the array to sort
        private final int[] values;
        
        // the starting index of the section of the array to be sorted
        private final int left;
        
        // the ending index of the section of the array to be sorted
        private final int right;
        
        // the number of threads currently executing
        private final AtomicInteger count;
        
        /**
         * Constructor
         *
         * @param values the array to sort.
         * @param left   the starting index of the section of the array to be sorted.
         * @param right  the ending index of the section of the array to be sorted.
         * @param count  the number of threads currently executing
         */
        public QuicksortRunnable(int[] values, int left, int right, AtomicInteger count) {
            this.values = values;
            this.left = left;
            this.right = right;
            this.count = count;
        }
        
        /**
         * The thread's run logic. When this thread is done doing its stuff it checks to see if all other threads are as
         * well. If so, then we notify the count object so Sorter.quicksort stops blocking.
         */
        @Override
        public void run() {
            quicksort(left, right);
            synchronized(count) {
                /*
                 * getAndDecrement() returns the old value
                 * If the old value is 1, then we know that the actual value is 0
                 */
                if(count.getAndDecrement() == 1) {
                    count.notify();
                }
            }
        }
        
        /**
         * Method which actually does the sorting. Falls back on recursion if there are a certain number of queued /
         * running tasks.
         *
         * @param pLeft  the left index of the sub array to sort.
         * @param pRight the right index of the sub array to sort.
         */
        private void quicksort(int pLeft, int pRight) {
            if(pLeft < pRight) {
                int storeIndex = partition(pLeft, pRight);
                if(count.get() >= FALLBACK * N_THREADS) {
                    quicksort(pLeft, storeIndex - 1);
                    quicksort(storeIndex + 1, pRight);
                }
                else {
                    count.getAndAdd(2);
                    pool.execute(new QuicksortRunnable(values, pLeft, storeIndex - 1, count));
                    pool.execute(new QuicksortRunnable(values, storeIndex + 1, pRight, count));
                }
            }
        }
        
        /**
         * Partitions the portion of the array between indexes left and right, inclusively, by moving all elements less
         * than values[pivotIndex] before the pivot, and the greater or equal elements after it.
         *
         * @param pLeft  the left index of the sub array to sort
         * @param pRight the right index of the sub array to sort
         *
         * @return The final index of the pivot value.
         */
        private int partition(int pLeft, int pRight) {
            int pivotValue = values[pRight];
            int storeIndex = pLeft;
            for(int i = pLeft; i < pRight; i++) {
                if(values[i] < pivotValue) {
                    swap(i, storeIndex);
                    storeIndex++;
                }
            }
            swap(storeIndex, pRight);
            return storeIndex;
        }
        
        /**
         * Simple swap method.
         *
         * @param left  the index of the first value to swap with the second value.
         * @param right the index of the second value to swap with the first value.
         */
        private void swap(int left, int right) {
            int temp = values[left];
            values[left] = values[right];
            values[right] = temp;
        }
    }
}
