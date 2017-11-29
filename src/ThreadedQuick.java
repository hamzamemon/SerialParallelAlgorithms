 
/**
 * Implements Parallel Quicksort
 * 
 * Megan Bayer, Maria Bamundo, Hamza Memon
 */
public class ThreadedQuick {
    private static final int MINIMUM_THREAD_WORKLOAD = Integer.MAX_VALUE;

    public static void sort(int[] array) {
        int[] temp = array.clone();
        sort(temp, 0, temp.length);
    }

    public static void sort(int[] array, int fromIndex, int toIndex) {
        int processes = Driver.PROCESSES;
        int rangeLength = toIndex - fromIndex;
        sortImpl(array,fromIndex,toIndex, processes);
    }

    private static void sortImpl(int[] array,int fromIndex,int toIndex, int processes) {
        if (cores <= 1) {
            quickSort(array, fromIndex, toIndex);
        }
        int rangeLength = toIndex - fromIndex;
        int distance = rangeLength / 4;

        int a = array[fromIndex + distance];
        int b = array[fromIndex + (rangeLength >>> 1)];
        int c = array[toIndex - distance];

        int pivot = median(a, b, c);
        int leftPL = 0;
        int rightPL = 0;
        int index = fromIndex;

        while (index < toIndex - rightPL) {
            int current = array[index];

            if (current > pivot) {
                ++rightPL;
                swap(array, toIndex - rightPL, index);
            } else if (current < pivot) {
                swap(array, fromIndex + leftPL, index);
                ++index;
                ++leftPL;
            } else {
                ++index;
            }
        }

        TaskQuick leftThread = new TaskQuick(array,fromIndex,fromIndex + leftPL, processes/2);
        TaskQuick rightThread = new TaskQuick(array,toIndex - rightPL,toIndex, processes - processes/2);

        leftThread.start();
        rightThread.start();

        try {
            leftThread.join();
            rightThread.join();
        } catch (InterruptedException ex) {
            throw new IllegalStateException(
                    "Parallel quicksort threw an InterruptedException.");
        }
    }
    
    public static int median(int a, int b, int c) {
        if (a <= b) {
            if (c <= a) {
                return a;
            }

            return b <= c ? b : c;
        } 

        if (c <= b) {
            return b;
        }

        return a <= c ? a : c;
    }
    
    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    private static final class TaskQuick extends Thread {

        private final int[] array;
        private final int fromIndex;
        private final int toIndex;
        private final int processes;

        TaskQuick(int[] array,int fromIndex,int toIndex, int processes) {
            this.array = array;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
            this.processes = processes;
        }

        @Override
        public void run() {
            sortImpl(array, fromIndex, toIndex, processes);
        }
    }
private int partition(int arr[], int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for(int j = low; j < high; j++) {
            if(arr[j] <= pivot) {
                i++;
                
                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }
    
    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    private void quickSort(int arr[], int low, int high) {
        if(low < high) {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);
            
            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi - 1);
            sort(arr, pi + 1, high);
        }
    }
}
