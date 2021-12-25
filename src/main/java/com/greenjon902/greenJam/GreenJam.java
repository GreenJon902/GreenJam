package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.BetterLogger;

public class GreenJam {
    public static void main(String[] args) {
        BetterLogger.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BetterLogger.info("Finishing");
            BetterLogger.finish();
        }, "Shutdown-thread"));
    }
}
