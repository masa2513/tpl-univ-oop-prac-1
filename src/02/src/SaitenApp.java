import javax.swing.*;
import java.awt.*;

public class SaitenApp extends JFrame {

    private JTextField inputField; // 文字を入力するフィールド
    private JButton processButton; // 処理を実行するボタン
    private JTextArea outputArea;  // 処理結果を表示するエリア


    public SaitenApp() {
        // --- ウィンドウの基本設定 ---
        setTitle("K24044:採点");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);

        // --- レイアウトにBorderLayoutを採用 ---
        // 部品間の隙間を縦横5ピクセルに設定
        setLayout(new BorderLayout(5, 5));

        // --- 上部に配置する部品 (入力欄、ボタンなど) ---
        // これらの部品をまとめるためのパネルを作成 (FlowLayoutを使用)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel inputLabel = new JLabel("テストの点数：");
        inputField = new JTextField(15);
        processButton = new JButton("処理実行");

        // パネルに部品を追加
        topPanel.add(inputLabel);
        topPanel.add(inputField);
        topPanel.add(processButton);

        // --- 中央に配置する部品 (結果表示エリア) ---
        outputArea = new JTextArea(); // 初期サイズはBorderLayoutが調整
        outputArea.setEditable(false); // 編集不可に設定
        // テキストエリアをスクロール可能にする (JScrollPaneでラップ)
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // --- 部品をウィンドウに追加 ---
        // 上部パネルをウィンドウの北 (上) に配置
        add(topPanel, BorderLayout.NORTH);
        // スクロール可能なテキストエリアをウィンドウの中央に配置（中央領域は利用可能な残りのスペースをすべて使う）
        add(scrollPane, BorderLayout.CENTER);

        // --- ボタンのアクション設定 ---
        processButton.addActionListener(e -> {
            String inputText = inputField.getText();
            int score = Integer.parseInt(inputText);
            // テキストエリアの内容を全て消去する
            outputArea.setText("");
//            変換した整数値が、0〜100の範囲外だった場合、「入力された値は点数として正しくありません。」と表示する
//            範囲内だった場合、以下の条件に応じてそれぞれ画面にメッセージを表示する
//            100〜60:「合格です。おめでとう！」
//            59〜20:「不合格です。再テストを行いましょう！」
//            19〜0:「不合格です。来年もう一年頑張りましょう。」
            if(score<=100 && score>=60){
                outputArea.setText("合格です。おめでとう！");
            } else if (score<=59 && score >=20) {
                outputArea.setText("不合格です。再テストを行いましょう！");
            } else if (score <=19 && score >=0) {
                outputArea.setText("不合格です。来年もう一年頑張りましょう。");
            } else{
                outputArea.setText("入力された値は点数として正しくありません。");
            }
        });

        // --- ウィンドウを表示 ---
        setVisible(true);
    }

    public static void main(String[] args) {
        // イベントディスパッチスレッドでGUIを作成・実行
        SwingUtilities.invokeLater(() -> new SaitenApp());
    }
}
