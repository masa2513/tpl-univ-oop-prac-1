package oop1.section11;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SurveyApp extends JFrame {

    private static final String CSV_FILE_NAME = "survey_results.csv";

    private JTextField nameField;
    private ButtonGroup ageGroup;
    private JRadioButton age20s, age30s, age40s;
    private JCheckBox programmingBox, designBox, travelBox;
    private JTextArea resultArea;
    private JButton submitButton;

    public SurveyApp() {
        initializeComponents();
        layoutComponents();
        setupEventListeners();
        setupWindow();
        loadExistingData();
    }

    private void initializeComponents() {
        // 氏名入力
        nameField = new JTextField(20);

        // 年代選択
        ageGroup = new ButtonGroup();
        age20s = new JRadioButton("20代");
        age30s = new JRadioButton("30代");
        age40s = new JRadioButton("40代");
        ageGroup.add(age20s);
        ageGroup.add(age30s);
        ageGroup.add(age40s);

        // 興味分野選択
        programmingBox = new JCheckBox("プログラミング");
        designBox = new JCheckBox("デザイン");
        travelBox = new JCheckBox("旅行");

        // 結果表示エリア
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);

        // 送信ボタン
        submitButton = new JButton("回答を送信");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // メインパネル
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        // 氏名入力
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(new JLabel("氏名:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);

        // 年代選択
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(new JLabel("年代:"), gbc);

        JPanel agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        agePanel.add(age20s);
        agePanel.add(age30s);
        agePanel.add(age40s);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(agePanel, gbc);

        // 興味分野
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("興味のある分野:"), gbc);

        JPanel interestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        interestPanel.add(programmingBox);
        interestPanel.add(designBox);
        interestPanel.add(travelBox);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(interestPanel, gbc);

        // 送信ボタン
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(submitButton, gbc);

        // 結果表示エリア
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("過去の回答一覧"));

        add(mainPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        submitButton.addActionListener(new SubmitButtonListener());
    }

    private void setupWindow() {
        setTitle("アンケート集計アプリ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
    }

    private class SubmitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            submitSurvey();
        }
    }

    private void submitSurvey() {
        try {
            // 入力チェック
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "氏名を入力してください",
                        "入力エラー",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 年代の取得
            String age = "未選択";
            if (age20s.isSelected()) {
                age = "20代";
            } else if (age30s.isSelected()) {
                age = "30代";
            } else if (age40s.isSelected()) {
                age = "40代";
            }

            // 興味分野の取得
            List<String> interests = new ArrayList<>();
            if (programmingBox.isSelected()) {
                interests.add("プログラミング");
            }
            if (designBox.isSelected()) {
                interests.add("デザイン");
            }
            if (travelBox.isSelected()) {
                interests.add("旅行");
            }

            String interestStr = String.join(";", interests);
            if (interestStr.isEmpty()) {
                interestStr = "なし";
            }

            // 現在日時の取得
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);

            // CSV形式のデータ作成
            String csvData = String.format("%s,%s,%s,%s", timestamp, name, age, interestStr);

            // ファイルに保存
            saveToFile(csvData);

            // 結果表示エリアに追加
            String displayData = String.format("[%s] %s (%s) - %s%n",
                    timestamp, name, age, interestStr);
            resultArea.append(displayData);

            // フォームのクリア
            clearForm();

            JOptionPane.showMessageDialog(this,
                    "回答を送信しました",
                    "送信完了",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "エラーが発生しました: " + ex.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToFile(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_NAME, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    private void loadExistingData() {
        File file = new File(CSV_FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length >= 4) {
                    String timestamp = parts[0];
                    String name = parts[1];
                    String age = parts[2];
                    String interests = parts[3];

                    String displayData = String.format("[%s] %s (%s) - %s%n",
                            timestamp, name, age, interests);
                    resultArea.append(displayData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "データファイルの読み込み中にエラーが発生しました: " + e.getMessage(),
                    "ファイル読み込みエラー",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        ageGroup.clearSelection();
        programmingBox.setSelected(false);
        designBox.setSelected(false);
        travelBox.setSelected(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SurveyApp().setVisible(true);
        });
    }
}
