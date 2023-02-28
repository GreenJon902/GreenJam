package com.greenjon902.greenJam.common;

public class EscapeCharacterUtil {

    /**
     * Gets the escape character from the next character(s) supplies in the {@link StringInputStream}. This expects any
     * \ to already be consumed.
     */
    public static char getEscapeCharacter(StringInputStream string) {
        Character ret = switch (string.next()) {
            case 'x' -> (char) parseHexCharacter(string);
            case '\'' -> '\'';
            case '\"' ->  '\"';
            case '\\' -> '\\';
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 't' -> '\t';
            case 'b' -> '\b';
            case 'f' -> '\f';
            case '0' -> '\0';
            default -> null;
        };
        if (ret != null) {
            if (string.consume() == 'x') { // We need to consume anyway but if it's a hex character then we need to
                string.consume();          // consume twice more
                string.consume();
            }
            return ret;
        }

        Errors.invalidEscapeSequence(string);
        return '\0'; // It will never get here
    }



    private static int parseHexCharacter(StringInputStream syntax) {
        return Integer.valueOf(String.valueOf(syntax.next(1)) + syntax.next(2), 16);
    }
}
