package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        )
        {
            System.out.println("Client started!");
            String wordsToServer = "Give me everything you have!";
            output.writeUTF(wordsToServer);
            System.out.println("Sent: " + wordsToServer);
            String wordsFromServer = input.readUTF();
            System.out.println("Received: " + wordsFromServer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}