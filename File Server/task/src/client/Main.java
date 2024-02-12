package client;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String DATA_DIR = "/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/client/Data/";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            boolean isRunning = true;
            while (isRunning) {
                System.out.println("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
                String actionInput = scanner.nextLine();
                String action = "";

                if (actionInput.equalsIgnoreCase("exit")) {
                    output.writeUTF("exit");
                    socket.close();
                    System.out.println("The request was sent.");
                    break;
                } else {
                    try {
                        int actionNumber = Integer.parseInt(actionInput);
                        switch (actionNumber) {
                            case 1 -> action = "GET";
                            case 2 -> action = "PUT";
                            case 3 -> action = "DELETE";
                            default -> System.out.println("Invalid action. Please enter 1, 2,3 or exit.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter 1, 2, 3 or 'exit'.");
                        continue;
                    }
                }

                String identifierType = "";
                String identifier = "";
                if (action.equals("GET") || action.equals("DELETE")) {
                    System.out.println("Do you want to " + action.toLowerCase() + " the file by name or by id (1 - name, 2 - id): ");
                    String identifierTypeInput = scanner.nextLine();
                    if ("1".equals(identifierTypeInput)) {
                        identifierType = "BY_NAME";
                    } else if ("2".equals(identifierTypeInput)) {
                        identifierType = "BY_ID";
                    } else {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                        continue;
                    }

                    System.out.println("Enter " + (identifierType.equals("BY_NAME") ? "name" : "id") + ": ");
                    identifier = scanner.nextLine();
                }

                String fileName;
                byte[] fileContent = new byte[0];
                if (action.equals("PUT")) {
                    System.out.println("Enter name of the file: ");
                    fileName = scanner.nextLine();
                    Path filePath = Paths.get(DATA_DIR + fileName);
                    try {
                        Files.createFile(filePath);
                    } catch (FileAlreadyExistsException e) {
                        System.out.println("File " + fileName + " already exists. Overwriting.");
                    }
                    fileContent = Files.readAllBytes(filePath);
                }

                String requestToServer = action + " " + identifierType + " " + identifier;
                output.writeUTF(requestToServer);
                if (action.equals("PUT")) {
                    output.writeInt(fileContent.length);
                    output.write(fileContent);
                }

                switch (action) {
                    case "PUT" -> {
                        String responseFromServer = input.readUTF();
                        if (responseFromServer.startsWith("200 ")) {
                            System.out.println("The request was sent.");
                            System.out.println("Response says that file is saved! ID = " + responseFromServer.substring(4));
                        } else {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that creating the file was forbidden!");
                        }
                        isRunning = false;
                    }
                    case "GET" -> {
                        int length = input.readInt();
                        byte[] message = new byte[length];
                        input.readFully(message, 0, message.length);
                        System.out.println("The request was sent.");
                        System.out.println("The file was downloaded! Specify a name for it: ");
                        String saveFileName = scanner.nextLine();
                        Files.write(Paths.get(DATA_DIR + saveFileName), message);
                        System.out.println("File saved on the hard drive!");
                        isRunning = false;
                    }
                    case "DELETE" -> {
                        String responseFromServer = input.readUTF();
                        if (responseFromServer.equals("200")) {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that this file was deleted successfully!");
                        } else {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that this file is not found!");
                        }
                        isRunning = false;
                    }
                }
            }
        }
    }
}