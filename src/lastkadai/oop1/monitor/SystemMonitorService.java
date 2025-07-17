package oop1.monitor;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * システムリソース監視サービス JMXを使用してCPU、メモリ、ディスク使用率を取得
 *
 * @author K24044
 */
public class SystemMonitorService {

    private static SystemMonitorService instance;
    private OperatingSystemMXBean osBean;
    private MemoryMXBean memoryBean;

    private SystemMonitorService() {
        osBean = ManagementFactory.getOperatingSystemMXBean();
        memoryBean = ManagementFactory.getMemoryMXBean();
    }

    public static synchronized SystemMonitorService getInstance() {
        if (instance == null) {
            instance = new SystemMonitorService();
        }
        return instance;
    }

    /**
     * CPU使用率を取得（パーセント）
     *
     * @return CPU使用率（0-100）
     */
    public double getCpuUsage() {
        // Java 1.6以降でサポートされているメソッドを使用
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean
                    = (com.sun.management.OperatingSystemMXBean) osBean;
            double cpuLoad = sunOsBean.getProcessCpuLoad();
            if (cpuLoad >= 0) {
                return cpuLoad * 100.0;
            }
        }

        // フォールバック: 負荷平均から推定
        double loadAverage = osBean.getSystemLoadAverage();
        if (loadAverage >= 0) {
            int processors = osBean.getAvailableProcessors();
            return Math.min((loadAverage / processors) * 100.0, 100.0);
        }

        return 0.0;
    }

    /**
     * メモリ使用率を取得
     *
     * @return メモリ使用情報
     */
    public MemoryInfo getMemoryUsage() {
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();

        long totalUsed = heapMemory.getUsed() + nonHeapMemory.getUsed();
        long totalMax = heapMemory.getMax() + nonHeapMemory.getMax();

        // システム全体のメモリ情報を取得
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean
                    = (com.sun.management.OperatingSystemMXBean) osBean;

            long totalPhysicalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freePhysicalMemory = sunOsBean.getFreePhysicalMemorySize();
            long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;

            double usagePercentage = (double) usedPhysicalMemory / totalPhysicalMemory * 100.0;

            return new MemoryInfo(
                    totalPhysicalMemory,
                    usedPhysicalMemory,
                    freePhysicalMemory,
                    usagePercentage
            );
        }

        // フォールバック
        double usagePercentage = totalMax > 0 ? (double) totalUsed / totalMax * 100.0 : 0.0;
        return new MemoryInfo(totalMax, totalUsed, totalMax - totalUsed, usagePercentage);
    }

    /**
     * ディスク使用率を取得
     *
     * @return ディスク使用情報のリスト
     */
    public List<DiskInfo> getDiskUsage() {
        List<DiskInfo> diskInfoList = new ArrayList<>();

        File[] roots = File.listRoots();
        for (File root : roots) {
            try {
                long totalSpace = root.getTotalSpace();
                long freeSpace = root.getFreeSpace();
                long usedSpace = totalSpace - freeSpace;

                if (totalSpace > 0) {
                    double usagePercentage = (double) usedSpace / totalSpace * 100.0;

                    diskInfoList.add(new DiskInfo(
                            root.getPath(),
                            totalSpace,
                            usedSpace,
                            freeSpace,
                            usagePercentage
                    ));
                }
            } catch (Exception e) {
                System.err.println("ディスク情報の取得に失敗: " + root.getPath());
            }
        }

        return diskInfoList;
    }

    /**
     * システム情報を取得
     *
     * @return システム情報
     */
    public SystemInfo getSystemInfo() {
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String architecture = osBean.getArch();
        int processors = osBean.getAvailableProcessors();

        return new SystemInfo(osName, osVersion, architecture, processors);
    }

    /**
     * メモリ使用情報クラス
     */
    public static class MemoryInfo {

        private final long totalMemory;
        private final long usedMemory;
        private final long freeMemory;
        private final double usagePercentage;

        public MemoryInfo(long totalMemory, long usedMemory, long freeMemory, double usagePercentage) {
            this.totalMemory = totalMemory;
            this.usedMemory = usedMemory;
            this.freeMemory = freeMemory;
            this.usagePercentage = usagePercentage;
        }

        public long getTotalMemory() {
            return totalMemory;
        }

        public long getUsedMemory() {
            return usedMemory;
        }

        public long getFreeMemory() {
            return freeMemory;
        }

        public double getUsagePercentage() {
            return usagePercentage;
        }
    }

    /**
     * ディスク使用情報クラス
     */
    public static class DiskInfo {

        private final String path;
        private final long totalSpace;
        private final long usedSpace;
        private final long freeSpace;
        private final double usagePercentage;

        public DiskInfo(String path, long totalSpace, long usedSpace, long freeSpace, double usagePercentage) {
            this.path = path;
            this.totalSpace = totalSpace;
            this.usedSpace = usedSpace;
            this.freeSpace = freeSpace;
            this.usagePercentage = usagePercentage;
        }

        public String getPath() {
            return path;
        }

        public long getTotalSpace() {
            return totalSpace;
        }

        public long getUsedSpace() {
            return usedSpace;
        }

        public long getFreeSpace() {
            return freeSpace;
        }

        public double getUsagePercentage() {
            return usagePercentage;
        }
    }

    /**
     * システム情報クラス
     */
    public static class SystemInfo {

        private final String osName;
        private final String osVersion;
        private final String architecture;
        private final int processors;

        public SystemInfo(String osName, String osVersion, String architecture, int processors) {
            this.osName = osName;
            this.osVersion = osVersion;
            this.architecture = architecture;
            this.processors = processors;
        }

        public String getOsName() {
            return osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public String getArchitecture() {
            return architecture;
        }

        public int getProcessors() {
            return processors;
        }
    }
}
