package com.greenjon902.betterLogger;

import org.python.util.PythonInterpreter;

import java.net.URL;

public class BetterLogger {
    public static URL getPythonLaunchFile() {
        return BetterLogger.class.getClassLoader().getResource("com/greenjon902/betterLogger/portal.py");
    }

    public static void start() {
        startPython();
    }

    public static void finish() {
    }

    private static void startPython() {
        try {
            PythonInterpreter pyInterp = new PythonInterpreter();
            pyInterp.execfile(getPythonLaunchFile().openStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
