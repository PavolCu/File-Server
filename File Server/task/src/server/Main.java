package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String DATA_DIR = "/Users/cuninkapavol/IdeaProjects/File Server/File Server/task/src/server/Data/";
    private static final String ID_MAP_FILE = DATA_DIR + "id_map.ser";
    private static Map<Integer, String> idMap = new ConcurrentHashMap<>();
    private static AtomicInteger nextId = new AtomicInteger(1);
    private static volatile boolean isRunning = true;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        System.out.println("Server started!");

        // Load the id map from disk
        loadIdMap();

        try {
            serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS));
            ExecutorService executor = Executors.newFixedThreadPool(10);
            while (isRunning && !serverSocket.isClosed() && !Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                executor.submit(() -> handleClient(socket));
            }
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String requestFromClient = input.readUTF();
            if ("exit".equals(requestFromClient)) {
                isRunning = false;
                serverSocket.close();
                System.exit(0);
                return;
            }

            String[] requestParts = requestFromClient.split(" ", 3);
            String action = requestParts[0];
            String identifier = "";
            if (requestParts.length > 1) {
                identifier = requestParts[1];
            }
            String fileContent = "";
            if (requestParts.length > 2) {
                fileContent = requestParts[2];
            }

            String fileName = "";
            if (identifier.equals("BY_NAME")) {
                fileName = requestParts[2];
            } else if (identifier.equals("BY_ID")) {
                fileName = idMap.get(Integer.parseInt(requestParts[2]));
            }

            if (action.equals("PUT")) {
                handlePutAction(output, fileName, fileContent);
            } else if (action.equals("GET")) {
                handleGetAction(output, fileName);
            } else if (action.equals("DELETE")) {
                handleDeleteAction(output, fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handlePutAction(DataOutputStream output, String fileName, String fileContent) throws IOException {
        if (fileName.isEmpty()) {
            fileName = UUID.randomUUID().toString();
        }

        Path filePath = Paths.get(DATA_DIR + fileName);
        if (Files.exists(filePath)) {
            output.writeUTF("403");
        } else {
            try {
                Files.write(filePath, fileContent.getBytes());
                int id = nextId.getAndIncrement();
                idMap.put(id, fileName);
                saveIdMap();
                output.writeUTF("200 " + id);
            } catch (IOException e) {
                output.writeUTF("403");
            }
        }
    }

    private static void handleGetAction(DataOutputStream output, String fileName) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        if (Files.exists(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            output.writeUTF("200 " + content.length);
            output.write(content);
        } else {
            output.writeUTF("404");
        }
    }

    private static void handleDeleteAction(DataOutputStream output, String fileName) throws IOException {
        Path filePath = Paths.get(DATA_DIR + fileName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            idMap.values().removeIf(value -> value.equals(fileName));
            saveIdMap();
            output.writeUTF("200");
        } else {
            output.writeUTF("404");
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadIdMap() {
        File file = new File(ID_MAP_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                idMap = (Map<Integer, String>) ois.readObject();
                nextId.set(idMap.keySet().stream().max(Integer::compareTo).orElse(0) + 1);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            idMap = new ConcurrentHashMap<>();
        }
    }

    private static void saveIdMap() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ID_MAP_FILE))) {
            oos.writeObject(idMap);
        } catch (IOException e) {
            e.printStackTrace();
            // Retry or log error
        }
    }
}