package com.greenjon902.greenJam.types;

public class UnclassifiedToken {
    public final String type;
    public final String value;

    @Override
    public String toString() {
        return "UnclassifiedToken{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public UnclassifiedToken(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
