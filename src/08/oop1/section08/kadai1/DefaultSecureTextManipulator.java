package oop1.section08.kadai1;

/**
 * SecureTextManipulatorインターフェイスのデフォルト実装
 */
public class DefaultSecureTextManipulator implements SecureTextManipulator {
    @Override
    public String getFirstNCharsAsUpperCase(String text, int n) {
        // 早期リターン: null、空文字列、またはnが0以下の場合
        if (text == null || text.isEmpty() || n <= 0) {
            return "";
        }

        // 文字列の長さとnの小さい方を取得
        int length = Math.min(text.length(), n);
        
        // 最初のn文字を抽出して大文字に変換
        return text.substring(0, length).toUpperCase();
    }
} 