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

public class Server {
    private String address;
    private int port;
    private ServerSocket serverSocket;
    private volatile boolean running;

    public Server(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        running = true;
        try {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(address));
            System.out.println("Server started!");
            listen();
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

    private void handleRequest(String request, DataOutputStream output) throws IOException {
        String[] parts = request.split(" ", 3);
        String command = parts[0];

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
            case "exit":
                stop();
                sendResponse(output, "200");
                running = false;
                break;
            default:
                sendResponse(output, "400");
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


}