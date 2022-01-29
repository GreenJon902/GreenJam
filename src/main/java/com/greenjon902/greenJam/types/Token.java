package com.greenjon902.greenJam.types;

import java.util.HashMap;

public class Token {
    private final String type;
    private final HashMap<String, String> attributes = new HashMap<>();

    public Token(String type, HashMap<String, String> attributes) {
        this.type = type;
        this.attributes.putAll(attributes);
    }

    @Override
    public String toString() {
        return "Token{" +
                "type='" + type + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
