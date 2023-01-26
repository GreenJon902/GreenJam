package com.greenjon902.greenJam;

public class StringInputStream {
    public final String string;
    public int location = 0;
    public final String fileName;

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
}
