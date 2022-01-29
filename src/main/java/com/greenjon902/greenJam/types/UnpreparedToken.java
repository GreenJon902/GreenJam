package com.greenjon902.greenJam.types;

public class UnpreparedToken {
    public final String type;
    public final String value;

    @Override
    public String toString() {
        return "UnpreparedToken{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public UnpreparedToken(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
