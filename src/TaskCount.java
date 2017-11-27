

import java.util.concurrent.Callable;

public class TaskCount implements Callable<int[]> {
    
    private int[] array;
    
    public TaskCount(int[] array) {
        this.array = array;
    }
    
    @Override
    public int[] call() throws Exception {
        return ThreadedCount.sort(array);
    }
}
