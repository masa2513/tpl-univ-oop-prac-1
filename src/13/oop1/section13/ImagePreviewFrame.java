package oop1.section13;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * 画像プレビューウィンドウクラス
 */
public class ImagePreviewFrame extends JFrame {

    private JLabel imageLabel;
    private File imageFile;

    public ImagePreviewFrame(File file) throws Exception {
        this.imageFile = file;
        initializeComponents();
        loadImage();
        setupLayout();
    }

    private void initializeComponents() {
        setTitle("画像プレビュー - " + imageFile.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
    }

    private void loadImage() throws Exception {
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new Exception("画像を読み込めませんでした");
        }

        ImageIcon imageIcon = new ImageIcon(image);
        imageLabel.setIcon(imageIcon);
    }

    private void setupLayout() {
        // スクロールパネルに画像を配置
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setPreferredSize(new Dimension(780, 580));

        add(scrollPane, BorderLayout.CENTER);

        // 画像サイズに応じてウィンドウサイズを調整
        pack();

        // 最大サイズを制限
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int) (screenSize.width * 0.8);
        int maxHeight = (int) (screenSize.height * 0.8);

        if (getWidth() > maxWidth) {
            setSize(maxWidth, getHeight());
        }
        if (getHeight() > maxHeight) {
            setSize(getWidth(), maxHeight);
        }

        setLocationRelativeTo(null);
    }
}
