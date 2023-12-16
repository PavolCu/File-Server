import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Utils {

    public static List<Integer> sortOddEven(List<Integer> numbers) {
        Collections.sort(numbers, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                if (a % 2 == 0 && b % 2 == 0) {
                    return Integer.compare(b, a);
                } else if (a % 2 != 0 && b % 2 != 0) {
                    return Integer.compare(a, b);
                } else {
                    return a % 2 == 0 ? 1 : -1;
                }
            }
        });
        return numbers;
    }
}