package com.greenjon902.greenJam.config;

public class Config {
    public final LexerTemplates lexerTemplates;
    public final TokenClassifierScripts tokenClassifierScripts;

    public Config() {
        lexerTemplates = new LexerTemplates();
        tokenClassifierScripts = new TokenClassifierScripts();
    }
}