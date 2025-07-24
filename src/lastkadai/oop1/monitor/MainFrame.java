package oop1.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * システム監視ツールのメインウィンドウ
 *
 * @author K24044
 */
public class MainFrame extends JFrame implements AlertManager.AlertListener {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.0");

    private SystemMonitorService monitorService;
    private AlertManager alertManager;
    private Timer updateTimer;

    // UI コンポーネント
    private JButton startStopButton;
    private JButton logButton;

    // リソース表示エリア
    private JProgressBar cpuProgressBar;
    private JLabel cpuLabel;
    private JProgressBar memoryProgressBar;
    private JLabel memoryLabel;
    private JProgressBar diskProgressBar;
    private JLabel diskLabel;

    // リアルタイムグラフ
    private RealtimeGraphPanel cpuGraphPanel;
    private RealtimeGraphPanel memoryGraphPanel;
    private RealtimeGraphPanel diskGraphPanel;

    // ステータスバー
    private JLabel statusLabel;
    private JLabel uptimeLabel;
    private JLabel lastUpdateLabel;

    private boolean isMonitoring = false;
    private long startTime;

    public MainFrame() {
        monitorService = SystemMonitorService.getInstance();
        alertManager = AlertManager.getInstance();
        alertManager.addAlertListener(this);
        initializeComponents();
        setupLayout();
        setupEvents();
        updateSystemInfo();
    }

    private void initializeComponents() {
        setTitle("システム監視ツール");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // ツールバーボタン
        startStopButton = new JButton("監視開始");
        logButton = new JButton("ログ");

        // CPU表示
        cpuProgressBar = new JProgressBar(0, 100);
        cpuProgressBar.setStringPainted(true);
        cpuLabel = new JLabel("CPU使用率: 0.0%");

        // メモリ表示
        memoryProgressBar = new JProgressBar(0, 100);
        memoryProgressBar.setStringPainted(true);
        memoryLabel = new JLabel("メモリ使用率: 0.0%");

        // ディスク表示（最初のディスクのみ表示）
        diskProgressBar = new JProgressBar(0, 100);
        diskProgressBar.setStringPainted(true);
        diskLabel = new JLabel("ディスク使用率: 0.0%");

        // ステータスバー
        statusLabel = new JLabel("監視停止中");
        uptimeLabel = new JLabel("稼働時間: 00:00:00");
        lastUpdateLabel = new JLabel("最終更新: --:--:--");

        // リアルタイムグラフパネル初期化
        initializeGraphPanels();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // ツールバー
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.add(startStopButton);
        toolbarPanel.add(logButton);
        add(toolbarPanel, BorderLayout.NORTH);

        // 中央エリア
        JPanel centerPanel = new JPanel(new BorderLayout());

        // 左側: リソース表示エリア
        JPanel resourcePanel = createResourcePanel();
        resourcePanel.setPreferredSize(new Dimension(350, 0));

        // 右側: グラフ表示エリア（とりあえずプレースホルダー）
        JPanel graphPanel = createGraphPanel();

        // 分割パネル
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, resourcePanel, graphPanel);
        splitPane.setDividerLocation(350);
        splitPane.setResizeWeight(0.4);

