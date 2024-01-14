package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private volatile boolean running;
    private ServerLogic serverLogic;  // Add this line

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverLogic = new ServerLogic("127.0.0.1", port );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        running = true;
        new Thread(this::runServer).start();
    }

    private void runServer() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            while (true) {
                String receivedMessage = input.readUTF();
                System.out.println("Received: " + receivedMessage);

                if ("exit".equals(receivedMessage)) {
                    System.out.println("Exit command received, shutting down server...");
                    stop();
                    break;
                }

                serverLogic.handleRequest(receivedMessage, output);  // Call handleRequest here
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}