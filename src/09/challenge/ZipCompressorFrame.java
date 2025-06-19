package oop1.section09.challenge;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * ドラッグ&ドロップZIP圧縮アプリケーションのメインウィンドウ
 * 
 * @author K24044KK
 */
public class ZipCompressorFrame extends JFrame {
    
    private JLabel dropLabel;
    private JTextArea fileListArea;
    private JButton compressButton;
    private JButton clearButton;
    private List<File> droppedFiles;
    
    public ZipCompressorFrame() {
        droppedFiles = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupDragAndDrop();
        setupEventHandlers();
    }
    
    /**
     * コンポーネントを初期化
     */
    private void initializeComponents() {
        // ウィンドウの基本設定
        setTitle("ZIP圧縮ツール - ファイルをドロップしてください");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        // ドロップラベル
        dropLabel = new JLabel("<html><div style='text-align: center;'>" +
                               "<h2>ここにファイルをドロップ</h2>" +
                               "<p>ファイルやフォルダをここにドラッグ&ドロップしてください</p>" +
                               "</div></html>");
        dropLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dropLabel.setVerticalAlignment(SwingConstants.CENTER);
        dropLabel.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5, 5, false));
        dropLabel.setBackground(new Color(240, 248, 255));
        dropLabel.setOpaque(true);
        dropLabel.setPreferredSize(new Dimension(580, 150));
        
        // ファイルリスト表示エリア
        fileListArea = new JTextArea();
        fileListArea.setEditable(false);
        fileListArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        fileListArea.setText("ドロップされたファイルがここに表示されます...");
        
        // ボタン
        compressButton = new JButton("ZIP圧縮実行");
        compressButton.setEnabled(false);
        compressButton.setPreferredSize(new Dimension(120, 30));
        
        clearButton = new JButton("リストクリア");
        clearButton.setEnabled(false);
        clearButton.setPreferredSize(new Dimension(120, 30));
    }
    
    /**
     * レイアウトを設定
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 上部: ドロップエリア
        add(dropLabel, BorderLayout.NORTH);
        
        // 中央: ファイルリスト
        JScrollPane scrollPane = new JScrollPane(fileListArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("ドロップされたファイル一覧"));
        add(scrollPane, BorderLayout.CENTER);
        
        // 下部: ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(clearButton);
        buttonPanel.add(compressButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * ドラッグ&ドロップ機能を設定
     */
    private void setupDragAndDrop() {
        // ドロップターゲットを作成
        DropTarget dropTarget = new DropTarget(dropLabel, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                    dropLabel.setBackground(new Color(220, 255, 220));
                } else {
                    dtde.rejectDrag();
                }
            }
            
            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                // ドラッグ中の処理（特に何もしない）
            }
            
            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                // ドロップアクション変更時の処理（特に何もしない）
            }
            
            @Override
            public void dragExit(DropTargetEvent dte) {
                dropLabel.setBackground(new Color(240, 248, 255));
            }
            
            @Override
            public void drop(DropTargetDropEvent dtde) {
                dropLabel.setBackground(new Color(240, 248, 255));
                
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();
                    
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        @SuppressWarnings("unchecked")
                        List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                        
                        for (File file : files) {
                            if (!droppedFiles.contains(file)) {
                                droppedFiles.add(file);
                            }
                        }
                        
                        updateFileList();
                        dtde.dropComplete(true);
                    } else {
                        dtde.dropComplete(false);
                    }
                } catch (Exception e) {
                    ZipCompressorApp.showErrorDialog(ZipCompressorFrame.this, 
                                                   "ファイルのドロップに失敗しました", e);
                    dtde.dropComplete(false);
                }
            }
        });
        
        // ドロップターゲットを設定
        setDropTarget(dropTarget);
    }
    
    /**
     * イベントハンドラーを設定
     */
    private void setupEventHandlers() {
        // ZIP圧縮ボタン
        compressButton.addActionListener(e -> {
            if (!droppedFiles.isEmpty()) {
                compressFiles();
            }
        });
        
        // クリアボタン
        clearButton.addActionListener(e -> {
            droppedFiles.clear();
            updateFileList();
        });
    }
    
    /**
     * ファイルリスト表示を更新
     */
    private void updateFileList() {
        if (droppedFiles.isEmpty()) {
            fileListArea.setText("ドロップされたファイルがここに表示されます...");
            compressButton.setEnabled(false);
            clearButton.setEnabled(false);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("ドロップされたファイル (").append(droppedFiles.size()).append("個):\n\n");
            
            for (int i = 0; i < droppedFiles.size(); i++) {
                File file = droppedFiles.get(i);
                sb.append(String.format("%d. %s%s\n", 
                    i + 1, 
                    file.getAbsolutePath(),
                    file.isDirectory() ? " [フォルダ]" : ""));
            }
            
            fileListArea.setText(sb.toString());
            fileListArea.setCaretPosition(0);
            compressButton.setEnabled(true);
            clearButton.setEnabled(true);
        }
    }
    
    /**
     * ファイルを圧縮
     */
    private void compressFiles() {
        if (droppedFiles.isEmpty()) {
            return;
        }
        
        try {
            // 圧縮処理を別スレッドで実行
            compressButton.setEnabled(false);
            compressButton.setText("圧縮中...");
            
            SwingWorker<File, Void> worker = new SwingWorker<File, Void>() {
                @Override
                protected File doInBackground() throws Exception {
                    FileZipper zipper = new FileZipper();
                    return zipper.compressFiles(droppedFiles);
                }
                
                @Override
                protected void done() {
                    try {
                        File zipFile = get();
                        String message = String.format(
                            "圧縮が完了しました！\n\n" +
                            "保存先: %s\n" +
                            "ファイルサイズ: %.2f MB",
                            zipFile.getAbsolutePath(),
                            zipFile.length() / (1024.0 * 1024.0)
                        );
                        ZipCompressorApp.showInfoDialog(ZipCompressorFrame.this, message);
                        
                        // リストをクリア
                        droppedFiles.clear();
                        updateFileList();
                        
                    } catch (Exception e) {
                        ZipCompressorApp.showErrorDialog(ZipCompressorFrame.this, 
                                                       "圧縮処理に失敗しました", e);
                    } finally {
                        compressButton.setEnabled(true);
                        compressButton.setText("ZIP圧縮実行");
                    }
                }
            };
            
            worker.execute();
            
        } catch (Exception e) {
            ZipCompressorApp.showErrorDialog(this, "圧縮処理の開始に失敗しました", e);
            compressButton.setEnabled(true);
            compressButton.setText("ZIP圧縮実行");
        }
    }
} 