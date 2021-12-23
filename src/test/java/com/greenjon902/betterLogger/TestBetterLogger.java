package com.greenjon902.betterLogger;

import org.junit.jupiter.api.Test;

public class TestBetterLogger {
    @Test
    public void testBetterLoggerBasicLog() {
        BetterLogger.start();
        BetterLogger.finish();
    }
}
