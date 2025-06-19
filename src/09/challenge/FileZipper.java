package oop1.section09.challenge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ファイルをZIP圧縮するためのユーティリティクラス
 * 
 * @author K24044KK
 */
public class FileZipper {
    
    private static final String DEFAULT_ZIP_NAME = "archive";
    private static final String ZIP_EXTENSION = ".zip";
    private static final int BUFFER_SIZE = 8192;
    
    /**
     * 指定されたファイルリストをZIP圧縮する
     * 
     * @param files 圧縮するファイルのリスト
     * @return 作成されたZIPファイル
     * @throws IOException 圧縮処理で例外が発生した場合
     */
    public File compressFiles(List<File> files) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("圧縮するファイルが指定されていません");
        }
        
        // 最初のファイルの親ディレクトリを取得
        File firstFile = files.get(0);
        File parentDir = firstFile.getParentFile();
        if (parentDir == null) {
            parentDir = new File(".");
        }
        
        // 重複しないZIPファイル名を生成
        File zipFile = generateUniqueZipFileName(parentDir);
        
        // ZIP圧縮実行
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            for (File file : files) {
                if (file.exists()) {
                    addToZip(zos, file, file.getName());
                } else {
                    System.err.println("Warning: File not found: " + file.getAbsolutePath());
                }
            }
        }
        
        return zipFile;
    }
    
    /**
     * 重複しないZIPファイル名を生成する
     * 
     * @param parentDir 保存先ディレクトリ
     * @return 重複しないZIPファイル
     */
    private File generateUniqueZipFileName(File parentDir) {
        File zipFile = new File(parentDir, DEFAULT_ZIP_NAME + ZIP_EXTENSION);
        
        // ファイルが存在しない場合はそのまま返す
        if (!zipFile.exists()) {
            return zipFile;
        }
        
        // ファイルが存在する場合は連番を付けて重複を回避
        int counter = 1;
        while (zipFile.exists()) {
            String fileName = DEFAULT_ZIP_NAME + "_" + counter + ZIP_EXTENSION;
            zipFile = new File(parentDir, fileName);
            counter++;
        }
        
        return zipFile;
    }
    
    /**
     * ファイルまたはディレクトリをZIPに追加する
     * 
     * @param zos ZipOutputStream
     * @param file 追加するファイルまたはディレクトリ
     * @param entryName ZIP内でのエントリ名
     * @throws IOException 追加処理で例外が発生した場合
     */
    private void addToZip(ZipOutputStream zos, File file, String entryName) throws IOException {
        if (file.isDirectory()) {
            // ディレクトリの場合
            addDirectoryToZip(zos, file, entryName);
        } else {
            // ファイルの場合
            addFileToZip(zos, file, entryName);
        }
    }
    
    /**
     * ディレクトリをZIPに追加する（再帰的）
     * 
     * @param zos ZipOutputStream
     * @param directory 追加するディレクトリ
     * @param baseName ベース名
     * @throws IOException 追加処理で例外が発生した場合
     */
    private void addDirectoryToZip(ZipOutputStream zos, File directory, String baseName) throws IOException {
        // ディレクトリエントリを追加（末尾にスラッシュが必要）
        if (!baseName.endsWith("/")) {
            baseName += "/";
        }
        
        ZipEntry dirEntry = new ZipEntry(baseName);
        dirEntry.setTime(directory.lastModified());
        zos.putNextEntry(dirEntry);
        zos.closeEntry();
        
        // ディレクトリ内のファイルとサブディレクトリを再帰的に追加
        File[] children = directory.listFiles();
        if (children != null) {
            for (File child : children) {
                String childEntryName = baseName + child.getName();
                addToZip(zos, child, childEntryName);
            }
        }
    }
    
    /**
     * ファイルをZIPに追加する
     * 
     * @param zos ZipOutputStream
     * @param file 追加するファイル
     * @param entryName エントリ名
     * @throws IOException 追加処理で例外が発生した場合
     */
    private void addFileToZip(ZipOutputStream zos, File file, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        entry.setTime(file.lastModified());
        entry.setSize(file.length());
        
        zos.putNextEntry(entry);
        
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
        }
        
        zos.closeEntry();
    }
    
    /**
     * ファイルサイズを人間が読みやすい形式で取得する
     * 
     * @param bytes バイト数
     * @return 読みやすい形式の文字列
     */
    public static String getReadableFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes;
        
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024.0;
            unitIndex++;
        }
        
        return String.format("%.2f %s", size, units[unitIndex]);
    }
    
    /**
     * 指定されたファイルリストの総サイズを計算する
     * 
     * @param files ファイルリスト
     * @return 総サイズ（バイト）
     */
    public static long calculateTotalSize(List<File> files) {
        long totalSize = 0;
        
        for (File file : files) {
            totalSize += getFileSize(file);
        }
        
        return totalSize;
    }
    
    /**
     * ファイルまたはディレクトリのサイズを取得する（再帰的）
     * 
     * @param file ファイルまたはディレクトリ
     * @return サイズ（バイト）
     */
    private static long getFileSize(File file) {
        if (file.isFile()) {
            return file.length();
        } else if (file.isDirectory()) {
            long size = 0;
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    size += getFileSize(child);
                }
            }
            return size;
        }
        return 0;
    }
} 