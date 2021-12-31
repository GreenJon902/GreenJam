package com.greenjon902.greenJam.interpreter;

import com.greenjon902.betterLogger.BetterLogger;
import com.greenjon902.betterLogger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class Interpreter {
    private final Logger logger = BetterLogger.getLogger("Interpreter");

    public void interpret(File file) {
        logger.push_name(file.getName().replace(".jon", ""));

        try {
            String fileContents = new String (Files.readAllBytes(file.toPath()));
            String[] lines = fileContents.split(";");

            int lineNumber = 1;
            for (String line : lines) {
                lineNumber += countNewlines(line);
                System.out.println(lineNumber);
                System.out.println(countNewlines(line));
                logger.push_name(String.valueOf(lineNumber));

                interpret(line);

                logger.pop_name();
            }

        } catch (FileNotFoundException e) {
            logger.critical("File at path", file.getAbsolutePath(), "doesn't exist");
        } catch (IOException e) {
            logger.critical("Can't read file at path", file.getAbsolutePath());
        }

        logger.pop_name();
    }

    private void interpret(String line) {
        String lineFormattedForPrint = line;
        if (lineFormattedForPrint.startsWith("\n")) {
           //line = line.substring(1);
        }

        logger.log_info("Interpreting \"" + line + "\"");
    }

    private static int countNewlines(String line) {
        return line.split("\n").length - 1;
    }
}
