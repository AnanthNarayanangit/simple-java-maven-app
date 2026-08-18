package com.mycompany.app.service;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * Service to inspect and report host OS, JVM runtime, and memory metrics.
 */
public class SystemInfoService {

    public record SystemInfo(
            String osName,
            String osVersion,
            String osArch,
            int availableProcessors,
            long totalMemoryMb,
            long freeMemoryMb,
            long maxMemoryMb,
            String javaVersion,
            String javaVendor,
            String jvmName,
            long uptimeSeconds
    ) {}

    public SystemInfo getSystemInfo() {
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        int mb = 1024 * 1024;
        return new SystemInfo(
                osBean.getName(),
                osBean.getVersion(),
                osBean.getArch(),
                osBean.getAvailableProcessors(),
                runtime.totalMemory() / mb,
                runtime.freeMemory() / mb,
                runtime.maxMemory() / mb,
                System.getProperty("java.version"),
                System.getProperty("java.vendor"),
                runtimeBean.getVmName(),
                runtimeBean.getUptime() / 1000
        );
    }

    public String formatSystemInfo() {
        SystemInfo info = getSystemInfo();
        return String.format("""
                ====================================================
                            SYSTEM & RUNTIME INFORMATION
                ====================================================
                OS Name:               %s
                OS Version:            %s
                OS Architecture:       %s
                Available Processors:  %d
                JVM Name:              %s
                Java Version:          %s (%s)
                Memory (Free/Total):   %d MB / %d MB (Max: %d MB)
                JVM Uptime:            %d seconds
                ====================================================""",
                info.osName(), info.osVersion(), info.osArch(),
                info.availableProcessors(), info.jvmName(),
                info.javaVersion(), info.javaVendor(),
                info.freeMemoryMb(), info.totalMemoryMb(), info.maxMemoryMb(),
                info.uptimeSeconds()
        );
    }
}
