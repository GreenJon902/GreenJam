package com.greenjon902.greenJam.config;

public class Config {
    public final LexerTemplates lexerTemplates;
    public final TokenPreparers tokenPreparers;

    public Config() {
        lexerTemplates = new LexerTemplates();
        tokenPreparers = new TokenPreparers();
    }
}