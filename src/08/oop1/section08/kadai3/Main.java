package oop1.section08.kadai3;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // メインフレームを作成
            JFrame frame = new JFrame("Validation Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

            // 年齢入力フィールド (0-100の範囲で検証)
            InputValidator ageValidator = new RangeValidator(0, 100);
            ValidatedTextField ageField = new ValidatedTextField(ageValidator, 10);
            frame.add(new JLabel("年齢 (0-100):"));
            frame.add(ageField);

            // 数量入力フィールド (1-99の範囲で検証)
            InputValidator quantityValidator = new RangeValidator(1, 99);
            ValidatedTextField quantityField = new ValidatedTextField(quantityValidator, 5);
            frame.add(new JLabel("数量 (1-99):"));
            frame.add(quantityField);

            // フォーカス移動用のダミーボタン
            frame.add(new JButton("OK"));

            // フレームのサイズを内容に合わせて自動調整
            frame.pack();
            // フレームを画面中央に表示
            frame.setLocationRelativeTo(null);
            // フレームを表示状態にする
            frame.setVisible(true);
        });
    }
} 