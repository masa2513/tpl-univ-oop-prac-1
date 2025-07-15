package oop1.section13;

import javax.swing.SwingUtilities;

/**
 * ファイルプレビューアプリケーションのメインクラス
 */
public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
