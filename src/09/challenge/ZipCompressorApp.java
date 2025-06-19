package oop1.section09.challenge;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * ドラッグ&ドロップZIP圧縮アプリケーションのメインクラス
 * 
 * @author K24044KK
 */
public class ZipCompressorApp {
    
    public static void main(String[] args) {
        // デフォルトのLook&Feelを使用
        
        // イベントディスパッチスレッドでGUIを作成
        SwingUtilities.invokeLater(() -> {
            try {
                ZipCompressorFrame frame = new ZipCompressorFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                showErrorDialog(null, "アプリケーションの起動に失敗しました", e);
            }
        });
    }
    
    /**
     * エラーダイアログを表示する共通メソッド
     */
    public static void showErrorDialog(Component parent, String message, Exception e) {
        String errorMessage = message + "\n\n詳細: " + e.getMessage();
        JOptionPane.showMessageDialog(parent, errorMessage, "エラー", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * 情報ダイアログを表示する共通メソッド
     */
    public static void showInfoDialog(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "情報", JOptionPane.INFORMATION_MESSAGE);
    }
} 