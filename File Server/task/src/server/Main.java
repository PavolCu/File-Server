package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final ConcurrentHashMap<Integer, String> fileMap = new ConcurrentHashMap<>();
    private static final AtomicInteger currentId = new AtomicInteger(0);

    public static void main(String[] args) {

        // Load the file map and current id from a file
        loadFileMap();

        System.out.println("Server started!");

        try (
                ServerSocket serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS));
        ) {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> handleClientRequest(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the file map and current id to a file
        saveFileMap();
    }


    private static void handleClientRequest(Socket socket) {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            String requestFromClient = input.readUTF();
            String[] requestParts = requestFromClient.split(" ", 4);
            String action = requestParts[0];
            String identifierType = "";
            String identifier = "";
            if (requestParts.length > 1) {
                identifierType = requestParts[1];
                identifier = requestParts[2];
            }
            String fileContent = "";
            if (requestParts.length > 3) {
                fileContent = requestParts[3];
            }

            while (true) {
                String clientRequest = input.readUTF();
                if (clientRequest.equals("exit")) {
                    break;
                }
                else {
                    continue;
                }
            }

            String fileName = identifierType.equals("BY_ID") ? fileMap.get(Integer.parseInt(identifier)) : identifier;
            Path filePath = Paths.get("/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/Data/" + fileName);
            if (action.equals("PUT")) {
                if (!Files.exists(filePath)) {
                    try {
                        Files.createDirectories(filePath.getParent()); // Create directories if they do not exist
                        Files.write(filePath, fileContent.getBytes());
                        int id = currentId.incrementAndGet();
                        fileMap.put(id, fileName);
                        output.writeUTF("200 " + id);
                    } catch (IOException e) {
                        output.writeUTF("403");
                    }
                } else {
                    output.writeUTF("403");
                }
            } else if (action.equals("GET")) {
                if (Files.exists(filePath)) {
                    byte[] content = Files.readAllBytes(filePath);
                    output.writeInt(content.length);
                    output.write(content);
                } else {
                    output.writeUTF("404");
                }
            } else if (action.equals("DELETE")) {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    fileMap.remove(Integer.parseInt(identifier));
                    output.writeUTF("200");
                } else {
                    output.writeUTF("404");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadFileMap() {
        File file = new File("fileMap.ser");
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    oos.writeObject(new ConcurrentHashMap<Integer, String>());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            ConcurrentHashMap<Integer, String> loadedMap = (ConcurrentHashMap<Integer, String>) ois.readObject();
            fileMap.clear();
            fileMap.putAll(loadedMap);
            currentId.set(fileMap.size()); // Use set method to change the value
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