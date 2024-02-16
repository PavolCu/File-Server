package client;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Client started!");

            while (true) {
                System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file, 4 - exit): ");
                String actionInput = scanner.nextLine();
                String action = "";

                if (actionInput.equalsIgnoreCase("exit")) {
                    action = "exit";
                    output.writeUTF(action);
                    break;
                } else {
                    try {
                        int actionNumber = Integer.parseInt(actionInput);
                        switch (actionNumber) {
                            case 1 -> action = "GET";
                            case 2 -> action = "PUT";
                            case 3 -> action = "DELETE";
                            default -> {
                                System.out.println("Invalid action. Please enter 1, 2, 3 or 'exit'.");
                                continue;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter 1, 2, 3 or 'exit'.");
                        continue;
                    }
                }

                String identifierType = "";
                String identifier = "";
                String fileContent = "";

                if (action.equals("PUT")) {
                    System.out.println("Enter filename:");
                    identifier = scanner.nextLine();
                    System.out.println("Enter name of the file to be saved on server:");
                    String serverFileName = scanner.nextLine();
                    Path filePath = Paths.get("../client/data/" + identifier);
                    if (!Files.exists(filePath)) {
                        System.out.println("File " + identifier + " does not exist. Creating a new file.");
                        Files.createFile(filePath);
                    }
                    fileContent = new String(Files.readAllBytes(filePath));
                    String requestToServer = action + " " + serverFileName + " " + fileContent;
                    output.writeUTF(requestToServer);
                }

                String requestToServer = action + " " + identifierType + " " + identifier + " " + fileContent;
                output.writeUTF(requestToServer);

                if (action.equals("PUT")) {
                    String responseFromServer = input.readUTF();
                    if (responseFromServer.startsWith("200 ")) {
                        System.out.println("The request was sent.");
                        System.out.println("The response says that the file was created! The id is: " + responseFromServer.substring(4));
                    } else {
                        System.out.println("The request was sent.");
                        System.out.println("The response says that creating the file was forbidden!");
                    }
                } else if (action.equals("GET")) {
                    int length = input.readInt();
                    byte[] message = new byte[length];
                    input.readFully(message, 0, message.length);
                    System.out.println("The request was sent.");
                    System.out.println("The content of the file is: " + new String(message));
                } else if (action.equals("DELETE")) {
                    String responseFromServer = input.readUTF();
                    if (responseFromServer.equals("200")) {
                        System.out.println("The request was sent.");
                        System.out.println("The response says that the file was successfully deleted!");
                    } else {
                        System.out.println("The request was sent.");
                        System.out.println("The response says that the file was not found!");
                    }
                }

                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}