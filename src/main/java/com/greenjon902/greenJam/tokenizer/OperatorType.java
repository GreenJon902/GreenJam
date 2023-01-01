package com.greenjon902.greenJam.tokenizer;

import java.util.*;

public enum OperatorType {
    // Special Operators ------------
    SET_VARIABLE("="), CALL("()"), GET_ATTRIBUTE("."),

    // Normal Operators ------------
    // Arithmetic
    ADD("+", 3), SUBTRACT("-", 4), MULTIPLY("*", 2), DIVIDE("/", 1),
    // Comparison:
    EQUALS("==", 0), GREATER_THAN(">", 0), LESS_THAN("<", 0);

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

    public static final OperatorType[] lengthOrderedValues;
    static {
        lengthOrderedValues = OperatorType.values();
        Arrays.sort(lengthOrderedValues, Comparator.comparingInt(o -> o.precedence));
        Arrays.sort(lengthOrderedValues, Collections.reverseOrder());
    }

    public final String symbol;
    /**
     * 0 means process first. -1 means it is processed separately.
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