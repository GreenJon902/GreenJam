package com.greenjon902.greenJam;

public class StringInputStream {
    public final String string;
    public int location = 0;

    public StringInputStream(String string) {
        this.string = string;
    }

    public String currentLocationString() {
        return location + " " + string.charAt(location);
    }

    public char next() {
        return string.charAt(location);
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
}
