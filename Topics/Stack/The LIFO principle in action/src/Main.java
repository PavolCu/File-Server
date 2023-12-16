import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;

class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(); // Read the number of elements
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            int element = scanner.nextInt(); // Read each element
            stack.push(element); // Push elements onto the stack
        }

        while (!stack.isEmpty()) {
            int element = stack.pop(); // Pop elements from the stack (LIFO)
            System.out.println(element); // Output elements in reverse order
        }
    }
}