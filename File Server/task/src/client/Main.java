package client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 23456);
        client.start();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter action:");
        String action = scanner.nextLine();

        System.out.println("Enter filename:");
        String filename = scanner.nextLine();
    }
}
