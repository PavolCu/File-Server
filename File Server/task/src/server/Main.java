package server;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        //Create a  new server and start it
        Server server = new Server("127.0.0.1",23456);
        server.start();
        //create a set to store the file names
        Set<String> files = new HashSet<>();
        //Create a scanner to read the user input
        Scanner scanner = new Scanner(System.in);

        label:
        while (true) {
            //Split the input into command, file name and file content
            String[] input = scanner.nextLine().split(" ", 3);
            String command = input[0];
            String fileName = input.length > 1 ? input[1] : "";
            String fileContent = input.length > 2 ? input[2] : "";
            //create a path for the file
            Path filePath = Paths.get("./server/data/" + fileName);

            switch (command) {
                // Add fa file to the set
                case "add":
                    if (fileName.matches("file[1-9]|file10") && files.add(fileName)) {
                        System.out.println("The file " + fileName + " added successfully");
                    } else {
                        System.out.println("Cannot add the file " + fileName);
                    }
                    break;
                case "get":
                    // Get the file from the set
                    if (files.contains(fileName)) {
                        System.out.println("The file " + fileName + " was sent");
                    } else {
                        System.out.println("The file " + fileName + " not found");
                    }
                    break;
                case "delete":
                    // Delete the file from the set
                    if (files.remove(fileName)) {
                        System.out.println("The file " + fileName + " was deleted");
                    } else {
                        System.out.println("The file " + fileName + " not found");
                    }
                    break;
                case "PUT":
                    // Create a new file or return an error if the file already exists
                    if (File.existrs(filePath)) {
                        server.sendResponse("404");
                    }else {
                        try {
                            Files.write((filePath, fileContent.getBytes());
                            server.sendResponse("200");
                        }catch (IOException e){
                            e.printStackTrace();
                        }
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