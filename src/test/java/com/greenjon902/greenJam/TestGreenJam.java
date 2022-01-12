package com.greenjon902.greenJam;

import org.junit.jupiter.api.Test;

public class TestGreenJam {
    private void waitForThreadsToFinish() {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            System.out.println(thread);
            System.out.println(thread.getName());
        }
    }

    @Test
    public void testInterpretSysCalls() {
        GreenJam.main(new String[]{"-c", "tests/sysCalls.jon"});
    }

    @Test
    public void testIdle() {
        System.out.println(1);
        GreenJam.main(new String[]{});
        System.out.println(2);
        waitForThreadsToFinish();
    }
}
