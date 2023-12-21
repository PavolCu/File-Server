import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < size; ++i) {
            int number = scanner.nextInt();
            if (number % 2 == 0) {
                deque.offerFirst(number); // Add even numbers to the beginning of the deque
            } else {
                deque.offerLast(number);  // Add odd numbers to the end of the deque
            }
        }
        for (int num : deque) {
            System.out.println(num);

        }
    }
}