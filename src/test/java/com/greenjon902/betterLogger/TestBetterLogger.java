package com.greenjon902.betterLogger;

import org.junit.jupiter.api.Test;

public class TestBetterLogger {
    private void setupLogger() {
        BetterLogger.setup("BetterLoggerTester", "GreenJon902", "V1.69.urMom", "BLT");
        BetterLogger.start();

        Runtime.getRuntime().addShutdownHook(new Thread(BetterLogger::finish, "Shutdown-thread"));
    }

    @Test
    public void testLogAtDifferentLevels() {
        setupLogger();

        Logger logger = BetterLogger.getLogger("TestLogger");
        logger.log_trace("This is trace text and cannot be seen");
        logger.log_dump("This is dump text");
        logger.log_debug("This is debug text");
        logger.log_info("This is info text");
        logger.log_warning("This is warning text");
        logger.log_error("This is error text");
        logger.log_critical("This is critical text");
    }
}
