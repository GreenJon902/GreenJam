package com.greenjon902.greenJam.config;

public class Config {
    public final LexerTemplates lexerTemplates;
    public final TokenClassificaters tokenClassificaters;

    public Config() {
        lexerTemplates = new LexerTemplates();
        tokenClassificaters = new TokenClassificaters();
    }
}