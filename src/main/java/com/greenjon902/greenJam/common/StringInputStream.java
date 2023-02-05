package com.greenjon902.greenJam.common;

public class StringInputStream {
    public final String string;
    public final String fileName;
    public int location = 0;

    public StringInputStream(String fileName, String string) {
        this.fileName = fileName;
        this.string = string;
    }

    public String currentLocationString() {
        return location + " " + string.charAt(location);
    }

    public char next() {
        return next(0);
    }

    public char next(int offset) {
        return string.charAt(location + offset);
    }

    public char consume() {
        char character = string.charAt(location);
        location += 1;
        return character;
    }

    public boolean consumeIf(char character) {
        if (string.charAt(location) == character) {
            location += 1;
            return true;
        }
        return false;
    }

    public boolean consumeIf(String other_string) {
        if (string.regionMatches(location, other_string, 0, other_string.length())) {
            location += other_string.length();
            return true;
        }
        return false;
    }

    public boolean isEnd() {
        return !(location < string.length());
    }

    public int getCurrentLineNumber() {
        int lines = 0;
        for (int i=0; i<location; i++) {
            if (string.charAt(i) == '\n') {
                lines += 1;
            }
        }
        return lines;
    }

    public String getCurrentLine() {
        int offset = -1;
        while (true) {
            if (location + offset < 0 || string.charAt(location + offset) == '\n') {
                offset += 1;
                break;
            }
            offset -= 1;
        }

        StringBuilder line = new StringBuilder();
        while (true) {
            if (location + offset >= string.length() || string.charAt(location + offset) == '\n') {
                break;
            }
            line.append(string.charAt(location + offset));
            offset += 1;
        }
        return line.toString();
    }

    public int getCurrentLinePosition() {
        int pos = 1;
        while (true) {
            if (location - pos < 0 || string.charAt(location - pos) == '\n') {
                break;
            }
            pos += 1;
        }
        return pos;
    }

    @Override
    public String toString() {
        String formatted_string = string;
        formatted_string = formatted_string.replaceAll("\n", "\\n");
        formatted_string = formatted_string.replaceAll("\t", "\\t");

        return "StringInputStream{" +
                "string=\n\t" + formatted_string + "\n" +
                '\t' + " ".repeat(location) + "^\n" +
                '\t' + ", location=" + location +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
