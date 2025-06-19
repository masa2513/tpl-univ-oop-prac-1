package oop1.section09.kadai1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AichiTouristSpot {
    // 愛工大の緯度経度情報
    private static final double AIT_LATITUDE = 35.1834122;
    private static final double AIT_LONGITUDE = 137.1130419;
    
    // 出力ファイル名
    private static final String OUTPUT_FILE = "TouristSpot.csv";
    
    // データフォルダのパス
    private static final String DATA_FOLDER = "data";
    
    // CSVファイル名の配列
    private static final String[] CSV_FILES = {
        "c200326.csv", // ルートスポット
        "c200328.csv", // 寄ってこみゃ
        "c200329.csv", // 地域資源スポット（風景・自然）
        "c200330.csv", // 地域資源スポット（施設）
        "c200361.csv", // 建造物
        "c200362.csv", // 名勝
        "c200363.csv", // 天然記念物
        "c200364.csv"  // 史跡
    };
    
    public static void main(String[] args) {
        try {
            AichiTouristSpot processor = new AichiTouristSpot();
            processor.processData();
            System.out.println("データ処理が完了しました。出力ファイル: " + OUTPUT_FILE);
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void processData() throws IOException {
        List<TouristSpotData> allSpots = new ArrayList<>();
        
        // 各CSVファイルを処理
        for (String fileName : CSV_FILES) {
            System.out.println("処理中: " + fileName);
            List<TouristSpotData> spots = readCsvFile(fileName);
            allSpots.addAll(spots);
            System.out.println(fileName + " から " + spots.size() + " 件のデータを読み込みました");
        }
        
        // 愛工大からの距離で昇順ソート
        allSpots.sort(Comparator.comparingDouble(TouristSpotData::getDistanceFromAIT));
        
        // 結果をCSVファイルに出力
        writeOutputCsv(allSpots);
        
        System.out.println("総データ件数: " + allSpots.size());
    }
    
    private List<TouristSpotData> readCsvFile(String fileName) throws IOException {
        List<TouristSpotData> spots = new ArrayList<>();
        
        // dataフォルダ内のファイルパスを構築
        String filePath = DATA_FOLDER + File.separator + fileName;
        
        // Shift-JIS(MS932)でファイルを読み込み
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), Charset.forName("MS932")))) {
            
            String line;
            String[] headers = null;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    headers = parseCsvLine(line);
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = parseCsvLine(line);
                if (values.length < headers.length) {
                    continue; // 不完全な行はスキップ
                }
                
                TouristSpotData spot = extractSpotData(headers, values);
                if (spot != null) {
                    spots.add(spot);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ファイルが見つかりません: " + filePath);
            System.err.println("CSVファイルを " + DATA_FOLDER + " フォルダに配置してください。");
        } catch (Exception e) {
            System.err.println("ファイル読み込みエラー (" + filePath + "): " + e.getMessage());
        }
        
        return spots;
    }
    
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentValue = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(currentValue.toString().trim());
                currentValue = new StringBuilder();
            } else {
                currentValue.append(c);
            }
        }
        
        values.add(currentValue.toString().trim());
        return values.toArray(new String[0]);
    }
    
    private TouristSpotData extractSpotData(String[] headers, String[] values) {
        String name = null;
        String coordinates = null;
        
        // ヘッダーから必要な列を特定
        for (int i = 0; i < headers.length && i < values.length; i++) {
            String header = headers[i].toLowerCase();
            String value = values[i];
            
            // 名称を取得（複数の可能性のある列名をチェック）
            if (name == null && (header.contains("名称") || header.contains("施設名") || 
                               header.contains("スポット名") || header.contains("文化財名"))) {
                name = value;
            }
            
            // 座標情報を取得
            if (coordinates == null && (header.contains("形状") || header.contains("座標") || 
                                      header.contains("point") || value.startsWith("POINT("))) {
                coordinates = value;
            }
        }
        
        if (name != null && coordinates != null && !name.isEmpty() && !coordinates.isEmpty()) {
            double[] latLon = parseCoordinates(coordinates);
            if (latLon != null) {
                double distance = calculateDistance(latLon[0], latLon[1], AIT_LATITUDE, AIT_LONGITUDE);
                return new TouristSpotData(latLon[0], latLon[1], distance, name);
            }
        }
        
        return null;
    }
    
    private double[] parseCoordinates(String coordinates) {
        // POINT(経度 緯度) 形式から緯度経度を抽出
        Pattern pattern = Pattern.compile("POINT\\(([0-9.-]+)\\s+([0-9.-]+)\\)");
        Matcher matcher = pattern.matcher(coordinates);
        
        if (matcher.find()) {
            try {
                double longitude = Double.parseDouble(matcher.group(1));
                double latitude = Double.parseDouble(matcher.group(2));
                return new double[]{latitude, longitude};
            } catch (NumberFormatException e) {
                System.err.println("座標解析エラー: " + coordinates);
            }
        }
        
        return null;
    }
    
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 単純な2点間の距離計算（ユークリッド距離）
        double deltaLat = lat1 - lat2;
        double deltaLon = lon1 - lon2;
        return Math.sqrt(deltaLat * deltaLat + deltaLon * deltaLon);
    }
    
    private void writeOutputCsv(List<TouristSpotData> spots) throws IOException {
        // 既存ファイルがあれば削除
        File outputFile = new File(OUTPUT_FILE);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE, Charset.forName("UTF-8")))) {
            // ヘッダー行を出力
            writer.println("緯度情報,経度情報,愛工大からの距離,データ名");
            
            // データ行を出力
            for (TouristSpotData spot : spots) {
                writer.printf("%.10f,%.10f,%.10f,%s%n",
                    spot.getLatitude(),
                    spot.getLongitude(),
                    spot.getDistanceFromAIT(),
                    spot.getName()
                );
            }
        }
    }
    
    // 観光スポットデータを格納するクラス
    private static class TouristSpotData {
        private final double latitude;
        private final double longitude;
        private final double distanceFromAIT;
        private final String name;
        
        public TouristSpotData(double latitude, double longitude, double distanceFromAIT, String name) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.distanceFromAIT = distanceFromAIT;
            this.name = name;
        }
        
        public double getLatitude() {
            return latitude;
        }
        
        public double getLongitude() {
            return longitude;
        }
        
        public double getDistanceFromAIT() {
            return distanceFromAIT;
        }
        
        public String getName() {
            return name;
        }
    }
} 