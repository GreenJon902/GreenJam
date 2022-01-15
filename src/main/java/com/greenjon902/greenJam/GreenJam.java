package com.greenjon902.greenJam;


import java.io.File;
import java.util.Objects;

public class GreenJam {
    public static final String NAME = "GreenJam";
    public static final String AUTHOR = "GreenJon902";
    public static final String VERSION = "V0.1-ALPHA";


    public static void main(String[] args) {
        System.out.println(NAME + " by " + AUTHOR);
        System.out.println("     " + "VERSION " + VERSION);

        useArgs(args);
        System.exit(0);
    }

    private static void useArgs(String[] args) {
        if (args.length == 0) { // Same as python idle
            Idle idle = new Idle();
            idle.start(System.in);

        } else if (Objects.equals(args[0], "-c") || Objects.equals(args[0], "--compile")) { // TODO: FIles

            File file = new File(args[1]);
            if (file.isDirectory()) {
                Logging.error("Compiling directories is not yet supported");
            } else if (!file.getName().contains(".")) {
                Logging.error("File doesn't have an extension");
            } else if (file.getName().split(".", 0)[1] == "jam") {

            }


        } else {
            Logging.error("Could not understand arguments");
        }

    }
}
