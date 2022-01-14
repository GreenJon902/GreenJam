package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.BetterLogger;
import com.greenjon902.betterLogger.Logger;
import com.greenjon902.greenJam.types.FloatToken;
import com.greenjon902.greenJam.types.IntegerToken;
import com.greenjon902.greenJam.types.Token;
import com.greenjon902.greenJam.types.TokenList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Lexer {
    private final static Logger logger = BetterLogger.getLogger("Lexer");

    public TokenList analyzeString(String jam, Config config) {
        TokenList tokenList = new TokenList();

        int currentLocation = 0;
        while (currentLocation < jam.length()) {
            logger.log_debug("Currently at location", String.valueOf(currentLocation), "in string", jam);
            Token token = getFirstToken(jam, config);
            logger.log_debug("Found token", token.toString().replace("{", "{{").replace("}", "}}"));
            break;
        }

        return tokenList;
    }

    public Token getFirstToken(String jam, Config config) {
        HashMap<Integer, Token> matches = new HashMap<>();



        TemplatedStringAndOriginalLength templatedStringAndOriginalLength;

        // Integer -----------------------------------------------------------------------------------------------------
        String[] integerTemplates = config.lexerTemplates.templates.get(config.lexerTemplates.integerName);
        templatedStringAndOriginalLength = matchTemplatesAndGetLength(jam, integerTemplates, config.lexerTemplates.templates);

        if (templatedStringAndOriginalLength != null) {
            matches.put(templatedStringAndOriginalLength.originalLength, new IntegerToken(templatedStringAndOriginalLength.templatedString));
        }

        // Float -------------------------------------------------------------------------------------------------------
        String[] floatTemplates = config.lexerTemplates.templates.get(config.lexerTemplates.floatName);
        templatedStringAndOriginalLength = matchTemplatesAndGetLength(jam, floatTemplates, config.lexerTemplates.templates);

        if (templatedStringAndOriginalLength != null) {
            matches.put(templatedStringAndOriginalLength.originalLength, new FloatToken(templatedStringAndOriginalLength.templatedString));
        }


        int elementLength = 0;
        for (int length : matches.keySet()) {
            if (length > elementLength) {
                elementLength = length;
            }
        }
        return matches.get(elementLength);
    }

    private static final char templateEscapeCharacter = '\\';
    private static final char templateStartMatchTemplate = '{';
    private static final char templateEndMatchTemplate = '}';

    private static TemplatedStringAndOriginalLength matchTemplatesAndGetLength(String string, String[] templatesToCheck, HashMap<String, String[]> allTemplates) {
        logger.push_logger_name("matchTemplatesAndGetLength(" + string + ")");
        logger.log_dump("Matching string", string, "to templates", Arrays.toString(templatesToCheck).replace("{", "{{").replace("}", "}}"));

        if (string.length() == 0) {
            logger.log_dump("String length was 0");
            logger.pop_logger_name();
            return null;
        }

        ArrayList<TemplatedStringAndOriginalLength> matches = new ArrayList<>();
        boolean saveMode = false;

        for (String template : templatesToCheck) {
            logger.push_name(template.replace("{", "{{").replace("}", "}}"));
            logger.log_trace("Matching string", string, "to templates", template.replace("{", "{{").replace("}", "}}"));

            StringBuilder currentMatch = new StringBuilder();

            boolean failed = false;

            int currentLocationInTemplate = 0;
            int currentLocationInString = 0;
            while (currentLocationInTemplate < template.length()) {
                logger.log_dump("Current location in template is", String.valueOf(currentLocationInTemplate), "which is", String.valueOf(template.charAt(currentLocationInTemplate)).replace("{", "{{").replace("}", "}}"));

                if (currentLocationInString < string.length()) {
                    System.out.println(1);
                    logger.log_dump("Current location in string is", String.valueOf(currentLocationInString), "which is", String.valueOf(string.charAt(currentLocationInString)).replace("{", "{{").replace("}", "}}"));
                    System.out.println(2);
                }
                System.out.println(1);

                if (template.charAt(currentLocationInTemplate) == templateStartMatchTemplate && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {

                    currentLocationInTemplate++; // skip first curly bracket
                    StringBuilder templateName = new StringBuilder();

                    while (template.charAt(currentLocationInTemplate) != templateEndMatchTemplate && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {
                        templateName.append(template.charAt(currentLocationInTemplate));
                        currentLocationInTemplate++;
                    }

                    currentLocationInTemplate++; // skip final curly bracket

                    if (!allTemplates.containsKey(templateName.toString())) {
                        logger.error("Could not find template for", templateName.toString());
                        System.exit(1);
                    }
                    TemplatedStringAndOriginalLength matched = matchTemplatesAndGetLength(string.substring(currentLocationInString), allTemplates.get(templateName.toString()), allTemplates);
                    if (matched == null) {
                        failed = true;
                        break;

                    } else if (saveMode) {
                        currentLocationInString += matched.originalLength;
                        currentMatch.append(matched.templatedString);
                    }

                } else if (template.charAt(currentLocationInTemplate) == '<' && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {
                    saveMode = true;
                    currentLocationInTemplate++;
                } else if (template.charAt(currentLocationInTemplate) == '>' && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {
                    saveMode = false;
                    currentLocationInTemplate++;

                } else {
                    if (currentLocationInString >= string.length()) {
                        failed = true;
                        break;
                    }


                    if (template.charAt(currentLocationInTemplate) != string.charAt(currentLocationInString)) {
                        failed = true;
                        break;
                    }

                    if (saveMode) {
                        currentMatch.append(string.charAt(currentLocationInString));
                    }

                    currentLocationInString++;
                    currentLocationInTemplate++;
                }
            }

            if (!failed) {
                matches.add(new TemplatedStringAndOriginalLength(currentMatch.toString(), currentLocationInString));
            }
            logger.pop_name();
        }

        if (matches.size() == 0) {
            logger.pop_name();
            return null;
        }


        int index = 0;
        int elementLength = matches.get(0).originalLength;
        for (int i=1; i< matches.size(); i++) {
            if (matches.get(i).originalLength > elementLength) {
                index = i;
                elementLength = matches.get(i).originalLength;
            }
        }

        logger.pop_name();
        return matches.get(index);
    }
}

class TemplatedStringAndOriginalLength {
    public String templatedString;
    public int originalLength;

    public TemplatedStringAndOriginalLength(String templatedString, int originalLength) {
        this.templatedString = templatedString;
        this.originalLength = originalLength;
    }

    @Override
    public String toString() {
        return "TemplatedStringAndOriginalLength{" +
                "templatedString='" + templatedString + '\'' +
                ", originalLength=" + originalLength +
                '}';
    }
}

class lastCharacter {
    public static boolean equals(char character, int currentLocation, String string) {
        return (currentLocation != 0 && string.charAt(currentLocation-1) == character);
    }
}