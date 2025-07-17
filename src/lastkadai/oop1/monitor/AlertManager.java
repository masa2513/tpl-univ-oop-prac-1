package oop1.monitor;

import java.awt.Color;
import java.awt.Toolkit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * アラート管理クラス 閾値監視とアラート通知を行う
 *
 * @author K24044
 */
public class AlertManager {

    private static AlertManager instance;
    private Settings settings;
    private List<AlertListener> alertListeners;
    private List<AlertLog> recentAlerts;

    private AlertManager() {
        this.settings = new Settings(); // デフォルト設定で初期化
        this.alertListeners = new ArrayList<>();
        this.recentAlerts = new ArrayList<>();
    }

    public static synchronized AlertManager getInstance() {
        if (instance == null) {
            instance = new AlertManager();
        }
        return instance;
    }

    /**
     * 設定を更新
     *
     * @param settings 新しい設定
     */
    public void updateSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * アラートリスナーを追加
     *
     * @param listener アラートリスナー
     */
    public void addAlertListener(AlertListener listener) {
        alertListeners.add(listener);
    }

    /**
     * アラートリスナーを削除
     *
     * @param listener アラートリスナー
     */
    public void removeAlertListener(AlertListener listener) {
        alertListeners.remove(listener);
    }

    /**
     * 監視データをチェックしてアラートを判定
     *
     * @param monitoringData 監視データ
     */
    public void checkAlerts(MonitoringData monitoringData) {
        // CPU使用率チェック
        checkCpuAlert(monitoringData.getCpuUsage());

        // メモリ使用率チェック
        checkMemoryAlert(monitoringData.getMemoryUsage());

        // ディスク使用率チェック
        checkDiskAlert(monitoringData.getDiskUsage());
    }

    private void checkCpuAlert(double cpuUsage) {
        AlertLevel alertLevel = getAlertLevel(cpuUsage,
                settings.getCpuWarningThreshold(),
                settings.getCpuCriticalThreshold());

        if (alertLevel != AlertLevel.NORMAL) {
            String message = String.format("CPU使用率が%sレベルです: %.1f%%",
                    alertLevel.getDisplayName(), cpuUsage);

            AlertLog alertLog = new AlertLog(AlertLog.AlertType.CPU,
                    alertLevel == AlertLevel.WARNING ? settings.getCpuWarningThreshold() : settings.getCpuCriticalThreshold(),
                    cpuUsage, message);

            triggerAlert(alertLog, alertLevel);
        }
    }

    private void checkMemoryAlert(double memoryUsage) {
        AlertLevel alertLevel = getAlertLevel(memoryUsage,
                settings.getMemoryWarningThreshold(),
                settings.getMemoryCriticalThreshold());

        if (alertLevel != AlertLevel.NORMAL) {
            String message = String.format("メモリ使用率が%sレベルです: %.1f%%",
                    alertLevel.getDisplayName(), memoryUsage);

            AlertLog alertLog = new AlertLog(AlertLog.AlertType.MEMORY,
                    alertLevel == AlertLevel.WARNING ? settings.getMemoryWarningThreshold() : settings.getMemoryCriticalThreshold(),
                    memoryUsage, message);

            triggerAlert(alertLog, alertLevel);
        }
    }

    private void checkDiskAlert(double diskUsage) {
        AlertLevel alertLevel = getAlertLevel(diskUsage,
                settings.getDiskWarningThreshold(),
                settings.getDiskCriticalThreshold());

        if (alertLevel != AlertLevel.NORMAL) {
            String message = String.format("ディスク使用率が%sレベルです: %.1f%%",
                    alertLevel.getDisplayName(), diskUsage);

            AlertLog alertLog = new AlertLog(AlertLog.AlertType.DISK,
                    alertLevel == AlertLevel.WARNING ? settings.getDiskWarningThreshold() : settings.getDiskCriticalThreshold(),
                    diskUsage, message);

            triggerAlert(alertLog, alertLevel);
        }
    }

    private AlertLevel getAlertLevel(double value, double warningThreshold, double criticalThreshold) {
        if (value >= criticalThreshold) {
            return AlertLevel.CRITICAL;
        } else if (value >= warningThreshold) {
            return AlertLevel.WARNING;
        } else {
            return AlertLevel.NORMAL;
        }
    }

    private void triggerAlert(AlertLog alertLog, AlertLevel alertLevel) {
        // 重複アラートを避けるため、最近のアラートをチェック
        if (isDuplicateAlert(alertLog)) {
            return;
        }

        // アラートログを記録
        recentAlerts.add(alertLog);

        // 古いアラートを削除（最新10件まで保持）
        if (recentAlerts.size() > 10) {
            recentAlerts.remove(0);
        }

        // 音声アラート
        if (settings.isSoundEnabled()) {
            playAlertSound();
        }

        // アラートリスナーに通知
        for (AlertListener listener : alertListeners) {
            listener.onAlert(alertLog, alertLevel);
        }

        System.out.println("ALERT: " + alertLog.getMessage());
    }

    private boolean isDuplicateAlert(AlertLog newAlert) {
        // 直近の同じタイプのアラートをチェック（1分以内）
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        return recentAlerts.stream()
                .filter(alert -> alert.getTimestamp().isAfter(oneMinuteAgo))
                .anyMatch(alert -> alert.getAlertType() == newAlert.getAlertType());
    }

    private void playAlertSound() {
        try {
            // システムビープ音を再生
            Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            System.err.println("アラート音の再生に失敗: " + e.getMessage());
        }
    }

    /**
     * 最近のアラートログを取得
     *
     * @return アラートログのリスト
     */
    public List<AlertLog> getRecentAlerts() {
        return new ArrayList<>(recentAlerts);
    }

    /**
     * アラートレベル列挙型
     */
    public enum AlertLevel {
        NORMAL("正常", Color.GREEN),
        WARNING("警告", Color.ORANGE),
        CRITICAL("危険", Color.RED);

        private final String displayName;
        private final Color color;

        AlertLevel(String displayName, Color color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Color getColor() {
            return color;
        }
    }

    /**
     * アラートリスナーインターフェース
     */
    public interface AlertListener {

        void onAlert(AlertLog alertLog, AlertLevel alertLevel);
    }
}
