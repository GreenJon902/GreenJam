package com.greenjon902.greenJam.tokenizer;

import java.util.HashMap;
import java.util.HashSet;

public enum OperatorType {
    SET_VARIABLE("="), ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/"),
    GET_ATTRIBUTE(">");

    public static final HashSet<String> symbols = new HashSet<>();
    static {
        for (OperatorType operatorType : OperatorType.values()) {
            symbols.add(operatorType.symbol);
        }
    }

    public final String symbol;

    OperatorType(String symbol) {
        this.symbol = symbol;
    }
}