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
                if (tokens[location + offset + 2].isBracket(BracketType.ROUND_OPEN)) { // This does the brackets
                    location += offset + 3;
                    System.out.println("start bracklets: ");
                    System.out.println(current);
                    System.out.println(tokens[location]);

                    current = parseExpression();
                    offset = 1;  // It's the close bracket
                    System.out.println("end bracklets: ");
                    System.out.println(current);
                    System.out.println(tokens[location + offset]);

                } else {
                    System.out.println("new current of: ");
                    System.out.println(tokens[location + offset]);
                    current = new Identifier((String) tokens[location + offset].primaryStorage);
                }


            // Attribute getting
            } else if (tokens[location + offset + 1].isOperator(OperatorType.GET_ATTRIBUTE)) { // Next operator is get attr
                current = new Operation(OperatorType.GET_ATTRIBUTE, current,
                        new Identifier((String) tokens[location + offset + 2].primaryStorage));
                offset += 2;


            } else {  // TODO: Function Calls
                simplified.add(current);
                offset += 1;
                Token next = tokens[location + offset];
                if (next.type != TokenType.OPERATOR) {
                    System.out.println(next);
                    break;
                }
                simplified.add(next);
                offset += 1;

                current = null;
            }
        }
        System.out.println(simplified);
        return new Test(simplified);
    }

    private void error() {

    }
}

class Test extends AbstractSyntaxTreeNode {
    ArrayList<Object> test;


    public Test(ArrayList<Object> simplified) {
        this.test = simplified;
    }

    @Override
    public String toString() {
        return "Test{" +
                "test=" + test +
                '}';
    }
}