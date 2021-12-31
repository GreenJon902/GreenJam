package com.greenjon902.betterLogger;

public class Logger {
    private static Integer lastLoggerId = -1;

    private final String name;
    public final Integer id;

    public Logger(String name) {
        lastLoggerId = lastLoggerId + 1;
        id = lastLoggerId;

        this.name = name;
    }
    
    public void set_logger_name(String name) {
        BetterLogger.set_logger_name(this, name);
    }

    public void push_logger_name(String name) {
        BetterLogger.push_logger_name(this, name);
    }

    public void pop_logger_name() {
        BetterLogger.pop_logger_name(this);
    }

    public void log_dump(String... messages) {
        BetterLogger.log(this, LogLevel.DUMP, String.join(" ", messages));
    }

    public void log_trace(String... messages) {
        BetterLogger.log(this, LogLevel.TRACE, String.join(" ", messages));
    }

    public void log_debug(String... messages) {
        BetterLogger.log(this, LogLevel.DEBUG, String.join(" ", messages));
    }

    public void log_info(String... messages) {
        BetterLogger.log(this, LogLevel.INFO, String.join(" ", messages));
    }

    public void log_warning(String... messages) {
        BetterLogger.log(this, LogLevel.WARNING, String.join(" ", messages));
    }

    public void log_error(String... messages) {
        BetterLogger.log(this, LogLevel.ERROR, String.join(" ", messages));
    }

    public void log_critical(String... messages) {
        BetterLogger.log(this, LogLevel.CRITICAL, String.join(" ", messages));
    }


    // ALIASES (because shorted and nicer but less explicit depending on the name the variable the loggers stored in) --
    public void set_name(String name) {set_logger_name(name);}
    public void push_name(String name) {push_logger_name(name);}
    public void pop_name() {pop_logger_name();}
    public void dump(String... messages) {log_dump(messages);}
    public void trace(String... messages) {log_trace(messages);}
    public void debug(String... messages) {log_debug(messages);}
    public void info(String... messages) {log_info(messages);}
    public void warning(String... messages) {log_warning(messages);}
    public void error(String... messages) {log_error(messages);}
    public void critical(String... messages) {log_critical(messages);}
}
