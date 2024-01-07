package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private String address;
    private int port;
    private ServerSocket server;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    public Server(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try {
            server = new ServerSocket(port, 50, InetAddress.getByName(address));
            System.out.println("Server started!");

            socket = server.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            String receivedMessage = input.readUTF();
            System.out.println("Received: " + receivedMessage);
            String responseMessage = "All files were sent!";
            output.writeUTF(responseMessage);
            System.out.println("Sent: " + responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendResponse(String response) {
        try {
            output.writeUTF(response);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}