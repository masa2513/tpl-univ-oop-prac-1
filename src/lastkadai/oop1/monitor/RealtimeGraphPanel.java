package oop1.monitor;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * リアルタイムグラフ表示パネル JFreeChartを使用してリアルタイムで更新される時系列グラフを表示
 *
 * @author K24044
 */
public class RealtimeGraphPanel extends JPanel {

    private final TimeSeries timeSeries;
    private final RealtimeDataBuffer dataBuffer;
    private ChartPanel chartPanel;
    private final String title;
    private final String yAxisLabel;
    private final Color lineColor;

    /**
     * コンストラクタ
     *
     * @param title グラフタイトル
     * @param yAxisLabel Y軸ラベル
     * @param lineColor 線の色
     * @param maxDataPoints 最大データポイント数
     */
    public RealtimeGraphPanel(String title, String yAxisLabel, Color lineColor, int maxDataPoints) {
        this.title = title;
        this.yAxisLabel = yAxisLabel;
        this.lineColor = lineColor;
        this.dataBuffer = new RealtimeDataBuffer(maxDataPoints);

        // TimeSeries作成
        this.timeSeries = new TimeSeries(title);
        this.timeSeries.setMaximumItemCount(maxDataPoints);

        // グラフ設定
        setupGraph();

        // レイアウト設定
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * グラフ設定
     */
    private void setupGraph() {
        // データセット作成
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(timeSeries);

        // チャート作成
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title, // タイトル
                "時刻", // X軸ラベル
                yAxisLabel, // Y軸ラベル
                dataset, // データセット
                false, // 凡例表示
                true, // ツールチップ
                false // URL
        );

        // チャートの外観設定
        chart.setBackgroundPaint(Color.WHITE);

        // プロット設定
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // レンダラー設定
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, lineColor);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        // Y軸設定（0-100%）
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setAutoRange(false);

        // X軸設定
        DateAxis domainAxis = (DateAxis) plot.getDomainAxis();
        domainAxis.setAutoRange(true);
        domainAxis.setFixedAutoRange(60000 * 5); // 5分間表示

        // ChartPanel作成
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setMouseWheelEnabled(true);
    }

    /**
     * データを追加してグラフを更新
     *
     * @param value 値（0-100）
     */
    public void addData(double value) {
        LocalDateTime now = LocalDateTime.now();
        dataBuffer.addData(now, value);

        // JFreeChartのTimeSeriesに追加
        Date timestamp = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        timeSeries.addOrUpdate(new Millisecond(timestamp), value);
    }

    /**
     * グラフをクリア
     */
    public void clearGraph() {
        dataBuffer.clear();
        timeSeries.clear();
    }

    /**
     * データバッファを取得
     *
     * @return データバッファ
     */
    public RealtimeDataBuffer getDataBuffer() {
        return dataBuffer;
    }

    /**
     * 最新の値を取得
     *
     * @return 最新の値（データがない場合は0.0）
     */
    public double getLatestValue() {
        RealtimeDataBuffer.DataPoint latest = dataBuffer.getLatestData();
        return latest != null ? latest.getValue() : 0.0;
    }

    /**
     * 統計情報を取得
     *
     * @return 統計情報
     */
    public GraphStatistics getStatistics() {
        List<RealtimeDataBuffer.DataPoint> data = dataBuffer.getAllData();

        if (data.isEmpty()) {
            return new GraphStatistics(0.0, 0.0, 0.0, 0);
        }

        double sum = 0.0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (RealtimeDataBuffer.DataPoint point : data) {
            double value = point.getValue();
            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        double average = sum / data.size();

        return new GraphStatistics(average, min, max, data.size());
    }

    /**
     * グラフ統計情報クラス
     */
    public static class GraphStatistics {

        private final double average;
        private final double min;
        private final double max;
        private final int dataCount;

        public GraphStatistics(double average, double min, double max, int dataCount) {
            this.average = average;
            this.min = min;
            this.max = max;
            this.dataCount = dataCount;
        }

        public double getAverage() {
            return average;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public int getDataCount() {
            return dataCount;
        }
    }
}
