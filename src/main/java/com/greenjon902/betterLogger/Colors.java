package com.greenjon902.betterLogger;

public class Colors {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static String format(String in) {
        String out = in;

        out = out.replace("{RESET}", RESET);
        out = out.replace("{BLACK}", BLACK);
        out = out.replace("{RED}", RED);
        out = out.replace("{GREEN}", GREEN);
        out = out.replace("{YELLOW}", YELLOW);
        out = out.replace("{BLUE}", BLUE);
        out = out.replace("{PURPLE}", PURPLE);
        out = out.replace("{CYAN}", CYAN);
        out = out.replace("{WHITE}", WHITE);

        return out;
    }
}
