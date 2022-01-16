package com.greenjon902.greenJam;

import com.greenjon902.greenJam.types.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Lexer {

    public TokenList analyzeString(String jam, Config config) {
        TokenList tokenList = new TokenList();

        int currentLocation = 0;
        while (currentLocation < jam.length()) {
            int ignorable = howMuchOfCurrentLocationIsIgnorable(jam.substring(currentLocation), config);
            if (ignorable != 0) {
                currentLocation += ignorable;
                continue;
            }

            TokenAndOriginalLength tokenAndOriginalLength = getFirstToken(jam.substring(currentLocation), config);

            if (tokenAndOriginalLength == null) {
                System.out.println(jam.substring(currentLocation));
                Logging.error("Could not find token at location " + currentLocation);
            }
            assert tokenAndOriginalLength != null;  // To stop pycharm complaining even tho Logging.error exits

            Token token = tokenAndOriginalLength.token;
            int origonalLength = tokenAndOriginalLength.originalLength;

            currentLocation += origonalLength;
            tokenList.append(token);
        }

        return tokenList;
    }

    public int howMuchOfCurrentLocationIsIgnorable(String string, Config config) {
        for (String ignorableString : config.lexerTemplates.ignorableCharacters) {
            if (string.startsWith(ignorableString)) {
                return ignorableString.length();
            }
        }
        return 0;
    }

    public TokenAndOriginalLength getFirstToken(String jam, Config config) {
        HashMap<Integer, Token> matches = new HashMap<>();
        System.out.println(jam);



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

        // Character -------------------------------------------------------------------------------------------------------
        String[] characterTemplates = config.lexerTemplates.templates.get(config.lexerTemplates.characterName);
        templatedStringAndOriginalLength = matchTemplatesAndGetLength(jam, characterTemplates, config.lexerTemplates.templates);

        if (templatedStringAndOriginalLength != null) {
            matches.put(templatedStringAndOriginalLength.originalLength, new CharacterToken(templatedStringAndOriginalLength.templatedString));
        }

        // String -------------------------------------------------------------------------------------------------------
        String[] stringTemplates = config.lexerTemplates.templates.get(config.lexerTemplates.stringName);
        templatedStringAndOriginalLength = matchTemplatesAndGetLength(jam, stringTemplates, config.lexerTemplates.templates);

        if (templatedStringAndOriginalLength != null) {
            matches.put(templatedStringAndOriginalLength.originalLength, new StringToken(templatedStringAndOriginalLength.templatedString));
        }

        // -------------------------------------------------------------------------------------------------------------

        if (matches.size() == 0) {
            return null;
        }

        int elementLength = 0;
        for (int length : matches.keySet()) {
            if (length > elementLength) {
                elementLength = length;
            }
        }
        return new TokenAndOriginalLength(matches.get(elementLength), elementLength);
    }

    private static final char templateEscapeCharacter = '!';
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

                if (template.charAt(currentLocationInTemplate) == templateStartMatchTemplate && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {

                    currentLocationInTemplate++; // skip first curly bracket
                    StringBuilder templateName = new StringBuilder();

                    while (template.charAt(currentLocationInTemplate) != templateEndMatchTemplate && !lastCharacter.equals(templateEscapeCharacter, currentLocationInTemplate, template)) {
                        templateName.append(template.charAt(currentLocationInTemplate));
                        currentLocationInTemplate++;
                    }

                    currentLocationInTemplate++; // skip final curly bracket

                    if (!allTemplates.containsKey(templateName.toString())) {
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

class TokenAndOriginalLength {
    public Token token;
    public int originalLength;

    public TokenAndOriginalLength(Token templatedString, int originalLength) {
        this.token = templatedString;
        this.originalLength = originalLength;
    }

    @Override
    public String toString() {
        return "TokenAndOriginalLength{" +
                "token='" + token + '\'' +
                ", originalLength=" + originalLength +
                '}';
    }
}

class lastCharacter {
    public static boolean equals(char character, int currentLocation, String string) {
        return (currentLocation != 0 && string.charAt(currentLocation-1) == character);
    }
}