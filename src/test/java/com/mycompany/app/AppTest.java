package com.mycompany.app;

import com.mycompany.app.model.AppConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("App Main and CLI Tests")
public class AppTest {

    private App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Nested
    @DisplayName("Backward Compatibility Tests")
    class BackwardCompatibilityTests {

        @Test
        @DisplayName("Legacy testAppConstructor compatibility")
        public void testAppConstructor() {
            App app1 = new App();
            App app2 = new App();
            assertEquals(app1.getMessage(), app2.getMessage());
        }

        @Test
        @DisplayName("Legacy testAppMessage compatibility")
        public void testAppMessage() {
            App app = new App();
            assertEquals("Hello World!", app.getMessage());
        }
    }

    @Nested
    @DisplayName("CLI Argument Parsing & Dispatching Tests")
    class CliArgumentParsingTests {

        @Test
        @DisplayName("Should parse default config when no args provided")
        void shouldHandleNoArguments() {
            AppConfig config = app.parseArguments(new String[]{});
            assertEquals(AppConfig.Mode.DEFAULT, config.mode());
            assertEquals("Hello World!", app.execute(config));
        }

        @Test
        @DisplayName("Should handle null args array")
        void shouldHandleNullArguments() {
            AppConfig config = app.parseArguments(null);
            assertEquals(AppConfig.Mode.DEFAULT, config.mode());
            assertEquals("Hello World!", app.execute(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"--help", "-h"})
        @DisplayName("Should display help message on --help or -h")
        void shouldHandleHelpOption(String flag) {
            AppConfig config = app.parseArguments(new String[]{flag});
            assertEquals(AppConfig.Mode.HELP, config.mode());
            String output = app.execute(config);
            assertTrue(output.contains("Usage:"));
            assertTrue(output.contains("--help"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"--version", "-v"})
        @DisplayName("Should display version on --version or -v")
        void shouldHandleVersionOption(String flag) {
            AppConfig config = app.parseArguments(new String[]{flag});
            assertEquals(AppConfig.Mode.VERSION, config.mode());
            String output = app.execute(config);
            assertTrue(output.contains("Simple Java Maven App v" + App.APP_VERSION));
        }

        @ParameterizedTest
        @ValueSource(strings = {"--sysinfo", "-s"})
        @DisplayName("Should display system info on --sysinfo or -s")
        void shouldHandleSysInfoOption(String flag) {
            AppConfig config = app.parseArguments(new String[]{flag});
            assertEquals(AppConfig.Mode.SYSINFO, config.mode());
            String output = app.execute(config);
            assertTrue(output.contains("SYSTEM & RUNTIME INFORMATION"));
        }

        @Test
        @DisplayName("Should greet named user via --name")
        void shouldHandleNameOption() {
            AppConfig config = app.parseArguments(new String[]{"--name", "Developer"});
            assertEquals(AppConfig.Mode.GREET, config.mode());
            assertEquals("Hello Developer!", app.execute(config));
        }

        @Test
        @DisplayName("Should handle single plain string argument as name")
        void shouldHandlePlainNameArgument() {
            AppConfig config = app.parseArguments(new String[]{"Jenkins"});
            assertEquals(AppConfig.Mode.GREET, config.mode());
            assertEquals("Hello Jenkins!", app.execute(config));
        }

        @Test
        @DisplayName("Should evaluate math expression via --calc")
        void shouldHandleCalcOption() {
            AppConfig config = app.parseArguments(new String[]{"--calc", "25", "*", "4"});
            assertEquals(AppConfig.Mode.CALCULATE, config.mode());
            String output = app.execute(config);
            assertEquals("Expression: 25 * 4 = 100", output);
        }

        @Test
        @DisplayName("Should handle calculation errors gracefully")
        void shouldHandleCalcErrorsGracefully() {
            AppConfig config = app.parseArguments(new String[]{"--calc", "10 / 0"});
            String output = app.execute(config);
            assertTrue(output.startsWith("Calculation error:"));
        }

        @Test
        @DisplayName("Should compute statistics via --stats")
        void shouldHandleStatsOption() {
            AppConfig config = app.parseArguments(new String[]{"--stats", "10,20,30,40"});
            assertEquals(AppConfig.Mode.STATS, config.mode());
            String output = app.execute(config);
            assertTrue(output.contains("Count: 4"));
            assertTrue(output.contains("Sum: 100"));
            assertTrue(output.contains("Avg: 25"));
        }

        @Test
        @DisplayName("Should handle statistics errors gracefully")
        void shouldHandleStatsErrorsGracefully() {
            AppConfig config = app.parseArguments(new String[]{"--stats", "abc,def"});
            String output = app.execute(config);
            assertTrue(output.startsWith("Statistics error:"));
        }
    }

    @Nested
    @DisplayName("Main Method Console Output Tests")
    class MainMethodTests {

        @Test
        @DisplayName("Should execute main without exception")
        void shouldExecuteMainMethod() {
            PrintStream originalOut = System.out;
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            try {
                System.setOut(new PrintStream(outContent));
                App.main(new String[]{"--name", "TestRunner"});
                assertTrue(outContent.toString().contains("Hello TestRunner!"));
            } finally {
                System.setOut(originalOut);
            }
        }
    }
}
