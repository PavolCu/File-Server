import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Object msg = scanner.nextLine();    

        // Write your code jere
        if (msg instanceof String) {
            String str = (String) msg;

            if (str.length() > 0) {
                System.out.println("The variable is not empty, the length is " + str.length());
            } else {
                System.out.println("Empty string");
            }
        }
    }
}