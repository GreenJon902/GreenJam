package com.greenjon902.greenJam.types;


import com.greenjon902.greenJam.Logging;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class JamFile {
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
            Logging.error("Could not load file at" + path);
            StringWriter sw = new StringWriter();
        }

    }
}
