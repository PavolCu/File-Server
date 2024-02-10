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

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    public static void main(String[] args) {

        System.out.println("Server started!");

        try (
                ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS));
        )
        {
            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                )
                {
                    String requestFromClient = input.readUTF();
                    String[] requestParts = requestFromClient.split(" ", 3);
                    String action = requestParts[0];
                    String fileName = requestParts[1];
                    String fileContent = requestParts[2];

                    /*if (requestParts.length > 1) {
                        fileName = requestParts[1];
                        if (requestParts.length > 2) {
                            fileContent = requestParts[2];
                        }
                    }*/

                    if (action.equals("exit")) {
                        break;
                    }
                    //String fileName = requestParts.length > 1 ? requestParts[1] : "";
                   // String fileContent = requestParts.length > 2 ? requestParts[2] : "";

                    Path filePath = Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/Data/" + fileName);
                    if (action.equals("PUT")) {
                        if (Files.exists(filePath)) {
                            output.writeUTF("403");
                        } else {
                            try {
                                Files.write(filePath, fileContent.getBytes());
                                output.writeUTF("200");
                            } catch (IOException e) {
                                output.writeUTF("403");
                            }
                        }
                    } else if (action.equals("GET")) {
                        if (Files.exists(filePath)) {
                            String content = new String(Files.readAllBytes(filePath));
                            output.writeUTF("200 " + content);
                        } else {
                            output.writeUTF("404");
                        }
                    } else if (action.equals("DELETE")) {
                        if (Files.exists(filePath)) {
                            Files.delete(filePath);
                            output.writeUTF("200");
                        } else {
                            output.writeUTF("404");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}