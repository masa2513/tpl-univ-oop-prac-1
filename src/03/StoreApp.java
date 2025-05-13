import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StoreApp extends JFrame {

    private JTextArea outputArea; // 処理結果を表示するエリア
    private Receipt receipt = new Receipt();
    JTextField janCodeField; // janコードを入力するフィールド
    // 販売商品を登録するための配列
    ProductItem[] saleProducts = new ProductItem[10];
    private JButton calcButton; // 合計計算ボタン


    public StoreApp() {
        saleProducts[0] = new ProductItem("アンパンマン", 100, 1, "1234567890123");
        saleProducts[1] = new ProductItem("バイキンマン", 200, 1, "2345678901234");
        saleProducts[2] = new ProductItem("ドキンちゃん", 300, 1, "3456789012345");
        saleProducts[3] = new ProductItem("食パンまん", 400, 1, "4567890123456");
        saleProducts[4] = new ProductItem("カレーパンマン", 500, 1, "5678901234567");
        saleProducts[5] = new ProductItem("チーズ", 600, 1, "6789012345678");
        saleProducts[6] = new ProductItem("バターおじさん", 700, 1, "7890123456789");
        saleProducts[7] = new ProductItem("メロンパンちゃん", 800, 1, "8901234567890");
        saleProducts[8] = new ProductItem("チーズパンちゃん", 900, 1, "9012345678901");
        saleProducts[9] = new ProductItem("ソーセージパンちゃん", 1000, 1, "0123456789012");
        // --- ウィンドウの基本設定 ---
        setTitle("レジ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);

        // --- レイアウトにBorderLayoutを採用 ---
        // 部品間の隙間を縦横5ピクセルに設定
        setLayout(new BorderLayout(5, 5));

        // GridBagLayoutを使用して柔軟な配置を行う
        JPanel topPanel = new JPanel(new GridBagLayout());
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // GridBagConstraintsのデフォルト設定
        gbc.insets = new Insets(5, 5, 5, 5); // 部品間の余白
        gbc.anchor = GridBagConstraints.WEST; // 左寄せを基本とする

        // --- 1行目: 商品名ラベルとフィールド ---
        // 商品名ラベル (gridx=0, gridy=0)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0; // ラベル列は伸縮させない
        gbc.fill = GridBagConstraints.NONE; // サイズ変更しない
        gbc.anchor = GridBagConstraints.EAST; // ラベルを右寄せにする
        JLabel productNameLabel = new JLabel("janコード:");
        topPanel.add(productNameLabel, gbc);

        // 商品名フィールド (gridx=1, gridy=0)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // フィールド列は横方向に伸縮させる
        gbc.fill = GridBagConstraints.HORIZONTAL; // 横方向にいっぱいに広げる
        janCodeField = new JTextField();
        topPanel.add(janCodeField, gbc);

        // 合計ボタンをボトムパネルに追加
        calcButton = new JButton("合計計算");
        bottomPanel.add(calcButton);

        // --- 中央に配置する部品 (結果表示エリア) ---
        outputArea = new JTextArea();
        // outputArea.setEditable(false); // 必要に応じて編集不可に設定
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // --- 部品をウィンドウに追加 ---
        // 上部パネルをウィンドウの北 (上) に配置
        add(topPanel, BorderLayout.NORTH);
        // スクロール可能なテキストエリアをウィンドウの中央に配置（中央領域は利用可能な残りのスペースをすべて使う）
        add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH); // ボタンを下部に配置

        // --- ボタンのアクション設定 ---
        calcButton.addActionListener(e -> {
            System.out.println("ボタンが押されました。");
            // 合計点数と合計金額を出力
            outputArea.append("\n--- 合計点数: " + receipt.getTotalQuantity() + " 点 ---");
            outputArea.append("\n--- 合計金額: " + receipt.getTotalPrice() + " 円 ---\n\n");
        });

        //janコードを入力してボタンを押された時の処理
        janCodeField.addActionListener(e-> {
            String janCode = janCodeField.getText();
            //janコードを元に商品を検索
            for (int i = 0; i < saleProducts.length; i++) {
                if (saleProducts[i].jancode.equals(janCode)&& saleProducts[i] != null) {
                    // 商品が見つかった場合、レシートに追加
                    receipt.addProduct(saleProducts[i]);
                    //結果をテキストエリアに表示
                    outputArea.append(saleProducts[i].toString());
                    break; // 商品が見つかったのでループを抜ける
                }
            }
            janCodeField.setText(""); // フィールドをクリア
            janCodeField.requestFocus(); // フォーカスを戻す
        });

        // --- ウィンドウを表示 ---
        setVisible(true);
    }

    public static void main(String[] args) {
        // イベントディスパッチスレッドでGUIを作成・実行
        SwingUtilities.invokeLater(() -> new StoreApp());
    }
}