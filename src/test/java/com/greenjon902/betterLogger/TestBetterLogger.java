package com.greenjon902.betterLogger;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

public class TestBetterLogger {
    private void setupLogger() {
        BetterLogger.setup("BetterLoggerTester", "GreenJon902", "V1.69.urMom", "BLT");
        BetterLogger.start();

        Runtime.getRuntime().addShutdownHook(new Thread(BetterLogger::finish, "Shutdown-thread"));
    }

    @SuppressWarnings({ "unchecked" })
    public void updateEnv(String name, String val) throws NoSuchFieldException, IllegalAccessException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }


    @Test
    public void testLogAtDifferentLevels() throws NoSuchFieldException, IllegalAccessException {
        updateEnv("LOG_LEVEL", "1");
        setupLogger();

        Logger logger = BetterLogger.getLogger("TestLogger");
        logger.log_trace("This is trace text");
        logger.log_dump("This is dump text");
        logger.log_debug("This is debug text");
        logger.log_info("This is info text");
        logger.log_warning("This is warning text");
        logger.log_error("This is error text");
        logger.log_critical("This is critical text");
    }
}
