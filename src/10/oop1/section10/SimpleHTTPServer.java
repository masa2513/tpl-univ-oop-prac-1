package oop1.section10;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 簡易HTTPサーバー ポート8088で待受し、HTTPリクエストに対してレスポンスを返す
 */
public class SimpleHTTPServer {

    private static final int PORT = 8088;

    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean running = false;

    public SimpleHTTPServer() {
        threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        running = true;

        System.out.println("SimpleHTTPServer started on port " + PORT);
        System.out.println("Access http://localhost:" + PORT + "/");

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        SimpleHTTPServer server = new SimpleHTTPServer();

        // シャットダウンフックを追加
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("\nShutting down server...");
                server.stop();
            } catch (IOException e) {
                System.err.println("Error stopping server: " + e.getMessage());
            }
        }));

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
