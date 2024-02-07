package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        System.out.println("Server started!");
        try (
                ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS));
                Socket socket = serverSocket.accept();
        )
        {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            String wordsFromClient = input.readUTF();
            System.out.println("Received: " + wordsFromClient);
            String wordsToClient = "All files were sent!";
            output.writeUTF(wordsToClient);
            System.out.println("Sent: " + wordsToClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}