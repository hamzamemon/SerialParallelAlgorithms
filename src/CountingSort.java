/**
 * Implementation of Serial CountingSort
 */
public class CountingSort {
    
    /**
     * Serial Counting Sort.
     *
     * @param array the array to sort
     */
    public void serialSort(int[] array) {
        int[] temp = array.clone();
        
        // Get max value of array to determine counts size
        int max = Integer.MIN_VALUE;
        for(int x : temp) {
            if(x > max) {
                max = x;
            }
        }
        
        int[] counts = new int[max + 1];
        for(int t : temp) {
            counts[t]++;
        }
        
        /*
        Update the Count[] so that each index will store the sum till previous step. (Count[i]=Count[i] + Count[i-1]).
        Now updated Count[] array will reflect the actual position of each integer in Result[].
         */
        for(int i = 1; i <= max; i++) {
            counts[i] += counts[i - 1];
        }
        
        /*
        Now navigate the input array taking one element at a time
        Count[input[i]] will tell you the index position of input[i] in Result[].
        When you do that, decrease the count in Count[input[i]] by 1.
         */
        int[] result = new int[temp.length + 1];
        for(int x : temp) {
            result[counts[x]] = x;
            counts[x]--;
        }
    }
}
