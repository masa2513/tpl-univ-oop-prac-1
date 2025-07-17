package oop1.monitor;

import java.time.LocalDateTime;

/**
 * アラートログモデルクラス
 *
 * @author K24044
 */
public class AlertLog {

    private long id;
    private LocalDateTime timestamp;
    private AlertType alertType;
    private double thresholdValue;
    private double actualValue;
    private String message;

    public enum AlertType {
        CPU("CPU"),
        MEMORY("メモリ"),
        DISK("ディスク"),
        NETWORK("ネットワーク");

        private final String displayName;

        AlertType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public AlertLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AlertLog(AlertType alertType, double thresholdValue, double actualValue, String message) {
        this();
        this.alertType = alertType;
        this.thresholdValue = thresholdValue;
        this.actualValue = actualValue;
        this.message = message;
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

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(double thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public double getActualValue() {
        return actualValue;
    }

    public void setActualValue(double actualValue) {
        this.actualValue = actualValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("AlertLog{timestamp=%s, type=%s, threshold=%.1f, actual=%.1f, message='%s'}",
                timestamp, alertType.getDisplayName(), thresholdValue, actualValue, message);
    }
}
