import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();

        Stack<Integer> mainStack = new Stack<>();
        Stack<Integer> maxStack = new Stack<>();

        for (int i = 0; i < n; i++) {
            String command = scanner.nextLine();
            if (command.startsWith("push")) {
                int value = Integer.parseInt(command.split(" ")[1]);
                push(mainStack, maxStack, value);
            } else if (command.equals("pop")) {
                pop(mainStack, maxStack);
            } else if (command.equals("max")) {
                System.out.println(maxStack.peek());
            }
        }
    }

    private static void push(Stack<Integer> mainStack, Stack<Integer> maxStack, int value) {
        mainStack.push(value);
        if (maxStack.isEmpty() || value >= maxStack.peek()) {
            maxStack.push(value);
        }
    }

    private static void pop(Stack<Integer> mainStack, Stack<Integer> maxStack) {
        if (!mainStack.isEmpty()) {
            int poppedValue = mainStack.pop();
            if (!maxStack.isEmpty() && poppedValue == maxStack.peek()) {
                maxStack.pop();
            }
        }
    }
}
