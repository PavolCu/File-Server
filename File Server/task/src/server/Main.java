package server;

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

        while (true) {
            String[] input = scanner.nextLine().split(" ", 3);
            String command = input[0];
            String fileName = input.length > 1 ? input[1] : "";
            String fileContent = input.length > 2 ? input[2] : "";
            Path filePath = Paths.get("./server/data/" + fileName);

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
                    if (Files.exists(filePath)) {
                        server.sendResponse("403");
                    } else {
                        try {
                            Files.write(filePath, fileContent.getBytes());
                            server.sendResponse("200");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "GET":
                    if (Files.exists(filePath)) {
                        try {
                            String content = new String(Files.readAllBytes(filePath));
                            server.sendResponse("200 " + content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        server.sendResponse("404");
                    }
                    break;
                case "DELETE":
                    if (Files.exists(filePath)) {
                        try {
                            Files.delete(filePath);
                            server.sendResponse("200");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        server.sendResponse("404");
                    }
                    break;
                case "exit":
                    server.stop();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }
}