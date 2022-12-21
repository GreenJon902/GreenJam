package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.tokenizer.BracketType;
import com.greenjon902.greenJam.tokenizer.OperatorType;
import com.greenjon902.greenJam.tokenizer.Token;
import com.greenjon902.greenJam.tokenizer.TokenType;

import java.util.ArrayList;

public class Parser {
    private int location;
    private Token[] tokens;

    public Parser(Token[] tokens) {
        this.location = 0;
        this.tokens = tokens;
    }

    public AbstractSyntaxTree getParsed() {
        return new AbstractSyntaxTree(parseCodeBlock());
    }

    private AbstractSyntaxTreeNode parseCodeBlock() {
        return parseStatement();
    }

    private AbstractSyntaxTreeNode parseStatement() {
        if (tokens[location + 1].isOperator(OperatorType.SET_VARIABLE)) {
            Identifier variableName = new Identifier((String) tokens[location].primaryStorage);
            location += 2;
            AbstractSyntaxTreeNode value = parseStatement();

            return new Operation(OperatorType.SET_VARIABLE, variableName, value);
        }

        return parseExpression();

    }

    private AbstractSyntaxTreeNode parseExpression() {
        int offset = 0;

        // Convert to simple arithmetic by removing brackets, attribute getting and function calls
        ArrayList<Object> simplified = new ArrayList<>();

        AbstractSyntaxTreeNode current;
        current = null;

        while (true) {
            // Handles new current (and brackets)
            if (current == null) { // Current
                if (tokens[location + offset].isBracket(BracketType.ROUND_OPEN)) { // This does the brackets
                    location += offset + 1;

                    current = parseExpression();
                    offset = 1;  // It's the close bracket

                } else {
                    if (tokens[location + offset].type == TokenType.LITERAL) {
                        current = new Literal((String) tokens[location + offset].primaryStorage);

                    } else if (tokens[location + offset].type == TokenType.IDENTIFIER) {
                        current = new Identifier((String) tokens[location + offset].primaryStorage);

                    } else {
                        throw new RuntimeException();
                    }
                    offset += 1;

                }


            // Attribute getting
            } else if (tokens[location + offset + 1].isOperator(OperatorType.GET_ATTRIBUTE)) { // Next operator is get attr
                current = new Operation(OperatorType.GET_ATTRIBUTE, current,
                        new Identifier((String) tokens[location + offset + 2].primaryStorage));
                offset += 2;


            } else {  // TODO: Function Calls
                simplified.add(current);

                Token next = tokens[location + offset];
                if (next.type != TokenType.OPERATOR) {
                    break;
                }
                simplified.add(next.primaryStorage); // Keep the operator as a token as we can use OperatorType#combinedAt(int)
                offset += 1;

                current = null;
            }
        }
        System.out.println(simplified);


        // It is now a simple maths, so we can apply all other operators while looping through the list
        for (int precedence_level = 0; precedence_level <= OperatorType.highest_precedence; precedence_level++) {
            offset = 1;
            while (offset < simplified.size()) {

                OperatorType currentOperatorType = (OperatorType) simplified.get(offset);
                if (currentOperatorType.combinedAt(precedence_level)) {
                    AbstractSyntaxTreeNode a = (AbstractSyntaxTreeNode) simplified.get(offset - 1); // This is at location that
                                                                                                    // is turning into combined
                    AbstractSyntaxTreeNode b = (AbstractSyntaxTreeNode) simplified.remove(offset + 1);
                    simplified.remove(offset); // We already have the operator

                    AbstractSyntaxTreeNode combined = new Operation(currentOperatorType, a, b);
                    simplified.set(offset - 1, combined);

                    // Offset is not in the position of the next operator, so it does not need to move

                } else {
                    offset += 2; // Move offset to next operator
                }
            }
        }

        System.out.println(simplified);
        System.out.println();
        assert simplified.size() == 1; // Everything should have been combined to one after second stage
        return (AbstractSyntaxTreeNode) simplified.get(0);
    }

    private void error() {

    }
}