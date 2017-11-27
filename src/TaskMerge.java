

public class TaskMerge implements Runnable {
    
    private int[] array;
    private int threadCount;
    
    public TaskMerge(int[] array, int threadCount) {
        this.array = array;
        this.threadCount = threadCount;
    }
    
    public void run() {
        ThreadedMerge.parallelMergeSort(array, threadCount);
    }
}
