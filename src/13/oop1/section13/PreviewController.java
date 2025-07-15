package oop1.section13;

import javax.swing.*;
import java.io.File;

/**
 * プレビュー機能を制御するコントローラクラス
 */
public class PreviewController {

    private MainFrame mainFrame;
    private JFrame currentPreviewFrame;

    public PreviewController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    /**
     * ファイルのプレビューを開く
     *
     * @param file プレビューするファイル
     */
    public void openPreview(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        // 既存のプレビューウィンドウを閉じる
        closeCurrentPreview();

        String extension = getFileExtension(file).toLowerCase();

        try {
            if (isImageFile(extension)) {
                currentPreviewFrame = new ImagePreviewFrame(file);
            } else if (isTextFile(extension)) {
                currentPreviewFrame = new TextPreviewFrame(file);
            } else if (isZipFile(extension)) {
                currentPreviewFrame = new ZipPreviewFrame(file);
            } else {
                // サポートされていないファイル形式
                return;
            }

            currentPreviewFrame.setVisible(true);

        } catch (Exception e) {
            // エラーが発生した場合
            JOptionPane.showMessageDialog(mainFrame,
                    "ファイルが破損しているか、サポートされていない形式です。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);

            // エラーが発生したプレビューウィンドウを閉じる
            if (currentPreviewFrame != null) {
                currentPreviewFrame.dispose();
                currentPreviewFrame = null;
            }
        }
    }

    /**
     * 現在のプレビューウィンドウを閉じる
     */
    public void closeCurrentPreview() {
        if (currentPreviewFrame != null) {
            currentPreviewFrame.dispose();
            currentPreviewFrame = null;
        }
    }

    /**
     * ファイルの拡張子を取得
     *
     * @param file ファイル
     * @return 拡張子（ピリオドなし）
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < name.length() - 1) {
            return name.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 画像ファイルかどうかを判定
     *
     * @param extension 拡張子
     * @return 画像ファイルの場合true
     */
    private boolean isImageFile(String extension) {
        return extension.equals("png") || extension.equals("jpg")
                || extension.equals("jpeg") || extension.equals("gif")
                || extension.equals("bmp");
    }

    /**
     * テキストファイルかどうかを判定
     *
     * @param extension 拡張子
     * @return テキストファイルの場合true
     */
    private boolean isTextFile(String extension) {
        return extension.equals("txt") || extension.equals("csv")
                || extension.equals("xml") || extension.equals("java")
                || extension.equals("md");
    }

    /**
     * ZIPファイルかどうかを判定
     *
     * @param extension 拡張子
     * @return ZIPファイルの場合true
     */
    private boolean isZipFile(String extension) {
        return extension.equals("zip");
    }
}
