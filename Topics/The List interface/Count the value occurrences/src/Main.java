import java.util.Collections;
import java.util.List;

class Counter {

    public static boolean checkTheSameNumberOfTimes(int elem, List<Integer> list1, List<Integer> list2) {
        // implement the method
        int count1 = Collections.frequency(list1, elem);
        int count2 = Collections.frequency(list2, elem);
        return count1 == count2;
        
    }
}