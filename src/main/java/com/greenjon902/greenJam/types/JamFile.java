package com.greenjon902.greenJam.types;


import com.greenjon902.betterLogger.BetterLogger;
import com.greenjon902.betterLogger.Logger;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class JamFile {
    private static final Logger logger = BetterLogger.getLogger("JamFile");

    private final File path;
    private String contents = null;

    public JamFile(File path) {
        this.path = path;
    }

    public String getContents() {
        if (contents == null) {
            reloadContents();
        }

        return contents;
    }

    public void reloadContents() {
        try {
            contents = FileUtils.readFileToString(path, StandardCharsets.UTF_8);

        } catch (IOException e) {
            logger.error("Could not load file at", path.toString());
            logger.debug("Absolute path is", path.getAbsolutePath());
            StringWriter sw = new StringWriter();
            //noinspection SuspiciousToArrayCall
            logger.error(Arrays.asList(e.getStackTrace()).toArray(new String[e.getStackTrace().length]));
        }

    }
}
