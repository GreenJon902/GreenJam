package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.BetterLogger;
import com.greenjon902.betterLogger.BetterLoggerCommunicator;
import com.greenjon902.betterLogger.Logger;
import com.greenjon902.greenJam.types.JamFile;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class GreenJam {
    public static final String NAME = "GreenJam";
    public static final String AUTHOR = "GreenJon902";
    public static final String VERSION = "V0.1-ALPHA";
    public static final String SHORTNAME = "JAM";

    private static Logger logger;

    public static void main(String[] args) {
        BetterLogger.setup(NAME, AUTHOR, VERSION, SHORTNAME);
        BetterLogger.setDoConsoleOut(false);
        BetterLogger.start();

        logger = BetterLogger.getLogger(NAME);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            BetterLogger.getLogger(NAME).info("Finishing"); //Gets rid of name stack
            BetterLogger.finish();
        }, "Shutdown-thread"));

        useArgs(args);
    }

    private static void useArgs(String[] args) {
        logger.push_name("parseArgs");

        logger.log_info("Parsing args");
        logger.debug("Args are", Arrays.toString(args));

        if (args.length == 0) { // Same as python idle
            Idle idle = new Idle();
            idle.start(System.in);

        } else if (Objects.equals(args[0], "-c") || Objects.equals(args[0], "--compile")) { // TODO: FIles
            logger.debug("First argument means compile");

            File file = new File(args[1]);
            if (file.isDirectory()) {
                logger.error("Cannot compile a directory");
                System.exit(1);
            } else if (!file.getName().contains(".")) {
                logger.error("File has no extension");
                System.exit(1);
            } else if (file.getName().split(".", 0)[1] == "jam") {

            }


        } else {
            logger.critical("Couldn't understand what arguments meant");
        }

        logger.pop_name();
    }
}
