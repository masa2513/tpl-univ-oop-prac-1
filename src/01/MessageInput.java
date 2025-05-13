import java.util.Scanner;
public class MessageInput {
    public static void main(String[] args) {
        // 標準入力をScannerで取得する
        System.out.println("こんにちは、メッセージをどうぞ");
        Scanner in = new Scanner(System.in);
        // nextLine()メソッドは、キーボードからReturnキーの入力があるまで待ち、入力された1行を返す
        String inputLine = in.nextLine();
        // ↑inputLineという変数には、入力された文字列データが設定されています

        // 入力された文字列データをそのまま出力
        System.out.println("メッセージを受信しました");
        System.out.println("----");
        System.out.println(inputLine);
        System.out.println("----");
    }
}