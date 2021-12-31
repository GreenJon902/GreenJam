package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.BetterLogger;
import com.greenjon902.betterLogger.Logger;

import java.util.Arrays;

public class GreenJam {
    public static final String NAME = "GreenJam";
    public static final String AUTHOR = "GreenJon902";
    public static final String VERSION = "V0.1-ALPHA";
    public static final String SHORTNAME = "JAM";

    private static Logger logger;

    public static void main(String[] args) {
        BetterLogger.setup(NAME, AUTHOR, VERSION, SHORTNAME);
        BetterLogger.start();

        logger = BetterLogger.getLogger(NAME);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Finishing");
            BetterLogger.finish();
        }, "Shutdown-thread"));

        parseArgs(args);
    }

    private static void parseArgs(String[] args) {
        logger.push_name("parseArgs");

        logger.log_info("Parsing args");
        logger.debug("Args are", Arrays.toString(args));

        logger.pop_name();
    }
}
