 
/**
 * Implements Parallel Quicksort
 * 
 * Megan Bayer, Maria Bamundo, Hamza Memon
 */
public class ThreadedQuick {
    private static final int MINIMUM_THREAD_WORKLOAD = Integer.MAX_VALUE;

    public static void sort(int[] array) {
        sort(array, 0, array.length);
    }

    public static void sort(int[] array, int fromIndex, int toIndex) {
        int rangeLength = toIndex - fromIndex;
        sortImpl(array,fromIndex,toIndex);
    }

    private static void sortImpl(int[] array,int fromIndex,int toIndex) {
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

        TaskQuick leftThread = new TaskQuick(array,fromIndex,fromIndex + leftPL);
        TaskQuick rightThread = new TaskQuick(array,toIndex - rightPL,toIndex);

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

        TaskQuick(int[] array,int fromIndex,int toIndex) {
            this.array = array;
            this.fromIndex = fromIndex;
            this.toIndex = toIndex;
        }

        @Override
        public void run() {
            sortImpl(array, fromIndex, toIndex);
        }
    }
}