package com.greenjon902.betterLogger;

public enum LogLevel {
    INFO("INFO"), DUMP("DUMP"), TRACE("TRACE"), DEBUG("DEBUG"), WARNING("WARNING"), ERROR("ERROR"), CRITICAL("CRITICAL");

    private final String logLevel;

    LogLevel(String level) {
        logLevel = level;
    }

    public String get() {
        return logLevel;
    }
}
