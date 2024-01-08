package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Server server = new Server("127.0.0.1",23456);
        server.start();
        System.out.println("Server started!");
        Set<String> files = new HashSet<>();
        Scanner scanner = new Scanner(System.in);

        String path = System.getProperty("user.dir") + "/File Server/task/src/server/data/";

        while (true) {
            String[] input = scanner.nextLine().split(" ", 3);
            String command = input[0];

            if ("exit".equals(command)) {
                server.stop();
                scanner.close();
                return;
            }
            String fileName = input.length > 1 ? input[1] : "";
            String fileContent = input.length > 2 ? input[2] : "";
            Path filePath = Paths.get(path + fileName);

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
                case "PUT":
                    System.out.println(("received PUT request: " + input[1] + " " + input[2]));
                    if (Files.exists(filePath)) {
                        try {
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "403");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Files.write(filePath, fileContent.getBytes());
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "200" + fileContent);
                            System.out.println("File" + fileName + "created succesfully");
                        } catch (IOException e) {
                            System.out.println("Error while creating file: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                    break;
                case "GET":
                    if (Files.exists(filePath)) {
                        try {
                            String content = new String(Files.readAllBytes(filePath));
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "200 " + content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "404");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "DELETE":
                    if (Files.exists(filePath)) {
                        try {
                            Files.delete(filePath);
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "200");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            DataOutputStream output = new DataOutputStream(server.getSocket().getOutputStream());
                            server.sendResponse(output, "404");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }
}