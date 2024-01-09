import java.io.IOException;

public class Application {
    public static void main(String[] args) {
        try {
            String classpath = System.getProperty("java.class.path");

            // Start the server
            ProcessBuilder serverProcessBuilder = new ProcessBuilder("java", "-cp", classpath, "server.Main");
            serverProcessBuilder.inheritIO();
            Process serverProcess = serverProcessBuilder.start();

            // Start the client
            ProcessBuilder clientProcessBuilder = new ProcessBuilder("java", "-cp", classpath, "client.Main");
            clientProcessBuilder.inheritIO();
            Process clientProcess = clientProcessBuilder.start();

            // Wait for the server and client to finish
            serverProcess.waitFor();
            clientProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}