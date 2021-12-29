package com.greenjon902.betterLogger;

import com.greenjon902.betterLogger.commands.*;

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

    public static void setup(String name, String author, String version, String shortname) {
        betterLoggerCommunicator = new BetterLoggerCommunicator(name, author, version, shortname);
    }


    public static Logger getLogger(String name) {
        Logger logger = new Logger(name);
        betterLoggerCommunicator.sendCommand(new CommandCtrlNewLogger(logger.id, name));
        return logger;
    }


    public static void set_logger_name(Logger logger, String name) {
        betterLoggerCommunicator.sendCommand(new CommandCtrlSetLoggerName(logger.id, name));
    }

    public static void push_logger_name(Logger logger, String name) {
        betterLoggerCommunicator.sendCommand(new CommandCtrlPushLoggerName(logger.id, name));
    }

    public static void pop_logger_name(Logger logger, String name) {
        betterLoggerCommunicator.sendCommand(new CommandCtrlPopLoggerName(logger.id, name));
    }

    public static void log(Logger logger, LogLevel level, String message) {
        betterLoggerCommunicator.sendCommand(new CommandLog(logger.id, level, message));
    }
}
