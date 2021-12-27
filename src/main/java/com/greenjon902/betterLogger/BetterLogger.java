package com.greenjon902.betterLogger;

import com.greenjon902.betterLogger.commands.CommandLog;

public class BetterLogger {
    private static BetterLoggerCommunicator betterLoggerCommunicator;

    public static void start() {
        if (betterLoggerCommunicator == null) {
            betterLoggerCommunicator = new BetterLoggerCommunicator();
        }
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

    public static void setup(String name, String author, String version, String shortname) {
        betterLoggerCommunicator = new BetterLoggerCommunicator(name, author, version, shortname);
    }
}
