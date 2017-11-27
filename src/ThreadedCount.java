import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedCount {
    
    public static int[] sort(int[] array) {
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
        
        // Update the Count[] so that each index will store the sum till
        // previous step. (Count[i]=Count[i] + Count[i-1]).
        // Now updated Count[] array will reflect the actual position of each
        // integer in Result[].
        for(int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }
        
        // Now navigate the input array taking one element at a time,
        // counts[input[i]] will tell you the index position of input[i] in
        // Result[]. When you do that, decrease the count in counts[input[i]] by
        // 1.
        
        int[] result = new int[array.length + 1];
        for(int x : array) {
            result[counts[x]] = x;
            counts[x]--;
        }
        
        return result;
    }
    
    public static void parallelCountingSort(int[] array) {
        int processes = Driver.PROCESSES;
        
        ExecutorService executorService = Executors.newFixedThreadPool(processes);
        
        int size = array.length / processes;
        int[] sumCounts = new int[array.length];
        
        for(int i = 0; i < processes; i++) {
            int[] subsetArray = Arrays.copyOfRange(array, (i * size), i * size + (size - 1));
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
        
        //System.out.println(Arrays.toString(sumCounts));
        
        int[] sortedArray = new int[array.length];
        for(int i = 0, k = 0; i < sumCounts.length; i++) {
            for(int j = 0; j < sumCounts[i]; j++) {
                sortedArray[k] = i;
                k++;
            }
        }
        
        //System.out.println(Arrays.toString(sortedArray));
        executorService.shutdown();
    }
}
