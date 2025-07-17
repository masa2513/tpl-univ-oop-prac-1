package oop1.monitor;

/**
 * 設定情報モデルクラス
 *
 * @author K24044
 */
public class Settings {

    // 監視設定
    private int monitoringInterval = 1000; // ミリ秒
    private int dataRetentionDays = 30;

    // 閾値設定
    private double cpuWarningThreshold = 70.0;
    private double cpuCriticalThreshold = 90.0;
    private double memoryWarningThreshold = 80.0;
    private double memoryCriticalThreshold = 95.0;
    private double diskWarningThreshold = 80.0;
    private double diskCriticalThreshold = 95.0;

    // 表示設定
    private boolean showCpuGraph = true;
    private boolean showMemoryGraph = true;
    private boolean showDiskGraph = true;
    private int graphTimeRange = 60; // 分

    // アラート設定
    private boolean soundEnabled = true;
    private boolean visualEnabled = true;

    public Settings() {
        // デフォルトコンストラクタ
    }

    // 監視設定
    public int getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(int monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    public int getDataRetentionDays() {
        return dataRetentionDays;
    }

    public void setDataRetentionDays(int dataRetentionDays) {
        this.dataRetentionDays = dataRetentionDays;
    }

    // CPU閾値設定
    public double getCpuWarningThreshold() {
        return cpuWarningThreshold;
    }

    public void setCpuWarningThreshold(double cpuWarningThreshold) {
        this.cpuWarningThreshold = cpuWarningThreshold;
    }

    public double getCpuCriticalThreshold() {
        return cpuCriticalThreshold;
    }

    public void setCpuCriticalThreshold(double cpuCriticalThreshold) {
        this.cpuCriticalThreshold = cpuCriticalThreshold;
    }

    // メモリ閾値設定
    public double getMemoryWarningThreshold() {
        return memoryWarningThreshold;
    }

    public void setMemoryWarningThreshold(double memoryWarningThreshold) {
        this.memoryWarningThreshold = memoryWarningThreshold;
    }

    public double getMemoryCriticalThreshold() {
        return memoryCriticalThreshold;
    }

    public void setMemoryCriticalThreshold(double memoryCriticalThreshold) {
        this.memoryCriticalThreshold = memoryCriticalThreshold;
    }

    // ディスク閾値設定
    public double getDiskWarningThreshold() {
        return diskWarningThreshold;
    }

    public void setDiskWarningThreshold(double diskWarningThreshold) {
        this.diskWarningThreshold = diskWarningThreshold;
    }

    public double getDiskCriticalThreshold() {
        return diskCriticalThreshold;
    }

    public void setDiskCriticalThreshold(double diskCriticalThreshold) {
        this.diskCriticalThreshold = diskCriticalThreshold;
    }

    // 表示設定
    public boolean isShowCpuGraph() {
        return showCpuGraph;
    }

    public void setShowCpuGraph(boolean showCpuGraph) {
        this.showCpuGraph = showCpuGraph;
    }

    public boolean isShowMemoryGraph() {
        return showMemoryGraph;
    }

    public void setShowMemoryGraph(boolean showMemoryGraph) {
        this.showMemoryGraph = showMemoryGraph;
    }

    public boolean isShowDiskGraph() {
        return showDiskGraph;
    }

    public void setShowDiskGraph(boolean showDiskGraph) {
        this.showDiskGraph = showDiskGraph;
    }

    public int getGraphTimeRange() {
        return graphTimeRange;
    }

    public void setGraphTimeRange(int graphTimeRange) {
        this.graphTimeRange = graphTimeRange;
    }

    // アラート設定
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    public boolean isVisualEnabled() {
        return visualEnabled;
    }

    public void setVisualEnabled(boolean visualEnabled) {
        this.visualEnabled = visualEnabled;
    }

    @Override
    public String toString() {
        return String.format("Settings{interval=%dms, retention=%dd, cpu=[%.1f,%.1f], memory=[%.1f,%.1f], disk=[%.1f,%.1f]}",
                monitoringInterval, dataRetentionDays,
                cpuWarningThreshold, cpuCriticalThreshold,
                memoryWarningThreshold, memoryCriticalThreshold,
                diskWarningThreshold, diskCriticalThreshold);
    }
}
