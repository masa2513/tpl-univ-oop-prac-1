package oop1.section13;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * テキストプレビューウィンドウクラス
 */
public class TextPreviewFrame extends JFrame {

    private JTextArea textArea;
    private File textFile;

    public TextPreviewFrame(File file) throws Exception {
        this.textFile = file;
        initializeComponents();
        loadText();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("テキストプレビュー - " + textFile.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setTabSize(4);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private void loadText() throws Exception {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new Exception("テキストファイルを読み込めませんでした: " + e.getMessage());
        }

        textArea.setText(content.toString());
        textArea.setCaretPosition(0);
    }

    private void setupLayout() {
        // スクロールパネルにテキストエリアを配置
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        // ステータスバーを追加
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("ファイル: " + textFile.getName() + " (読み取り専用)");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
}
