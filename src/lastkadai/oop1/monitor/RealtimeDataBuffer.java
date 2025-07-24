package oop1.monitor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * リアルタイムデータを保持するリングバッファ 指定された最大サイズを超えると古いデータから削除される
 *
 * @author K24044
 */
public class RealtimeDataBuffer {

    /**
     * データポイント
     */
    public static class DataPoint {

        private final LocalDateTime timestamp;
        private final double value;

        public DataPoint(LocalDateTime timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public double getValue() {
            return value;
        }
    }

    private final List<DataPoint> dataPoints;
    private final int maxSize;

    /**
     * コンストラクタ
     *
     * @param maxSize 最大保持データ数（60分間 = 3600秒分のデータを想定）
     */
    public RealtimeDataBuffer(int maxSize) {
        this.maxSize = maxSize;
        this.dataPoints = new ArrayList<>(maxSize);
    }

    /**
     * データを追加
     *
     * @param value 値
     */
    public synchronized void addData(double value) {
        addData(LocalDateTime.now(), value);
    }

    /**
     * データを追加
     *
     * @param timestamp タイムスタンプ
     * @param value 値
     */
    public synchronized void addData(LocalDateTime timestamp, double value) {
        dataPoints.add(new DataPoint(timestamp, value));

        // 最大サイズを超えた場合、古いデータを削除
        while (dataPoints.size() > maxSize) {
            dataPoints.remove(0);
        }
    }

    /**
     * 全データを取得
     *
     * @return データポイントのコピー
     */
    public synchronized List<DataPoint> getAllData() {
        return new ArrayList<>(dataPoints);
    }

    /**
     * 最新のデータを取得
     *
     * @return 最新データ（データがない場合はnull）
     */
    public synchronized DataPoint getLatestData() {
        if (dataPoints.isEmpty()) {
            return null;
        }
        return dataPoints.get(dataPoints.size() - 1);
    }

    /**
     * データをクリア
     */
    public synchronized void clear() {
        dataPoints.clear();
    }

    /**
     * データ数を取得
     *
     * @return データ数
     */
    public synchronized int size() {
        return dataPoints.size();
    }

    /**
     * 指定された時間範囲のデータを取得
     *
     * @param minutes 何分前からのデータを取得するか
     * @return 指定時間範囲のデータ
     */
    public synchronized List<DataPoint> getDataSince(int minutes) {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(minutes);
        List<DataPoint> result = new ArrayList<>();

        for (DataPoint point : dataPoints) {
            if (point.getTimestamp().isAfter(cutoffTime)) {
                result.add(point);
            }
        }

        return result;
    }
}
