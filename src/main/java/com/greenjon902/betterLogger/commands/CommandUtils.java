package com.greenjon902.betterLogger.commands;

public class CommandUtils {
    public static String padType(String in) {
        String out = "0000";
        out = (out + in.length()).substring(String.valueOf(in.length()).length());
        out = out + in;
        return out;
    }

    public static String padMessage(String in) {
        String out = "00000000";
        out = (out + in.length()).substring(String.valueOf(in.length()).length());
        out = out + in;
        return out;
    }
}
