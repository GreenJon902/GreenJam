package com.greenjon902.betterLogger;

public enum LogLevel {
    INFO ("INFO");

    private final String logLevel;

    LogLevel(String level) {
        logLevel = level;
    }

    public String get() {
        return logLevel;
    }
}
