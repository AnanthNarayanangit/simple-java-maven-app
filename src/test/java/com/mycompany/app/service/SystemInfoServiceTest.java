package com.mycompany.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SystemInfoService Tests")
class SystemInfoServiceTest {

    private SystemInfoService systemInfoService;

    @BeforeEach
    void setUp() {
        systemInfoService = new SystemInfoService();
    }

    @Test
    @DisplayName("Should return valid non-null SystemInfo record")
    void shouldReturnValidSystemInfo() {
        SystemInfoService.SystemInfo info = systemInfoService.getSystemInfo();

        assertNotNull(info);
        assertNotNull(info.osName());
        assertNotNull(info.osVersion());
        assertNotNull(info.osArch());
        assertTrue(info.availableProcessors() > 0);
        assertTrue(info.totalMemoryMb() > 0);
        assertNotNull(info.javaVersion());
        assertNotNull(info.jvmName());
    }

    @Test
    @DisplayName("Should format system information as non-empty report string")
    void shouldFormatSystemInfoString() {
        String report = systemInfoService.formatSystemInfo();

        assertNotNull(report);
        assertTrue(report.contains("SYSTEM & RUNTIME INFORMATION"));
        assertTrue(report.contains("OS Name:"));
        assertTrue(report.contains("Java Version:"));
        assertTrue(report.contains("Memory (Free/Total):"));
    }
}
