package com.greenjon902.greenJam.tokenizer;

public enum CommandType {
    WRITE_TO_STREAM("WRITE"), ADD("ADD");

    public final String name;

    CommandType(String name) {
        this.name = name;
    }
}
