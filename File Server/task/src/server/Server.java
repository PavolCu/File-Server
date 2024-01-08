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
    private Socket socket;
    private boolean running;

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
                socket = serverSocket.accept();
                try (DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    String receivedMessage = input.readUTF();
                    System.out.println("Received: " + receivedMessage);

                    if ("exit".equals(receivedMessage)) {
                        stop();
                        break;
                    }

                    String responseMessage = handleRequest(receivedMessage);
                    sendResponse(output, responseMessage);  // Call sendResponse here
                    System.out.println("Sent: " + responseMessage);
                }
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String handleRequest(String request) throws IOException {
        String[] parts = request.split(" ", 3);
        String command = parts[0];

        String fileName = parts[1];
        Path filePath = Paths.get(System.getProperty("cuninkapavol") + "/File Server/File Server/task/src/server/data/" + fileName);

        switch (command) {
            case "GET":
                if (Files.exists(filePath)) {
                    return "200 " + new String(Files.readAllBytes(filePath));
                } else {
                    return "404";
                }
            case "PUT":
                Files.write(filePath, parts[2].getBytes());
                return "200";
            case "DELETE":
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    return "200";
                } else {
                    return "404";
                }
            case "exit":
                stop();
                return "200";
            default:
                return "400";
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

    public Socket getSocket() {
        return socket;
    }
}