package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private String address;
    private int port;
    private Socket clientSocket;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try {
            clientSocket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Client started!");

            String message = "Give me everything you have!";
            output.writeUTF(message);
            System.out.println("Sent: " + message);

            String received = (input.readUTF());
            System.out.println("Received: " + received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendRequest(String request) {
        try {
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            output.writeUTF(request);
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

    public void sendExitCommand() {
        sendRequest("exit");
        stop();
    }
}