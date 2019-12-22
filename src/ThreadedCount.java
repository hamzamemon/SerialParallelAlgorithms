import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Implementation of Parallel CountingSort
 */
public class ThreadedCount {
    
    /**
     * Parallel counting sort.
     *
     * @param array   the array to sort
     * @param threads the number of threads
     */
    public void parallelCountingSort(int[] array, int threads) {
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        
        int size = array.length / threads;
        int[] sumCounts = new int[array.length];
        
        for(int i = 0; i < threads; i++) {
            int[] subsetArray = Arrays.copyOfRange(array, i * size, i * size + (size - 1));
            Future<int[]> future = executorService.submit(new TaskCount(subsetArray));
            int[] result;
            try {
                result = future.get();
                for(int j = 1; j < result.length; j++) {
                    sumCounts[result[j]]++;
                }
            }
            catch(InterruptedException | ExecutionException e) {
                executorService.shutdown();
            }
        }
        
        int[] sortedArray = new int[array.length];
        for(int i = 0, k = 0; i < sumCounts.length; i++) {
            for(int j = 0; j < sumCounts[i]; j++) {
                sortedArray[k] = i;
                k++;
            }
        }
        
        executorService.shutdown();
    }
    
    /**
     * Class to represent threaded item
     */
    private static class TaskCount implements Callable<int[]> {
        
        private int[] array;
        
        /**
         * Instantiates a new Task count.
         *
         * @param array the array
         */
        public TaskCount(int[] array) {
            this.array = array;
        }
        
        @Override
        public int[] call() {
            return sort(array);
        }
        
        /**
         * Sort int array.
         *
         * @param array the array to srt
         *
         * @return the array
         */
        private int[] sort(int[] array) {
            // Get max value of array to determine Count size
            int max = 0;
            for(int x : array) {
                if(x > max) {
                    max = x;
                }
            }
            
            int[] counts = new int[max + 1];
            for(int t : array) {
                counts[t]++;
            }
            
            /*
            Update the Count[] so that each index will store the sum till previous step. (Count[i]=Count[i] + Count[i-1]).
            Now updated Count[] array will reflect the actual position of each integer in Result[].
             */
            for(int i = 1; i < counts.length; i++) {
                counts[i] += counts[i - 1];
            }
            
            /*
            Now navigate the input array taking one element at a time
            Count[input[i]] will tell you the index position of input[i] in Result[].
            When you do that, decrease the count in Count[input[i]] by 1.
             */
            int[] result = new int[array.length + 1];
            for(int x : array) {
                result[counts[x]] = x;
                counts[x]--;
            }
            
            return result;
        }
    }
}
