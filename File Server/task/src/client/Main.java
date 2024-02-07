package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
        )
        {
            System.out.println("Client started!");

            System.out.println("Enter action (1 - get a file, 2 - create a file, 3 - delete a file): ");
            String action = scanner.nextLine();

            String fileName = "";
            String fileContent = "";

            if (action.equals("exit")) {
                System.out.println("The request was sent.");
                output.writeUTF(action);
                socket.close();
                System.out.println("Disconnected from the server and terminated.");
                return;
            }

            System.out.println("Enter file name:");
            fileName = scanner.nextLine();
            if (action.equals("2")) {
                System.out.println("Enter the content of the file:");
                fileContent = scanner.nextLine();
            }


            String requestToServer = action + " " + fileName + " " + fileContent;
            output.writeUTF(requestToServer);

            String responseFromServer = input.readUTF();
            if (action.equals("2")) {
                if (responseFromServer.equals("200")) {
                    System.out.println("The response says that the file was created!");
                } else {
                    System.out.println("The response says that creating the file was forbidden!");
                }
            } else if (action.equals("1")) {
                if (responseFromServer.startsWith("200 ")) {
                    System.out.println("The content of the file is: " + responseFromServer.substring(4));
                } else {
                    System.out.println("The response says that the file was not found!");
                }
            } else if (action.equals("3")) {
                if (responseFromServer.equals("200")) {
                    System.out.println("The response says that the file was successfully deleted!");
                } else {
                    System.out.println("The response says that the file was not found!");
                }
            }
            socket.close();
            System.out.println("Disconnected from the server and terminated.");



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}