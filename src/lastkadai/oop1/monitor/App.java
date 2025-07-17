package oop1.monitor;

import javax.swing.SwingUtilities;

/**
 * システム監視ツールのメインアプリケーションクラス
 *
 * @author K24044
 */
public class App {

    public static void main(String[] args) {
        // GUIをEDTで実行
        SwingUtilities.invokeLater(() -> {
            try {
                // メインフレームを表示
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);

                System.out.println("システム監視ツールが起動しました。");

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("アプリケーションの初期化に失敗しました: " + e.getMessage());
                System.exit(1);
            }
        });
    }
}
