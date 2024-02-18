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

                String identifierType;
                String identifier;
                //String fileContent;

                // Client side
                // Client side
                if (action.equals("PUT")) {
                    System.out.println("Enter filename:");
                    String localFileName = scanner.nextLine();
                    System.out.println("Enter name of the file to be saved on server (press Enter for a unique name):");
                    String serverFileName = scanner.nextLine();
                    identifier = serverFileName; // Assign the serverFileName to identifier
                    Path filePath = Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/client/Data/" + localFileName);
                    if (!Files.exists(filePath)) {
                        System.out.println("File " + localFileName + " does not exist. Creating a new file.");
                        Files.createFile(filePath);
                    } else {
                        System.out.println("File " + localFileName + " already exists.");
                    }
                    byte[] fileContent = Files.readAllBytes(filePath);
                    output.writeInt(fileContent.length); // send length of the file
                    output.write(fileContent); // send the file content
                    // Handle server response...
                }

                if (action.equals("GET")) {
                    System.out.println("Do you want to use the id or the name of the file? (Enter 'id' or 'name')");
                    identifierType = scanner.nextLine();
                    System.out.println("Enter the " + identifierType + " of the file:");
                    identifier = scanner.nextLine();
                    System.out.println("Enter the name under which the file should be saved:");
                    String saveFileName = scanner.nextLine();
                    String requestToServer = action + " " + identifierType.toUpperCase() + " " + identifier;
                    output.writeUTF(requestToServer);
                    // Handle server response...
                    int length = input.readInt(); // read length of incoming message
                    byte[] message = new byte[length];
                    input.readFully(message, 0, message.length); // read the message
                    Files.write(Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/client/Data/" + saveFileName), message);
                }
                switch (action) {
                    case "PUT" -> {
                        String responseFromServer = input.readUTF();
                        if (responseFromServer.startsWith("200 ")) {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that the file was created! The id is: " + responseFromServer.substring(4));
                        } else {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that creating the file was forbidden!");
                        }
                    }
                    case "GET" -> {
                        int length = input.readInt();
                        byte[] message = new byte[length];
                        input.readFully(message, 0, message.length);
                        System.out.println("The request was sent.");
                        System.out.println("The content of the file is: " + new String(message));
                    }
                    case "DELETE" -> {
                        String responseFromServer = input.readUTF();
                        if (responseFromServer.equals("200")) {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that the file was successfully deleted!");
                        } else {
                            System.out.println("The request was sent.");
                            System.out.println("The response says that the file was not found!");
                        }
                    }
                }

                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}