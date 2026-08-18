package com.mycompany.app.model;

/**
 * Immutable configuration record for the CLI application options.
 */
public record AppConfig(
    Mode mode,
    String name,
    String expression,
    String numbersList
) {
    public enum Mode {
        DEFAULT,
        GREET,
        CALCULATE,
        STATS,
        SYSINFO,
        VERSION,
        HELP
    }

    public static AppConfig defaultConfiguration() {
        return new AppConfig(Mode.DEFAULT, "World", null, null);
    }
}
