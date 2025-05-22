package oop1.k24044;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class MainFrame extends JFrame {
    private DrawingPanel drawingPanel;

    public MainFrame() {
        setTitle("図形描画");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        drawingPanel = new DrawingPanel();

        // --- 図形選択ラジオボタン ---
        JRadioButton circleRadioButton = new JRadioButton("円");
        circleRadioButton.setActionCommand("Circle");
        circleRadioButton.setSelected(true);
        drawingPanel.setCurrentShapeType("Circle");

        JRadioButton rectangleRadioButton = new JRadioButton("四角形");
        rectangleRadioButton.setActionCommand("Rectangle");

        ButtonGroup shapeGroup = new ButtonGroup();
        shapeGroup.add(circleRadioButton);
        shapeGroup.add(rectangleRadioButton);

        ActionListener shapeSelectionListener = e -> {
            drawingPanel.setCurrentShapeType(e.getActionCommand());
        };

        circleRadioButton.addActionListener(shapeSelectionListener);
        rectangleRadioButton.addActionListener(shapeSelectionListener);

        // --- 色選択ラジオボタン ---
        JRadioButton redRadioButton = new JRadioButton("赤");
        redRadioButton.setForeground(Color.RED);
        JRadioButton blueRadioButton = new JRadioButton("青");
        blueRadioButton.setForeground(Color.BLUE);
        JRadioButton greenRadioButton = new JRadioButton("緑");
        greenRadioButton.setForeground(Color.GREEN);

        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(redRadioButton);
        colorGroup.add(blueRadioButton);
        colorGroup.add(greenRadioButton);

        blueRadioButton.setSelected(true);
        drawingPanel.setCurrentColor(Color.BLUE);

        ActionListener colorSelectionListener = e -> {
            if (e.getSource() == redRadioButton) {
                drawingPanel.setCurrentColor(Color.RED);
            } else if (e.getSource() == blueRadioButton) {
                drawingPanel.setCurrentColor(Color.BLUE);
            } else if (e.getSource() == greenRadioButton) {
                drawingPanel.setCurrentColor(Color.GREEN);
            }
        };

        redRadioButton.addActionListener(colorSelectionListener);
        blueRadioButton.addActionListener(colorSelectionListener);
        greenRadioButton.addActionListener(colorSelectionListener);

        // --- クリアボタン ---
        JButton clearButton = new JButton("クリア");
        clearButton.addActionListener(e -> {
            drawingPanel.clearShapes();
        });

        // ツールバーにコンポーネントを配置
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JLabel("図形: "));
        toolBar.add(circleRadioButton);
        toolBar.add(rectangleRadioButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("色: "));
        toolBar.add(redRadioButton);
        toolBar.add(blueRadioButton);
        toolBar.add(greenRadioButton);
        toolBar.addSeparator();
        toolBar.add(clearButton);

        add(toolBar, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainFrame());
    }
} 