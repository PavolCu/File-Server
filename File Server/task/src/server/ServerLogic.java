package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ServerLogic {
    private String address;
    private int port;
    private ServerSocket serverSocket;
    private volatile boolean running;


    public ServerLogic(String address, int port) {
        this.address = address;
        this.port = port;
    }


    public void start() {
        running = true;
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
            //System.out.println("Server started!");
            listen();
            runServerLogic(new Scanner(System.in));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void listen() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                if (!running) {
                    break;
                }
                new Thread(() -> handleClient(socket)).start();
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleClient(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            while (true) {
                String receivedMessage = input.readUTF();
                System.out.println("Received: " + receivedMessage);

                if ("exit".equals(receivedMessage)) {
                    System.out.println("Exit command received, shutting down server...");
                    socket.close();
                    serverSocket.close();
                    System.exit(0);
                }

                handleRequest(receivedMessage, output);  // Call handleRequest here
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRequest(String request, DataOutputStream output) throws IOException {
        String[] parts = request.split(" ", 3);
        String command = parts[0];

        if ("exit".equals(command)) {
            sendResponse(output, "200");
            stop();
            System.exit(0);
        } else {
            String fileName = parts[1];
            Path filePath = Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/data/" + fileName);

            switch (command) {
                case "GET":
                    if (Files.exists(filePath)) {
                        sendResponse(output, "200 " + new String(Files.readAllBytes(filePath)));
                    } else {
                        sendResponse(output, "404");
                    }
                    break;
                case "PUT":
                    Files.write(filePath, parts[2].getBytes());
                    sendResponse(output, "200");
                    break;
                case "DELETE":
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        sendResponse(output, "200");
                    } else {
                        sendResponse(output, "404");
                    }
                    break;
                default:
                    sendResponse(output, "400");
            }
        }
    }

    public void sendResponse(DataOutputStream output, String response) throws IOException {
        output.writeUTF(response);
        output.flush();
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runServerLogic(Scanner scanner) {
        Set<String> files = new HashSet<>();
        String path = System.getProperty("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/data/");

        while (true) {
            String[] input = scanner.nextLine().split(" ", 3);
            String command = input[0];

            if ("exit".equals(command)) {
                stop();
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
                        System.out.println("403");
                    } else {
                        try {
                            Files.write(filePath, fileContent.getBytes());
                            System.out.println("200" + fileContent);
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
                            System.out.println("200 " + content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("404");
                    }
                    break;
                case "DELETE":
                    if (Files.exists(filePath)) {
                        try {
                            Files.delete(filePath);
                            System.out.println("200");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("404");
                    }
                    break;
                default:
                    System.out.println("Command not recognized");
                    break;
            }
        }
    }
}