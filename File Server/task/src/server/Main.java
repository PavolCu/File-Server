package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<String> files = new HashSet<>();
        Scanner scanner = new Scanner(System.in);

        label:
        while (true) {
            String[] input = scanner.nextLine().split(" ", 2);
            String command = input[0];
            String fileName = input.length > 1 ? input[1] : "";

            switch (command) {
                case "add":
                    if (fileName.matches("file[1-9]|file10") && files.add(fileName)) {
                        System.out.println("The file " + fileName + " added successfully");
                    } else {
                        System.out.println("Cannot add the file " + fileName);
                    }
                    break;
                case "get":
                    if (files.contains(fileName)) {
                        System.out.println("The file " + fileName + " was sent");
                    } else {
                        System.out.println("The file " + fileName + " not found");
                    }
                    break;
                case "delete":
                    if (files.remove(fileName)) {
                        System.out.println("The file " + fileName + " was deleted");
                    } else {
                        System.out.println("The file " + fileName + " not found");
                    }
                    break;
                case "exit":
                    break label;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
        scanner.close();
    }
}