package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.*;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final ConcurrentHashMap<Integer, String> fileMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        // Load the file map from a file
        loadFileMap();

        System.out.println("Server started!");

        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> handleClientRequest(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the file map to a file
        saveFileMap();
    }

    private static void handleClientRequest(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String action = input.readUTF();
            String fileName = input.readUTF();
            Path filePath = Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/Data/" + fileName);

            switch (action) {
                case "PUT":
                    if (fileName.isEmpty()) {
                        fileName = UUID.randomUUID().toString();
                    }
                    byte[] content = new byte[input.readInt()];
                    input.readFully(content);
                    Files.write(filePath, content);
                    int id = fileMap.size() + 1;
                    fileMap.put(id, fileName);
                    output.writeUTF("200 " + id);
                    break;
                case "GET":
                    if (Files.exists(filePath)) {
                        byte[] fileContent = Files.readAllBytes(filePath);
                        output.writeInt(fileContent.length);
                        output.write(fileContent);
                    } else {
                        output.writeUTF("404");
                    }
                    break;
                case "DELETE":
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        fileMap.values().remove(fileName);
                        output.writeUTF("200");
                    } else {
                        output.writeUTF("404");
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFileMap() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("fileMap.ser"))) {
            ConcurrentHashMap<Integer, String> loadedMap = (ConcurrentHashMap<Integer, String>) ois.readObject();
            fileMap.clear();
            fileMap.putAll(loadedMap);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void saveFileMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("fileMap.ser"))) {
            oos.writeObject(fileMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}