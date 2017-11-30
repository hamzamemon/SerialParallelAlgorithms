import java.util.Random;

public class Driver {
    
    public static final int PROCESSES = 4;
    
    public static void main(String[] args) {
        int length = 1024;
        int runs = 5;
        
        System.out.println("--------------------Serial---------------------");
        System.out.printf("%8s %12s %12s %12s\n", "", "Counting", "Merge", "Quick");
        
        MergeSort mergeSort = new MergeSort();
        CountingSort countingSort = new CountingSort();
        QuickSort quickSort = new QuickSort();
        
        int[][] arrays = new int[runs][];
        
        for(int i = 0; i < runs; i++) {
            int[] array = new int[length];
            Random random = new Random();
            for(int j = 0; j < array.length; j++) {
                array[j] = random.nextInt(length);
            }
            arrays[i] = array;
            
            length *= 8;
        }
        
        
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
            ThreadedCount.parallelCountingSort(array);
            long end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            ThreadedMerge.parallelMergeSort(array.clone(), PROCESSES);
            end = System.nanoTime();
            printStat(start, end);
            
            start = System.nanoTime();
            ThreadedQuick.parallelQuickSort(array);
            end = System.nanoTime();
            printStat(start, end);
            
            System.out.println();
        }
        
        System.exit(0);
    }
    
    private static void printStat(long start, long end) {
        long diff = end - start;
        
        // Convert nanoseconds to milliseconds
        double millis = diff / 1_000_000.0;
        System.out.printf(" %9.1f ms", millis);
        
    }
}
