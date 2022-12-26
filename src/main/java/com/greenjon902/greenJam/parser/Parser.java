package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.parser.commands.CommandAdd;
import com.greenjon902.greenJam.parser.commands.CommandWriteToStream;
import com.greenjon902.greenJam.tokenizer.*;

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
        ArrayList<AbstractSyntaxTreeNode> codeBlock = new ArrayList<>();

        while (location < tokens.length) {
            System.out.println(codeBlock);
            codeBlock.add(parseStatement());
            location += 1; // Line end
        }

        return new CodeBlock(codeBlock);
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

        // Convert to simple arithmetic by removing brackets and function calls -------------------
        ArrayList<Object> simplified = new ArrayList<>();

        AbstractSyntaxTreeNode current;
        current = null;

        while (true) {

            // Handles new current (and brackets)
            if (current == null) {
                if (tokens[location + offset].isBracket(BracketType.ROUND_OPEN)) { // This does the brackets
                    location += offset + 1;

                    current = parseExpression();
                    offset = 1;  // It's the close bracket

                } else { // This does the new current
                    if (tokens[location + offset].type == TokenType.LITERAL) {
                        current = new Literal((String) tokens[location + offset].primaryStorage);

                    } else if (tokens[location + offset].type == TokenType.IDENTIFIER) {
                        current = new Identifier((String) tokens[location + offset].primaryStorage);

                    } else if (tokens[location + offset].type == TokenType.COMMAND) {
                        location += offset;
                        current = parseCommand();

                    } else {
                        throw new RuntimeException();
                    }
                    offset += 1;
                }

            } else { // TODO: Function calls
                simplified.add(current);

                if (!(location + offset < tokens.length)) break;
                Token next = tokens[location + offset];
                if (next.type != TokenType.OPERATOR) {
                    break;
                }
                simplified.add(next.primaryStorage); // Keep the operator as a token as we can use OperatorType#combinedAt(int)
                offset += 1;

                current = null;
            }
        }
        location += offset;


        // It is now a simple maths, so we can apply all other operators while looping through the list ----------------
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
        assert simplified.size() == 1; // Everything should have been combined to one after second stage
        return (AbstractSyntaxTreeNode) simplified.get(0);
    }

    private AbstractSyntaxTreeNode parseCommand() {
        CommandType commandType = (CommandType) tokens[location].primaryStorage;
        location += 1;

        return switch (commandType) {
            case ADD -> {
                AbstractSyntaxTreeNode input_1 = parseExpression();
                AbstractSyntaxTreeNode input_2 = parseExpression();
                if (tokens[location].type == TokenType.IDENTIFIER) { // We have an output
                    Identifier output = new Identifier((String) tokens[location].primaryStorage); // We are storing something in a certain
                                                                                                  // location so has to be an identifier
                    yield new CommandAdd(input_1, input_2, output);
                } else {
                    yield new CommandAdd(input_1, input_2);
                }
            }
            case WRITE_TO_STREAM -> {
                AbstractSyntaxTreeNode stream = parseExpression();
                AbstractSyntaxTreeNode data = parseExpression();

                yield new CommandWriteToStream(stream, data);
            }
        };
    }

    private void error() {

    }
}