package com.greenjon902.greenJam.tokenizer;

public enum BracketType {
    ROUND_OPEN("(", true), ROUND_CLOSE(")", false);

    public final String symbol;
    public final boolean opening;

    BracketType(String symbol, boolean opening) {
        this.symbol = symbol;
        this.opening = opening;
    }
}
