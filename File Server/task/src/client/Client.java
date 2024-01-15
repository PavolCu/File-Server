package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private String address;
    private int port;
    private Socket clientSocket;


    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() throws SocketException {
        try {
            clientSocket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            //System.out.println("Client started!");

            String message = "Give me everything you have!";
            output.writeUTF(message);
            System.out.println("Sent: " + message);

            String received = (input.readUTF());
            System.out.println("Received: " + received);
        } catch (ConnectException e) {
            System.out.println("Failed to connect to the server: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request, String fileContent) throws SocketException {
        if (clientSocket == null || clientSocket.isClosed()) {
            throw new SocketException("Client socket is not been initialized.");
        }
        try {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            output.writeUTF(request + " " + fileContent);
            output.flush();
            return input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stop() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendExitCommand() throws SocketException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            sendRequest("exit", " ");
            stop();
        } else {
            throw new SocketException("Client socket has not been initialized.");
        }
    }
}