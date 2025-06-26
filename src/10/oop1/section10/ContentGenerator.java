package oop1.section10;

/**
 * WebコンテンツのHTML、CSS、JavaScriptを生成するクラス
 */
public class ContentGenerator {

    private static final String STUDENT_ID = "K24044";
    private static final String STUDENT_NAME = "加藤雅士";

    /**
     * インデックスページのHTMLを生成
     */
    public static String generateIndexHtml() {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"ja\">\n"
                + "<head>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <title>SimpleHTTPServer</title>\n"
                + "  <link rel=\"stylesheet\" href=\"style.css\">\n"
                + "</head>\n"
                + "<body>\n"
                + "  <main>\n"
                + "    <h1>このページはSimpleHTTPServerより生成されて返されています。</h1>\n"
                + "    <p><button class=\"fire\">Push!!</button></p>\n"
                + "    <p class=\"copyright\">" + STUDENT_ID + " - " + STUDENT_NAME + "</p>\n"
                + "  </main>\n"
                + "  <script src=\"script.js\"></script>\n"
                + "</body>\n"
                + "</html>";
    }

    /**
     * 挨拶ページのHTMLを生成
     *
     * @param name 表示する名前
     */
    public static String generateHelloHtml(String name) {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"ja\">\n"
                + "<head>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <title>SimpleHTTPServer</title>\n"
                + "  <link rel=\"stylesheet\" href=\"style.css\">\n"
                + "</head>\n"
                + "<body>\n"
                + "  <main>\n"
                + "    <h1>こんにちは！" + name + "さん！！</h1>\n"
                + "  </main>\n"
                + "</body>\n"
                + "</html>";
    }

    /**
     * 404エラーページのHTMLを生成
     */
    public static String generate404Html() {
        return "<!DOCTYPE html>\n"
                + "<html lang=\"ja\">\n"
                + "<head>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                + "  <meta charset=\"UTF-8\">\n"
                + "  <title>404</title>\n"
                + "  <link rel=\"stylesheet\" href=\"style.css\">\n"
                + "</head>\n"
                + "<body>\n"
                + "  <main>\n"
                + "    <h1>404... Not Found!</h1>\n"
                + "  </main>\n"
                + "</body>\n"
                + "</html>";
    }

    /**
     * CSSスタイルシートを生成
     */
    public static String generateCss() {
        return "* {\n"
                + "  margin: 0;\n"
                + "  padding: 0;\n"
                + "  box-sizing: border-box;\n"
                + "}\n"
                + "body {\n"
                + "  height: 100vh;\n"
                + "  display: flex;\n"
                + "  justify-content: center;\n"
                + "  align-items: center;\n"
                + "}\n"
                + "main {\n"
                + "  height: 450px;\n"
                + "  max-height: 90vh;\n"
                + "  width: 800px;\n"
                + "  max-width: 90vw;\n"
                + "  border-radius: 10px;\n"
                + "  box-shadow: rgba(0, 0, 0, 0.1) 0px 20px 60px -10px;\n"
                + "  display: flex;\n"
                + "  justify-content: center;\n"
                + "  align-items: center;\n"
                + "  flex-direction: column;\n"
                + "}\n"
                + "h1 {\n"
                + "  padding: 0 3em;\n"
                + "  margin-bottom: 2em;\n"
                + "  text-align: center;\n"
                + "}\n"
                + "button {\n"
                + "  font-size: 1.25em;\n"
                + "  padding: 0.5em 1em;\n"
                + "}\n"
                + ".copyright {\n"
                + "  margin-top: 20px;\n"
                + "  text-decoration: underline;\n"
                + "  font-style: italic;\n"
                + "}";
    }

    /**
     * JavaScriptコードを生成
     */
    public static String generateJavaScript() {
        return "var btn = document.querySelector('button.fire');\n"
                + "btn.addEventListener('click', function() {\n"
                + "  alert('Hello, SimpleHTTPServer!!');\n"
                + "});";
    }
}
