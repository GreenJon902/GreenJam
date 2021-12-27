package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.BetterLogger;

public class GreenJam {
    public static final String NAME = "GreenJam";
    public static final String AUTHOR = "GreenJon902";
    public static final String VERSION = "V0.1-ALPHA";
    public static final String SHORTNAME = "JAM";

    public static void main(String[] args) {
        BetterLogger.setup(NAME, AUTHOR, VERSION, SHORTNAME);
        BetterLogger.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BetterLogger.info("Finishing");
            BetterLogger.finish();
        }, "Shutdown-thread"));
    }
}
