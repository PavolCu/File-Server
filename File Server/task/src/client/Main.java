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
        String fileName = scanner.nextLine();

        String fileContent = "";
        if (action.equals("PUT")) {
            System.out.println("Enter file content:");
            fileContent = scanner.nextLine();
        }

        String response = client.sendRequest(action + " " + fileName + " " + fileContent);

        switch (action) {
            case "PUT":
                if (response.equals("200")) {
                    System.out.println("The response says that the file was created!");
                } else {
                    System.out.println("The response says that creating the file was forbidden!");
                }
                break;
                case "GET":
                    if (response.equals("200")) {
                        System.out.println("The content of the file is: " + response.substring(4));
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;
                case "DELETE":
                    if (response.equals("200")) {
                        System.out.println("The response says that the file was successfully deleted!");
                    } else {
                        System.out.println("The response says that the file was not found!");
                    }
                    break;
                default:
                    System.out.println("Commmand not recognized");
                    break;
        }

        client.stop();
        scanner.close();
    }
}
