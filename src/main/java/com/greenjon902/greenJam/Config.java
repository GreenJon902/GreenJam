package com.greenjon902.greenJam;

import java.util.HashMap;

public class Config {
    public final static class LexerTemplates {
        public static final String integerName = "integer";
        public static final String floatName = "float";

        /**
         * My templating system thingy inspired by the idea of Backus–Naur Form
         * You can use a "." to show that these are two items connected, not one
         * You can put a "\" in front of something to show that it is a letter (in java strings, use "\\")
         */
        public static final HashMap<String, String[]> templates = new HashMap<String, String[]>() {{
            put("integer", new String[]{"integer.integer", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
            put("floatName", new String[]{"integer\\.integer"});
        }};
    }
}
