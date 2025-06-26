package oop1.section10;

import java.io.PrintWriter;

/**
 * HTTPレスポンスの生成と送信を担当するクラス
 */
public class ResponseGenerator {

    /**
     * インデックスページのレスポンスを送信
     */
    public static void sendIndexPage(PrintWriter out) {
        String html = ContentGenerator.generateIndexHtml();
        sendHttpResponse(out, "200 OK", "text/html; charset=utf-8", html);
    }

    /**
     * 挨拶ページのレスポンスを送信
     *
     * @param out PrintWriter
     * @param name 表示する名前
     */
    public static void sendHelloPage(PrintWriter out, String name) {
        String html = ContentGenerator.generateHelloHtml(name);
        sendHttpResponse(out, "200 OK", "text/html; charset=utf-8", html);
    }

    /**
     * CSSファイルのレスポンスを送信
     */
    public static void sendCssResponse(PrintWriter out) {
        String css = ContentGenerator.generateCss();
        sendHttpResponse(out, "200 OK", "text/css; charset=utf-8", css);
    }

    /**
     * JavaScriptファイルのレスポンスを送信
     */
    public static void sendJsResponse(PrintWriter out) {
        String js = ContentGenerator.generateJavaScript();
        sendHttpResponse(out, "200 OK", "text/javascript; charset=utf-8", js);
    }

    /**
     * 404エラーレスポンスを送信
     */
    public static void send404Response(PrintWriter out) {
        String html = ContentGenerator.generate404Html();
        sendHttpResponse(out, "404 Not Found", "text/html; charset=utf-8", html);
    }

    /**
     * HTTPレスポンスを送信する共通メソッド
     *
     * @param out PrintWriter
     * @param status HTTPステータス
     * @param contentType コンテンツタイプ
     * @param body レスポンスボディ
     */
    private static void sendHttpResponse(PrintWriter out, String status, String contentType, String body) {
        out.println("HTTP/1.0 " + status);
        out.println("Content-Type: " + contentType);
        out.println("Content-Length: " + body.getBytes().length);
        out.println("Connection: close");
        out.println();
        out.print(body);
        out.flush();
    }
}
