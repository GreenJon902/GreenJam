package com.greenjon902.greenJam;

import com.greenjon902.betterLogger.Logger;
import com.greenjon902.greenJam.types.FloatToken;
import com.greenjon902.greenJam.types.IntegerToken;
import com.greenjon902.greenJam.types.Token;
import com.greenjon902.greenJam.types.TokenList;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private final static Logger logger = new Logger("GreenJam.Lexer");

    public TokenList analyzeString(String jam, Config config) {
        TokenList tokenList = new TokenList();

        int currentLocation = 0;
        while (currentLocation < jam.length()) {
            System.out.println(getFirstToken(jam, config));
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

        if (string.length() == 0) {
            return null;
        }

        ArrayList<TemplatedStringAndOriginalLength> matches = new ArrayList<>();
        boolean saveMode = false;

        for (String template : templatesToCheck) {
            StringBuilder currentMatch = new StringBuilder();

            boolean failed = false;

            int currentLocationInTemplate = 0;
            int currentLocationInString = 0;
            while (currentLocationInTemplate < template.length()) {


                if (template.charAt(currentLocationInTemplate) == templateStartMatchTemplate && (currentLocationInTemplate == 0 || template.charAt(currentLocationInTemplate-1) != templateEscapeCharacter)) {

                    currentLocationInTemplate++; // skip first curly bracket
                    StringBuilder templateName = new StringBuilder();

                    while (template.charAt(currentLocationInTemplate) != templateEndMatchTemplate && (currentLocationInTemplate == 0 || template.charAt(currentLocationInTemplate-1) != templateEscapeCharacter)) {
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

                } else if (template.charAt(currentLocationInTemplate) == '<') {
                    saveMode = true;
                    currentLocationInTemplate++; // TODO: Implement next check
                } else if (template.charAt(currentLocationInTemplate) == '>') {
                    saveMode = false;
                    currentLocationInTemplate++; // TODO: Implement next check

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

        }

        if (matches.size() == 0) {
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
