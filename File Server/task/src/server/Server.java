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

    public  Server(String adress, int port) {
        this.address = adress;
        this.port = port;
    }

    public void start() {
        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName(address))) {
            System.out.println("Server started!");

            try (Socket socket = server.accept();
                 DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                String receivedMessage = input.readUTF();
                System.out.println("Received: " + receivedMessage);
                String responseMessage = "All files were sent!";
                output.writeUTF(responseMessage);
                System.out.println("Sent: " + responseMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
