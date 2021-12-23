package com.greenjon902.betterLogger;

import org.python.util.PythonInterpreter;

import java.util.Objects;
import java.util.Properties;

public class BetterLogger {
    public static String getPythonScriptsPath() {
        return Objects.requireNonNull(BetterLogger.class.getClassLoader().getResource("com/greenjon902/betterLogger")).getFile();
    }

    public static void start() {
        startPython();
    }

    public static void finish() {
    }

    private static void startPython() {
        try {
            Properties properties = new Properties();
            properties.setProperty("python.path", getPythonScriptsPath());
            PythonInterpreter.initialize(System.getProperties(), properties, new String[]{""});

            PythonInterpreter pyInterp = new PythonInterpreter();
            pyInterp.exec("import sys; print(sys.path)");
            pyInterp.exec("import betterLogger");
            pyInterp.exec("betterLogger.info(\"Test\")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
