package client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 23456);
        client.start();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file):");
            String action = scanner.nextLine();

            if ("exit".equals(action)) {
                client.sendExitCommand();
                scanner.close();
                return;
            }

            System.out.println("Enter filename:");
            String fileName = scanner.nextLine();

            String fileContent = "";
            if ("2".equals(action)) {
                System.out.println("Enter file content:");
                fileContent = scanner.nextLine();
            }

            String request;
            String response; // Declare 'response' here

            switch (action) {
                case "1":
                    request = "GET " + fileName;
                    System.out.println("The request was sent.");
                    response = client.sendRequest(request); // Now 'response' is visible here
                    if (response.startsWith("200")) {
                        System.out.println("The content of the file is: " + response.substring(4));
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    continue;
                case "2":
                    request = "PUT " + fileName + " " + fileContent;
                    System.out.println("The request was sent.");
                    response = client.sendRequest(request);
                    if (response == null) {
                        System.out.println("Failed to get a response from the server.");
                    } else if (response.startsWith("200")) {
                        System.out.println("The response says that the file was created!");
                    } else {
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                    continue;
                case "3":
                    request = "DELETE " + fileName;
                    System.out.println("The request was sent.");
                    response = client.sendRequest(request); // And here as well
                    if (response.equals("200")) {
                        System.out.println("The response says that the file was successfully deleted!");
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    continue;
                    case "exit":
                        client.sendExitCommand();
                        scanner.close();
                        return;
                default:
                    break;
            }


            scanner.close();
        }
    }
}