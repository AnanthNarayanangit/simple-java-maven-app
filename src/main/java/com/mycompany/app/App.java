package com.mycompany.app;

import com.mycompany.app.model.AppConfig;
import com.mycompany.app.service.CalculatorService;
import com.mycompany.app.service.GreetingService;
import com.mycompany.app.service.SystemInfoService;

import java.util.Arrays;
import java.util.List;

/**
 * Modern Java 21 Application Entry Point and CLI Dispatcher.
 */
public class App {

    private static final String DEFAULT_MESSAGE = "Hello World!";
    public static final String APP_VERSION = "1.0.0";

    private final GreetingService greetingService;
    private final CalculatorService calculatorService;
    private final SystemInfoService systemInfoService;

    public App() {
        this(new GreetingService(), new CalculatorService(), new SystemInfoService());
    }

    public App(GreetingService greetingService, CalculatorService calculatorService, SystemInfoService systemInfoService) {
        this.greetingService = greetingService;
        this.calculatorService = calculatorService;
        this.systemInfoService = systemInfoService;
    }

    public static void main(String[] args) {
        App app = new App();
        AppConfig config = app.parseArguments(args);
        String output = app.execute(config);
        System.out.println(output);
    }

    public AppConfig parseArguments(String[] args) {
        if (args == null || args.length == 0) {
            return AppConfig.defaultConfiguration();
        }

        String flag = args[0].toLowerCase();
        return switch (flag) {
            case "--help", "-h" -> new AppConfig(AppConfig.Mode.HELP, null, null, null);
            case "--version", "-v" -> new AppConfig(AppConfig.Mode.VERSION, null, null, null);
            case "--sysinfo", "-s" -> new AppConfig(AppConfig.Mode.SYSINFO, null, null, null);
            case "--name", "-n" -> {
                String name = (args.length > 1) ? args[1] : "World";
                yield new AppConfig(AppConfig.Mode.GREET, name, null, null);
            }
            case "--calc", "-c" -> {
                String expr = (args.length > 1) ? String.join(" ", Arrays.copyOfRange(args, 1, args.length)) : "0";
                yield new AppConfig(AppConfig.Mode.CALCULATE, null, expr, null);
            }
            case "--stats" -> {
                String numbers = (args.length > 1) ? args[1] : "";
                yield new AppConfig(AppConfig.Mode.STATS, null, null, numbers);
            }
            default -> new AppConfig(AppConfig.Mode.GREET, args[0], null, null);
        };
    }

    public String execute(AppConfig config) {
        if (config == null) {
            return DEFAULT_MESSAGE;
        }

        return switch (config.mode()) {
            case HELP -> getHelpMessage();
            case VERSION -> "Simple Java Maven App v" + APP_VERSION + " (Java " + System.getProperty("java.version") + ")";
            case SYSINFO -> systemInfoService.formatSystemInfo();
            case GREET -> greetingService.getGreeting(config.name());
            case CALCULATE -> {
                try {
                    double result = calculatorService.evaluateSimpleExpression(config.expression());
                    yield String.format("Expression: %s = %s", config.expression(), formatNumber(result));
                } catch (Exception e) {
                    yield "Calculation error: " + e.getMessage();
                }
            }
            case STATS -> {
                try {
                    List<Double> nums = Arrays.stream(config.numbersList().split("[,\\s]+"))
                            .filter(s -> !s.isBlank())
                            .map(Double::parseDouble)
                            .toList();
                    CalculatorService.StatsResult stats = calculatorService.calculateStats(nums);
                    yield String.format("Statistics [Count: %d, Sum: %s, Min: %s, Max: %s, Avg: %s, Median: %s]",
                            stats.count(),
                            formatNumber(stats.sum()),
                            formatNumber(stats.min()),
                            formatNumber(stats.max()),
                            formatNumber(stats.average()),
                            formatNumber(stats.median()));
                } catch (Exception e) {
                    yield "Statistics error: " + e.getMessage();
                }
            }
            case DEFAULT -> DEFAULT_MESSAGE;
        };
    }

    private String formatNumber(double num) {
        if (num == (long) num) {
            return String.format("%d", (long) num);
        }
        return String.format("%.4f", num);
    }

    public String getHelpMessage() {
        return """
                Usage: java -jar target/my-app-1.0-SNAPSHOT.jar [OPTIONS]
                
                Options:
                  -h, --help               Display this help message
                  -v, --version            Display application version
                  -s, --sysinfo            Show host OS and JVM runtime metrics
                  -n, --name <name>        Generate personalized greeting for <name>
                  -c, --calc <expr>        Evaluate simple math expression (e.g. "15 * 4", "2 ^ 8")
                  --stats <n1,n2,...>      Compute statistical metrics on comma-separated numbers
                
                Examples:
                  java -jar target/my-app-1.0-SNAPSHOT.jar --name "Alice"
                  java -jar target/my-app-1.0-SNAPSHOT.jar --calc "42 * 2"
                  java -jar target/my-app-1.0-SNAPSHOT.jar --stats "10,20,30,40,50"
                  java -jar target/my-app-1.0-SNAPSHOT.jar --sysinfo
                """;
    }

    /**
     * Preserved for backwards compatibility with existing Jenkins/Maven sample tests.
     */
    public String getMessage() {
        return DEFAULT_MESSAGE;
    }
}
