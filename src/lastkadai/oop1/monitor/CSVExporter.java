package oop1.monitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CSV出力機能を提供するクラス
 *
 * @author K24044
 */
public class CSVExporter {

    private static final String CSV_HEADER = "timestamp,cpu_usage,memory_usage,disk_usage,network_in,network_out";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 監視データをCSVファイルに出力
     *
     * @param data 監視データのリスト
     * @param fileName 出力ファイル名（nullの場合は自動生成）
     * @return 出力したファイルパス
     * @throws IOException ファイル出力エラー
     */
    public static String exportMonitoringData(List<MonitoringData> data, String fileName) throws IOException {
        if (fileName == null) {
            fileName = generateFileName();
        }

        // logsディレクトリを作成
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File outputFile = new File(logsDir, fileName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            // ヘッダー行を書き込み
            writer.write(CSV_HEADER);
            writer.write("\n");

            // データ行を書き込み
            for (MonitoringData monitoringData : data) {
                writer.write(formatMonitoringDataToCsv(monitoringData));
                writer.write("\n");
            }
        }

        return outputFile.getAbsolutePath();
    }

    /**
     * アラートログをCSVファイルに出力
     *
     * @param alerts アラートログのリスト
     * @param fileName 出力ファイル名（nullの場合は自動生成）
     * @return 出力したファイルパス
     * @throws IOException ファイル出力エラー
     */
    public static String exportAlertLogs(List<AlertLog> alerts, String fileName) throws IOException {
        if (fileName == null) {
            fileName = "alert_logs_" + LocalDateTime.now().format(FILE_DATE_FORMAT) + ".csv";
        }

        // logsディレクトリを作成
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File outputFile = new File(logsDir, fileName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            // ヘッダー行を書き込み
            writer.write("timestamp,alert_type,threshold_value,actual_value,message");
            writer.write("\n");

            // データ行を書き込み
            for (AlertLog alertLog : alerts) {
                writer.write(formatAlertLogToCsv(alertLog));
                writer.write("\n");
            }
        }

        return outputFile.getAbsolutePath();
    }

    /**
     * 現在のシステム状況をCSVファイルに出力
     *
     * @param fileName 出力ファイル名（nullの場合は自動生成）
     * @return 出力したファイルパス
     * @throws IOException ファイル出力エラー
     */
    public static String exportCurrentStatus(String fileName) throws IOException {
        SystemMonitorService monitorService = SystemMonitorService.getInstance();

        double cpuUsage = monitorService.getCpuUsage();
        SystemMonitorService.MemoryInfo memoryInfo = monitorService.getMemoryUsage();
        List<SystemMonitorService.DiskInfo> diskInfos = monitorService.getDiskUsage();
        double diskUsage = diskInfos.isEmpty() ? 0.0 : diskInfos.get(0).getUsagePercentage();

        MonitoringData currentData = new MonitoringData(cpuUsage, memoryInfo.getUsagePercentage(), diskUsage, 0, 0);

        if (fileName == null) {
            fileName = "current_status_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        }

        // logsディレクトリを作成
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File outputFile = new File(logsDir, fileName);

        try (FileWriter writer = new FileWriter(outputFile)) {
            // ヘッダー行を書き込み
            writer.write(CSV_HEADER);
            writer.write("\n");

            // 現在のデータを書き込み
            writer.write(formatMonitoringDataToCsv(currentData));
            writer.write("\n");

            // システム情報も追加
            writer.write("\n");
            writer.write("# System Information\n");
            SystemMonitorService.SystemInfo systemInfo = monitorService.getSystemInfo();
            writer.write(String.format("# OS: %s %s (%s)\n",
                    systemInfo.getOsName(),
                    systemInfo.getOsVersion(),
                    systemInfo.getArchitecture()));
            writer.write(String.format("# Processors: %d\n", systemInfo.getProcessors()));

            if (!diskInfos.isEmpty()) {
                writer.write("# Disk Information\n");
                for (SystemMonitorService.DiskInfo diskInfo : diskInfos) {
                    writer.write(String.format("# Disk %s: %.1f%% used (%s / %s)\n",
                            diskInfo.getPath(),
                            diskInfo.getUsagePercentage(),
                            formatBytes(diskInfo.getUsedSpace()),
                            formatBytes(diskInfo.getTotalSpace())));
                }
            }
        }

        return outputFile.getAbsolutePath();
    }

    private static String generateFileName() {
        return "monitoring_" + LocalDateTime.now().format(FILE_DATE_FORMAT) + ".csv";
    }

    private static String formatMonitoringDataToCsv(MonitoringData data) {
        return String.format("%s,%.2f,%.2f,%.2f,%d,%d",
                data.getTimestamp().format(TIMESTAMP_FORMAT),
                data.getCpuUsage(),
                data.getMemoryUsage(),
                data.getDiskUsage(),
                data.getNetworkIn(),
                data.getNetworkOut());
    }

    private static String formatAlertLogToCsv(AlertLog alertLog) {
        return String.format("%s,%s,%.2f,%.2f,\"%s\"",
                alertLog.getTimestamp().format(TIMESTAMP_FORMAT),
                alertLog.getAlertType().name(),
                alertLog.getThresholdValue(),
                alertLog.getActualValue(),
                alertLog.getMessage().replace("\"", "\"\""));
    }

    private static String formatBytes(long bytes) {
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
}
