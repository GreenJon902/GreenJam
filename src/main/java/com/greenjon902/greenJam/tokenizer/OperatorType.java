package com.greenjon902.greenJam.tokenizer;

import java.util.HashMap;

public enum OperatorType {
    SET_VARIABLE("="), ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"),
    OPEN_BRACKET("("), CLOSE_BRACKET(")");

    public final String symbol;

    OperatorType(String symbol) {
        this.symbol = symbol;
    }
}