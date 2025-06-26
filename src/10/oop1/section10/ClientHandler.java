package oop1.section10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

/**
 * クライアントからのHTTPリクエストを処理するクラス
 */
public class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            System.out.println("Request: " + requestLine);

            // HTTPヘッダーを読み飛ばす
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // ヘッダーを読み飛ばす
            }

            // リクエストラインを解析
            String[] parts = requestLine.split(" ");
            if (parts.length >= 2) {
                String method = parts[0];
                String path = parts[1];

                if ("GET".equals(method)) {
                    handleGetRequest(path, out);
                } else {
                    ResponseGenerator.send404Response(out);
                }
            } else {
                ResponseGenerator.send404Response(out);
            }

        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    /**
     * GETリクエストを処理する
     *
     * @param path リクエストパス
     * @param out PrintWriter
     */
    private void handleGetRequest(String path, PrintWriter out) {
        if (path.equals("/") || path.equals("/index.html")) {
            ResponseGenerator.sendIndexPage(out);
        } else if (path.startsWith("/hello?name=")) {
            String name = extractNameFromQuery(path);
            ResponseGenerator.sendHelloPage(out, name);
        } else if (path.equals("/style.css")) {
            ResponseGenerator.sendCssResponse(out);
        } else if (path.equals("/script.js")) {
            ResponseGenerator.sendJsResponse(out);
        } else {
            ResponseGenerator.send404Response(out);
        }
    }

    /**
     * クエリパラメータから名前を抽出する
     *
     * @param path リクエストパス
     * @return 名前
     */
    private String extractNameFromQuery(String path) {
        int nameIndex = path.indexOf("name=");
        if (nameIndex != -1) {
            String name = path.substring(nameIndex + 5);
            try {
                return URLDecoder.decode(name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return name;
            }
        }
        return "Unknown";
    }
}
