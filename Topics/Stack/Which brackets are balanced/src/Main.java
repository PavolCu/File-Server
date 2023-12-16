import java.util.Scanner;
import java.util.Stack;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scanner = new Scanner(System.in);

        String brackets = new String(scanner.nextLine());


        System.out.println(isBalanced(brackets));

    }

    public static boolean isBalanced(String brackets) {
        // write your code here
        Stack<Character> stack = new Stack<>();

        for (char c : brackets.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (stack.isEmpty()) {
                return false;
            } else if (c == ')' && stack.pop() != '(') {
                return false;
            } else if (c == ']' && stack.pop() != '[') {
                return false;
            } else if (c == '}' && stack.pop() != '{') {
                return false;
            }
        }
        return stack.isEmpty();
    }
}