package com.greenjon902.betterLogger;

public class BetterLogger {
    private static final BetterLoggerCommunicator betterLoggerCommunicator = new BetterLoggerCommunicator();

    public static void start() {
        betterLoggerCommunicator.start();
    }

    public static void finish() {
        betterLoggerCommunicator.end();
    }

    public static void info(String... strings) {
        System.out.println(String.join(" ", strings));
    };
}
