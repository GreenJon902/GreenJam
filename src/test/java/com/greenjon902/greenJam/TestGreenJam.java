package com.greenjon902.greenJam;

import org.junit.jupiter.api.Test;

public class TestGreenJam {
    @Test
    public void testInterpretSysCalls() {
        GreenJam.main(new String[]{"-c", "tests/sysCalls.jon"});
    }
}
