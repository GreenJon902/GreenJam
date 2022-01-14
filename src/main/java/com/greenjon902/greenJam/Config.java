package com.greenjon902.greenJam;

import java.util.HashMap;

public class Config {
    public final _lexerTemplates lexerTemplates;

    public Config() {
        lexerTemplates = new _lexerTemplates();
    }
}

class _lexerTemplates {
    public final String integerName = "integer";
    public final String floatName = "float";

    /**
     * My templating system thingy!
     *
     * You can surround a string with curly brackets to show that it is a template.
     * You can surround a string with angle brackets to show that the value inside is to be recorded, e.g.
     * "'A test string'" matched to the template "'<{string}>'" with return only "A test string" which doesn't have
     *      the "'". Multiple sets of angle brackets are joined together
     */
    public final HashMap<String, String[]> templates = new HashMap<String, String[]>() {{
        put("integer", new String[]{"<{digit}>", "<{digit}{integer}>"});
        put("float", new String[]{"<{integer}.{integer}>"});

        put("digit", new String[]{"<0>", "<1>", "<2>", "<3>", "<4>", "<5>", "<6>", "<7>", "<8>", "<9>"});
    }};
}