        centerPanel.add(splitPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ステータスバー
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createResourcePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("システムリソース"));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // CPU使用率
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("CPU使用率"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(cpuProgressBar, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panel.add(cpuLabel, gbc);

        // メモリ使用率
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("メモリ使用率"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(memoryProgressBar, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panel.add(memoryLabel, gbc);

        // ディスク使用率
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(new JLabel("ディスク使用率"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(diskProgressBar, gbc);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        panel.add(diskLabel, gbc);

        return panel;
    }

    private void initializeGraphPanels() {
        // 5分間のデータを保持（300秒 = 300データポイント）
        int maxDataPoints = 300;

        cpuGraphPanel = new RealtimeGraphPanel(
                "CPU使用率", "使用率 (%)", Color.BLUE, maxDataPoints);
        memoryGraphPanel = new RealtimeGraphPanel(
                "メモリ使用率", "使用率 (%)", Color.GREEN, maxDataPoints);
        diskGraphPanel = new RealtimeGraphPanel(
                "ディスク使用率", "使用率 (%)", Color.ORANGE, maxDataPoints);
    }

    private JPanel createGraphPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("リアルタイムグラフ"));

        // タブパネルにリアルタイムグラフを追加
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("CPU", cpuGraphPanel);
        tabbedPane.addTab("メモリ", memoryGraphPanel);
        tabbedPane.addTab("ディスク", diskGraphPanel);

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(statusLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(uptimeLabel);
        rightPanel.add(new JLabel(" | "));
        rightPanel.add(lastUpdateLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void setupEvents() {
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMonitoring) {
                    stopMonitoring();
                } else {
                    startMonitoring();
                }
            }
        });

        logButton.addActionListener(e -> {
            try {
                String filePath = CSVExporter.exportCurrentStatus(null);
                JOptionPane.showMessageDialog(this,
                        "現在のシステム状況をCSVファイルに出力しました:\n" + filePath,
                        "CSV出力完了",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "CSV出力に失敗しました:\n" + ex.getMessage(),
                        "エラー",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void startMonitoring() {
        isMonitoring = true;
        startTime = System.currentTimeMillis();
        startStopButton.setText("監視停止");
        statusLabel.setText("監視中");

        // 1秒間隔でタイマー開始
        updateTimer = new Timer(1000, e -> updateSystemInfo());
        updateTimer.start();
    }

    private void stopMonitoring() {
        isMonitoring = false;
        startStopButton.setText("監視開始");
        statusLabel.setText("監視停止中");

        if (updateTimer != null) {
            updateTimer.stop();
        }

        // グラフをクリア
        if (cpuGraphPanel != null) {
            cpuGraphPanel.clearGraph();
        }
        if (memoryGraphPanel != null) {
            memoryGraphPanel.clearGraph();
        }
        if (diskGraphPanel != null) {
            diskGraphPanel.clearGraph();
        }
    }

    private void updateSystemInfo() {
        try {
            // CPU使用率更新
            double cpuUsage = monitorService.getCpuUsage();
            cpuProgressBar.setValue((int) Math.round(cpuUsage));
            cpuProgressBar.setString(DECIMAL_FORMAT.format(cpuUsage) + "%");
            cpuLabel.setText("CPU使用率: " + DECIMAL_FORMAT.format(cpuUsage) + "%");

            // プログレスバーの色を使用率に応じて変更
            cpuProgressBar.setForeground(getUsageColor(cpuUsage));

            // リアルタイムグラフにデータ追加
            if (cpuGraphPanel != null) {
                cpuGraphPanel.addData(cpuUsage);
            }

            // メモリ使用率更新
            SystemMonitorService.MemoryInfo memoryInfo = monitorService.getMemoryUsage();
            double memoryUsage = memoryInfo.getUsagePercentage();
            memoryProgressBar.setValue((int) Math.round(memoryUsage));
            memoryProgressBar.setString(DECIMAL_FORMAT.format(memoryUsage) + "%");
            memoryLabel.setText(String.format("メモリ使用率: %s%% (%s MB / %s MB)",
                    DECIMAL_FORMAT.format(memoryUsage),
                    formatBytes(memoryInfo.getUsedMemory()),
                    formatBytes(memoryInfo.getTotalMemory())));
            memoryProgressBar.setForeground(getUsageColor(memoryUsage));

            // リアルタイムグラフにデータ追加
            if (memoryGraphPanel != null) {
                memoryGraphPanel.addData(memoryUsage);
            }

            // ディスク使用率更新（最初のディスクのみ）
            double diskUsage = 0.0;
            List<SystemMonitorService.DiskInfo> diskInfos = monitorService.getDiskUsage();
            if (!diskInfos.isEmpty()) {
                SystemMonitorService.DiskInfo diskInfo = diskInfos.get(0);
                diskUsage = diskInfo.getUsagePercentage();
                diskProgressBar.setValue((int) Math.round(diskUsage));
                diskProgressBar.setString(DECIMAL_FORMAT.format(diskUsage) + "%");
                diskLabel.setText(String.format("ディスク使用率 (%s): %s%% (%s GB / %s GB)",
                        diskInfo.getPath(),
                        DECIMAL_FORMAT.format(diskUsage),
                        formatBytes(diskInfo.getUsedSpace()),
                        formatBytes(diskInfo.getTotalSpace())));
                diskProgressBar.setForeground(getUsageColor(diskUsage));

                // リアルタイムグラフにデータ追加
                if (diskGraphPanel != null) {
                    diskGraphPanel.addData(diskUsage);
                }
            }

            // 監視データを作成してアラートチェック
            if (isMonitoring) {
                MonitoringData monitoringData = new MonitoringData(cpuUsage, memoryUsage, diskUsage, 0, 0);
                alertManager.checkAlerts(monitoringData);
            }

            // ステータスバー更新
            if (isMonitoring) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                uptimeLabel.setText("稼働時間: " + formatElapsedTime(elapsedTime));
            }
            lastUpdateLabel.setText("最終更新: " + new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date()));

        } catch (Exception e) {
            System.err.println("システム情報の更新に失敗: " + e.getMessage());
        }
    }

    private Color getUsageColor(double usage) {
        if (usage >= 90) {
            return Color.RED;
        } else if (usage >= 70) {
            return Color.ORANGE;
        } else {
            return Color.GREEN;
        }
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        }
        if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024));
        }
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String formatElapsedTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * アラート発生時のコールバック
     *
     * @param alertLog アラートログ
     * @param alertLevel アラートレベル
     */
    @Override
    public void onAlert(AlertLog alertLog, AlertManager.AlertLevel alertLevel) {
        // アラート発生時の処理
        SwingUtilities.invokeLater(() -> {
            // ステータスバーにアラート表示
            statusLabel.setText("アラート: " + alertLog.getMessage());
            statusLabel.setForeground(alertLevel.getColor());

            // 一定時間後に元に戻す
            Timer resetTimer = new Timer(5000, e -> {
                statusLabel.setText(isMonitoring ? "監視中" : "監視停止中");
                statusLabel.setForeground(Color.BLACK);
            });
            resetTimer.setRepeats(false);
            resetTimer.start();
        });
    }
}
