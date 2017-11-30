import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TaskQuick extends SortTask {
  private final int SPLIT_THRESHOLD = 100000;
  private int sortArray [];
  private int iStart = 0;
  private int iEnd  =0;
  ExecutorService threadPool;
  List futureList;
 
  public TaskQuick(ExecutorService threadPool, List futureList, int[] inList,int start, int end){
    this.sortArray = inList;
    this.iStart = start;
    this.iEnd   = end;
    this.threadPool =threadPool;
    this.futureList =futureList;
  }
 
  @Override
  public boolean isReadyToProcess() {
    return true;
  }
 
  public void run() {
    sort(sortArray, iStart, iEnd) ;
  }
 
  private   void sort(final int inList[], int start, int end) {
    int pivot = inList[start]; // consider this as  hole at inList[start],
    int leftPointer = start;
    int rightPointer = end;
    final int LEFT = 1;
    final int RIGHT = -1;
    int pointerSide = RIGHT; //  we start with right as pivot is from left
 
    while (leftPointer != rightPointer) {
      if (pointerSide == RIGHT) {
        if (inList[rightPointer] < pivot) {           inList[leftPointer] = inList[rightPointer];           leftPointer++;           pointerSide = LEFT;         } else {           rightPointer--;         }       } else if (pointerSide == LEFT) {         if (inList[leftPointer] > pivot) {
          inList[rightPointer] = inList[leftPointer];
          rightPointer--;
          pointerSide = RIGHT;
        } else {
          leftPointer++;
        }
      }
    }
 
    //put the pivot where leftPointer and rightPointer collide i.e. leftPointer == rightPointer
    inList[leftPointer]=pivot;
 
    if((leftPointer - start) > 1){
      if ((leftPointer - start) > SPLIT_THRESHOLD){
        futureList.add(threadPool.submit(new TaskQuick(threadPool,futureList,inList, start, leftPointer-1)));
      }else {
        sort(inList, start, leftPointer-1);
      }
    } 
    if((end - leftPointer) > 1){
      if ((end - leftPointer) > SPLIT_THRESHOLD ){
        futureList.add(threadPool.submit(new TaskQuick(threadPool,futureList,inList, leftPointer+1, end)));
      }  else {
        sort(inList, leftPointer+1, end);
      }
    } 
  } 
}
