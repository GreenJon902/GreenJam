package com.greenjon902.greenJam;

public class Logging {
    public static void error(String string) {
        System.out.println("\u001B[31m[ERROR] " + string + "\u001B[0m");
        System.exit(1);
    }
}
