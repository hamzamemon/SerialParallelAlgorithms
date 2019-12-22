import java.util.Random;

/**
 * Main class.
 */
public class Driver {
    
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Number of threads
        int threads = 4;
        
        // Initial length of array
        int length = 1024;
        
        // Number of runs
        int runs = 5;
        
        // Create variables to be used
        MergeSort mergeSort = new MergeSort();
        CountingSort countingSort = new CountingSort();
        QuickSort quickSort = new QuickSort();
        ThreadedCount threadedCount = new ThreadedCount();
        ThreadedMerge threadedMerge = new ThreadedMerge();
        ThreadedQuick threadedQuick = new ThreadedQuick();
        int[][] arrays = new int[runs][];
        
        // Build data for each run based on size of "length"
        // Increase length by 8 each time to get a good increase and better see results
        for(int i = 0; i < runs; i++) {
            int[] array = new int[length];
            Random random = new Random();
            for(int j = 0; j < array.length; j++) {
                array[j] = random.nextInt(length);
            }
            
            arrays[i] = array;
            length *= 8;
        }
        
        System.out.println("--------------------Serial---------------------");
        System.out.printf("%8s %12s %12s %12s\n", "", "Counting", "Merge", "Quick");
        for(int[] array : arrays) {
            System.out.printf("%8d", array.length);
            
            long start = System.nanoTime();
            countingSort.serialSort(array);
            long end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            mergeSort.mergeSort(array);
            end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            quickSort.serialSort(array);
            end = System.nanoTime();
            printStat(start, end);
            
            System.out.println();
        }
        
        System.out.println("\n-------------------Parallel--------------------");
        System.out.printf("%8s %12s %12s %12s\n", "", "Counting", "Merge", "Quick");
        for(int[] array : arrays) {
            System.out.printf("%8d", array.length);
            
            long start = System.nanoTime();
            threadedCount.parallelCountingSort(array, threads);
            long end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            threadedMerge.parallelMergeSort(array.clone(), threads);
            end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            threadedQuick.quicksort(array);
            end = System.nanoTime();
            printStat(start, end);
            
            System.out.println();
        }
        
        System.exit(0);
    }
    
    /**
     * Print the time taken in milliseconds
     *
     * @param start beginning time as nanoseconds
     * @param end   ending time as nanoseconds
     */
    private static void printStat(long start, long end) {
        long diff = end - start;
        
        double millis = diff / 1_000_000.0;
        System.out.printf(" %9.1f ms", millis);
        
    }
}
