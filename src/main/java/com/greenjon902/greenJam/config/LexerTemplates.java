package com.greenjon902.greenJam.config;

import java.util.HashMap;

/**
 * My templating system thingy!
 *
 * You can surround a string with curly brackets to show that it is a template.
 * You can surround a string with angle brackets to show that the value inside is to be recorded, e.g.
 * "'A test string'" matched to the template "'<{string}>'" with return only "A test string" which doesn't have
 *      the "'". Multiple sets of angle brackets are joined together
 */
public class LexerTemplates {
    public final String[] ignorableCharacters = {"\n", " "};

    public final String[] firstLayerTemplateNames = {"integer", "float", "character", "string", "operator"};

    public final HashMap<String, String[]> templates = new HashMap<>() {{
        put("integer", new String[]{"<{digit}>", "<{digit}{integer}>"});
        put("character", new String[]{"'<{_character}>'"});
        put("string", new String[]{"\"<{word}>\""});
        put("operator", new String[]{"<+>"});

        put("word", new String[]{"<{_character}>", "<{_character}{word}>"});
        put("digit", new String[]{"<0>", "<1>", "<2>", "<3>", "<4>", "<5>", "<6>", "<7>", "<8>", "<9>"});
        put("_character", new String[]{"<0>", "<1>", "<2>", "<3>", "<4>", "<5>", "<6>", "<7>", "<8>", "<9>", "<a>",
                "<b>", "<c>", "<d>", "<e>", "<f>", "<g>", "<h>", "<i>", "<j>", "<k>", "<l>", "<m>", "<n>", "<o>", "<p>",
                "<q>", "<r>", "<s>", "<t>", "<u>", "<v>", "<w>", "<x>", "<y>", "<z>", "<A>", "<B>", "<C>", "<D>", "<E>",
                "<F>", "<G>", "<H>", "<I>", "<J>", "<K>", "<L>", "<M>", "<N>", "<O>", "<P>", "<Q>", "<R>", "<S>", "<T>",
                "<U>", "<V>", "<W>", "<X>", "<Y>", "<Z>", "<!!>", "<#>", "<$>", "<%>", "<&>", "<'>", "<(>", "<)>", "<*>",
                "<+>", "<,>", "<->", "<.>", "</>", "<:>", "<;>", "<!<>", "<=>", "<!>>", "<?>", "<@>", "<[>", "<!>",
                "<]>", "<^>", "<_>", "<`>", "<!{>", "<|>", "<}>", "<~>", "< >", "<\t>", "<\n>", "\\<\">"});
    }};
}