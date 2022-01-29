package com.greenjon902.greenJam.config;

public class Config {
    public final LexerTemplates lexerTemplates;
    public final TokenPreparationScripts tokenPreparationScripts;

    public Config() {
        lexerTemplates = new LexerTemplates();
        tokenPreparationScripts = new TokenPreparationScripts();
    }
}