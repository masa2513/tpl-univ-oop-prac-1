
package oop1.section13;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

/**
 * メインウィンドウクラス
 * ディレクトリ選択とファイル一覧表示を行う
 */
public class MainFrame extends JFrame {

    private JButton openDirectoryButton;
    private JList<FileItem> fileList;
    private DefaultListModel<FileItem> listModel;
    private PreviewController previewController;

    public MainFrame() {
        previewController = new PreviewController(this);
        initializeComponents();
        setupLayout();
        setupEvents();
    }

    private void initializeComponents() {
        setTitle("ファイルプレビューアプリケーション");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // ディレクトリ選択ボタン
        openDirectoryButton = new JButton("ディレクトリを開く");

        // ファイル一覧リスト
        listModel = new DefaultListModel<>();
        fileList = new JList<>(listModel);
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.setCellRenderer(new FileListCellRenderer());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // 上部にボタン配置
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(openDirectoryButton);
        add(buttonPanel, BorderLayout.NORTH);

        // 中央にファイル一覧配置
        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupEvents() {
        // ディレクトリ選択ボタンのイベント
        openDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDirectory();
            }
        });

        // ファイル一覧の選択イベント
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FileItem selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    previewController.openPreview(selectedFile.getFile());
                }
            }
        });
    }

    private void openDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            loadFiles(selectedDirectory);
        }
    }

    private void loadFiles(File directory) {
        listModel.clear();

        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        listModel.addElement(new FileItem(file));
                    }
                }
            }
        }
    }

    /**
     * ファイルアイテムクラス ファイル情報とアイコンを保持
     */
    public static class FileItem {

        private File file;
        private Icon icon;

        public FileItem(File file) {
            this.file = file;
            this.icon = FileSystemView.getFileSystemView().getSystemIcon(file);
        }

        public File getFile() {
            return file;
        }

        public Icon getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return file.getName();
        }
    }

    /**
     * ファイルリスト用のカスタムレンダラー
     */
    private static class FileListCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof FileItem) {
                FileItem fileItem = (FileItem) value;
                setIcon(fileItem.getIcon());
                setText(fileItem.toString());
            }

            return this;
        }
    }
}
