package oop1.monitor;

import java.time.LocalDateTime;

/**
 * 監視データモデルクラス
 *
 * @author K24044
 */
public class MonitoringData {

    private long id;
    private LocalDateTime timestamp;
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private long networkIn;
    private long networkOut;

    public MonitoringData() {
        this.timestamp = LocalDateTime.now();
    }

    public MonitoringData(double cpuUsage, double memoryUsage, double diskUsage,
            long networkIn, long networkOut) {
        this();
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.networkIn = networkIn;
        this.networkOut = networkOut;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public long getNetworkIn() {
        return networkIn;
    }

    public void setNetworkIn(long networkIn) {
        this.networkIn = networkIn;
    }

    public long getNetworkOut() {
        return networkOut;
    }

    public void setNetworkOut(long networkOut) {
        this.networkOut = networkOut;
    }

    @Override
    public String toString() {
        return String.format("MonitoringData{timestamp=%s, cpu=%.1f%%, memory=%.1f%%, disk=%.1f%%}",
                timestamp, cpuUsage, memoryUsage, diskUsage);
    }
}
