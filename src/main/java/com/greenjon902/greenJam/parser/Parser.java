package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.parser.commands.CommandAdd;
import com.greenjon902.greenJam.parser.commands.CommandWriteToStream;
import com.greenjon902.greenJam.tokenizer.*;

import java.util.ArrayList;

public class Parser {
    private TokenStream tokens;

    public Parser(TokenStream tokens) {
        this.tokens = tokens;
    }

    public AbstractSyntaxTree getParsed() {
        return new AbstractSyntaxTree(parseCodeBlock());
    }

    private AbstractSyntaxTreeNode parseCodeBlock() {
        ArrayList<AbstractSyntaxTreeNode> codeBlock = new ArrayList<>();

        while (tokens.hasNext()) {
            System.out.println(codeBlock);
            codeBlock.add(parseStatement());
            tokens.consume(TokenType.LINE_END);
        }

        return new CodeBlock(codeBlock);
    }

    private AbstractSyntaxTreeNode parseStatement() {
        if (tokens.next(1).isOperator(OperatorType.SET_VARIABLE)) {
            Identifier variableName = new Identifier((String) tokens.consume().primaryStorage);
            tokens.consume(); // We've already checked for OperatorType.SET_VARIABLE
            AbstractSyntaxTreeNode value = parseStatement();

            return new Operation(OperatorType.SET_VARIABLE, variableName, value);
        }

        return parseExpression();

    }

    private AbstractSyntaxTreeNode parseExpression() {
        // Convert to simple arithmetic by removing brackets and function calls -------------------
        ArrayList<Object> simplified = new ArrayList<>();

        AbstractSyntaxTreeNode current;
        current = null;

        while (true) {

            // Handles new current (and brackets)
            if (current == null) {
                if (tokens.consumeIf(TokenType.BRACKET, BracketType.ROUND_OPEN)) { // This does the brackets
                    current = parseExpression();
                    tokens.consume(TokenType.BRACKET, BracketType.ROUND_CLOSE);

                } else { // This does the new current
                    if (tokens.next().type == TokenType.LITERAL) {
                        current = new Literal((String) tokens.consume().primaryStorage);

                    } else if (tokens.next().type == TokenType.IDENTIFIER) {
                        current = new Identifier((String) tokens.consume().primaryStorage);

                    } else if (tokens.next().type == TokenType.COMMAND) {
                        current = parseCommand(); // ParseCommand needs next to be the command so don't consume

                    } else {
                        throw new RuntimeException();
                    }
                }

            } else { // TODO: Function calls
                simplified.add(current);

                if (!tokens.hasNext()) break;
                Token next = tokens.next();
                if (next.type != TokenType.OPERATOR) {
                    break;
                }
                simplified.add(next.primaryStorage); // Don't convert to AbstractSyntaxTreeNode yet as we can use OperatorType#combinedAt(int)
                tokens.consume();

                current = null;
            }
        }


        // It is now a simple maths, so we can apply all other operators while looping through the list ----------------
        int location;
        for (int precedence_level = 0; precedence_level <= OperatorType.highest_precedence; precedence_level++) {
            location = 1;
            while (location < simplified.size()) {

                OperatorType currentOperatorType = (OperatorType) simplified.get(location);
                if (currentOperatorType.combinedAt(precedence_level)) {
                    AbstractSyntaxTreeNode a = (AbstractSyntaxTreeNode) simplified.get(location - 1); // This is at location that
                                                                                                      // is turning into combined
                    AbstractSyntaxTreeNode b = (AbstractSyntaxTreeNode) simplified.remove(location + 1);
                    simplified.remove(location); // We already have the operator

                    AbstractSyntaxTreeNode combined = new Operation(currentOperatorType, a, b);
                    simplified.set(location - 1, combined);

                    // Offset is not in the position of the next operator, so it does not need to move

                } else {
                    location += 2; // Move offset to next operator
                }
            }
        }
        assert simplified.size() == 1; // Everything should have been combined to one after second stage
        return (AbstractSyntaxTreeNode) simplified.get(0);
    }

    /**
     * Parse a command. This requires the command token to be at the next location.
     */
    private AbstractSyntaxTreeNode parseCommand() {
        CommandType commandType = (CommandType) tokens.consume().primaryStorage;

        return switch (commandType) {
            case ADD -> {
                AbstractSyntaxTreeNode input_1 = parseExpression();
                AbstractSyntaxTreeNode input_2 = parseExpression();
                if (tokens.next().type == TokenType.IDENTIFIER) { // We have an output
                    Identifier output = new Identifier((String) tokens.consume().primaryStorage); // We are storing something in a certain
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