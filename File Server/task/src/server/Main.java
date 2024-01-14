package server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(23456);
        server.start();

        // when you want to stop the server:
        // server.stop();
    }
}