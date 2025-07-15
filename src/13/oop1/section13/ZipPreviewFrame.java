package oop1.section13;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * ZIPプレビューウィンドウクラス
 */
public class ZipPreviewFrame extends JFrame {

    private JTree tree;
    private DefaultMutableTreeNode root;
    private File zipFile;

    public ZipPreviewFrame(File file) throws Exception {
        this.zipFile = file;
        initializeComponents();
        loadZipContents();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("ZIPプレビュー - " + zipFile.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        root = new DefaultMutableTreeNode(zipFile.getName());
        tree = new JTree(root);
        tree.setShowsRootHandles(true);
    }

    private void loadZipContents() throws Exception {
        Map<String, DefaultMutableTreeNode> directoryNodes = new HashMap<>();
        directoryNodes.put("", root);

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                String entryName = entry.getName();

                if (entry.isDirectory()) {
                    // ディレクトリの場合
                    createDirectoryNode(entryName, directoryNodes);
                } else {
                    // ファイルの場合
                    createFileNode(entryName, directoryNodes);
                }
            }
        } catch (IOException e) {
            throw new Exception("ZIPファイルを読み込めませんでした: " + e.getMessage());
        }

        // ツリーを展開
        expandTree();
    }

    private void createDirectoryNode(String path, Map<String, DefaultMutableTreeNode> directoryNodes) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (directoryNodes.containsKey(path)) {
            return;
        }

        String[] pathParts = path.split("/");
        StringBuilder currentPath = new StringBuilder();

        for (int i = 0; i < pathParts.length; i++) {
            if (i > 0) {
                currentPath.append("/");
            }
            currentPath.append(pathParts[i]);

            String current = currentPath.toString();
            if (!directoryNodes.containsKey(current)) {
                String parentPath = i == 0 ? "" : currentPath.substring(0, currentPath.lastIndexOf("/"));
                DefaultMutableTreeNode parentNode = directoryNodes.get(parentPath);

                DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(pathParts[i]);
                parentNode.add(dirNode);
                directoryNodes.put(current, dirNode);
            }
        }
    }

    private void createFileNode(String path, Map<String, DefaultMutableTreeNode> directoryNodes) {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        String parentPath = path.lastIndexOf("/") == -1 ? "" : path.substring(0, path.lastIndexOf("/"));

        // 親ディレクトリが存在しない場合は作成
        if (!directoryNodes.containsKey(parentPath) && !parentPath.isEmpty()) {
            createDirectoryNode(parentPath, directoryNodes);
        }

        DefaultMutableTreeNode parentNode = directoryNodes.get(parentPath);
        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(fileName);
        parentNode.add(fileNode);
    }

    private void expandTree() {
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private void setupLayout() {
        // スクロールパネルにツリーを配置
        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

        // ステータスバーを追加
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("ZIPファイル: " + zipFile.getName());
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }
}
