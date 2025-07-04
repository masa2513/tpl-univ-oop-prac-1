package oop1.section11;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class BmiCalculatorApp extends JFrame {

    private JTextField heightField;
    private JTextField weightField;
    private JLabel resultLabel;

    public BmiCalculatorApp() {
        initializeComponents();
        layoutComponents();
        setupEventListeners();
        setupWindow();
    }

    private void initializeComponents() {
        heightField = new JTextField(10);
        weightField = new JTextField(10);
        resultLabel = new JLabel("身長と体重を入力してください");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // 入力パネル
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        inputPanel.add(new JLabel("身長（cm）:"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("体重（kg）:"));
        inputPanel.add(weightField);

        JButton calculateButton = new JButton("計算実行");
        inputPanel.add(new JLabel()); // 空のラベルでスペースを作る
        inputPanel.add(calculateButton);

        // 結果表示パネル
        JPanel resultPanel = new JPanel(new FlowLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        resultPanel.add(resultLabel);

        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);

        // イベントリスナーの設定
        calculateButton.addActionListener(new CalculateButtonListener());
    }

    private void setupEventListeners() {
        // EnterキーでもBMI計算を実行
        ActionListener calculateAction = new CalculateButtonListener();
        heightField.addActionListener(calculateAction);
        weightField.addActionListener(calculateAction);
    }

    private void setupWindow() {
        setTitle("BMI計算機");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private class CalculateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            calculateBMI();
        }
    }

    private void calculateBMI() {
        try {
            // 入力値の取得
            String heightText = heightField.getText().trim();
            String weightText = weightField.getText().trim();

            // 入力チェック
            if (heightText.isEmpty() || weightText.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "身長と体重を入力してください",
                        "入力エラー",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 数値変換
            double height = Double.parseDouble(heightText);
            double weight = Double.parseDouble(weightText);

            // 値の妥当性チェック
            if (height <= 0 || weight <= 0) {
                JOptionPane.showMessageDialog(this,
                        "正の数値を入力してください",
                        "入力エラー",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // BMI計算（身長をcmからmに変換）
            double heightInMeters = height / 100.0;
            double bmi = weight / (heightInMeters * heightInMeters);

            // BMI判定
            String category = getBMICategory(bmi);

            // 結果表示
            String resultText = String.format("BMI: %.2f (%s)", bmi, category);
            resultLabel.setText(resultText);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "数値を入力してください",
                    "入力エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "低体重（痩せ型）";
        } else if (bmi < 25) {
            return "普通体重";
        } else if (bmi < 30) {
            return "肥満（1度）";
        } else {
            return "肥満（2度以上）";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BmiCalculatorApp().setVisible(true);
        });
    }
}
