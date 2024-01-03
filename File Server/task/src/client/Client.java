package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Client started!");

            String message = "Give me everything you have";
            output.writeUTF(message);
            System.out.println("Sent: " + message);

            String received = (input.readUTF());
            System.out.println("Received: " + received);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
