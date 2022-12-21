package com.greenjon902.greenJam.tokenizer;

import java.util.HashMap;
import java.util.HashSet;

public enum OperatorType {
    SET_VARIABLE("="), ADD("+", 2), SUBTRACT("-", 3), MULTIPLY("*", 1), DIVIDE("/", 0),
    GET_ATTRIBUTE(">");

    public static final HashSet<String> symbols = new HashSet<>();
    static {
        for (OperatorType operatorType : OperatorType.values()) {
            symbols.add(operatorType.symbol);
        }
    }

    public static final int highest_precedence;
    static {
        int highest = -1;
        for (OperatorType operatorType : OperatorType.values()) {
            if (operatorType.precedence > highest) {
                highest = operatorType.precedence;
            }
        }
        highest_precedence = highest;
    }

    public final String symbol;
    /**
     * 0 is highest
     */
    public final int precedence;

    OperatorType(String symbol, int precedence) {
        this.symbol = symbol;
        this.precedence = precedence;
    }
    OperatorType(String symbol) {
        this.symbol = symbol;
        this.precedence = -1;
    }

    public boolean combinedAt(int precedence) {
        return this.precedence == precedence;
    }
}