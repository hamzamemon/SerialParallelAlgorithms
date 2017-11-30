import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedQuick {

    public static void parallelQuickSort(int[] array){
        int processes = Driver.PROCESSES;
        int [] sortArray = array.clone();
        
        final ExecutorService executor = Executors.newFixedThreadPool(processes);
        List futures = new Vector();
        TaskQuick rootTask = new TaskQuick (executor,futures,sortArray,0,sortArray.length-1)  ;

        futures.add(executor.submit(rootTask));
        while(!futures.isEmpty()){
            //     System.out.println("Future size " +futures.size());
            Future topFeature = (Future)futures.remove(0);
            try{
                if(topFeature!=null)topFeature.get();
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }catch(ExecutionException ie){
                ie.printStackTrace();
            }
        }
        executor.shutdown();
    }

}
