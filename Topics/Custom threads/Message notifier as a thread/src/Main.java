import java.util.Scanner;

class MessageNotifier extends Thread {
    final private String msg;
    final private int repeats;


    //Constructor that takes a message and the number of repetitions
    public MessageNotifier(String msg, int repeats) {
        super();
        this.msg = msg;
        this.repeats = repeats;
    }

    @Override
    public void run() {
        // Use StringBuilder for efficient string concatenation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeats; i++) {
            sb.append(msg).append("\n");
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the message:");
        String msg = scanner.nextLine();

        System.out.println("Enter the number of repetitions:");
        int repeats = scanner.nextInt();

        MessageNotifier messageNotifier = new MessageNotifier(msg, repeats);
        messageNotifier.start();
    }
}