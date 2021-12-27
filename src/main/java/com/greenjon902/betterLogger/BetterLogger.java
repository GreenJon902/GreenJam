package com.greenjon902.betterLogger;

import com.greenjon902.betterLogger.commands.CommandLog;

public class BetterLogger {
    private static final BetterLoggerCommunicator betterLoggerCommunicator = new BetterLoggerCommunicator();

    public static void start() {
        betterLoggerCommunicator.start();
    }

    public static void finish() {
        betterLoggerCommunicator.end();
    }

    public static void info(String... strings) {
        log(LogLevel.INFO, String.join(" ", strings));
    }

    private static void log(LogLevel info, String message) {
        betterLoggerCommunicator.sendCommand(new CommandLog(info, message));
    }
}
