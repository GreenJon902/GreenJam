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
            parseLineInto(codeBlock);
        }

        return new CodeBlock(codeBlock);
    }

    private void parseLineInto(ArrayList<AbstractSyntaxTreeNode> codeBlock) {
        //Variable declaration can only happen at the start of a line
        if (tokens.next().type == TokenType.IDENTIFIER && tokens.next(1).type == TokenType.IDENTIFIER) {
            // Declaring and setting in same line results in two nodes:
            // int a = 5;
            // turns into {DECLARE, type="int", identifier="a"}, {SET, identifier="a", value=5}

            Identifier type = new Identifier((String) tokens.consume().primaryStorage);

            boolean isInizializing = tokens.next(1).isOperator(OperatorType.SET_VARIABLE);
            int savedLocation = tokens.currentLocation();

            Identifier[] identifiers = parseIdentifierList();
            codeBlock.add(new VariableDeclaration(type, identifiers));

            if (isInizializing) {
                tokens.setLocation(savedLocation); // So it's back to the location of the variable name
                codeBlock.add(parseExpression());
            }

            tokens.consume(TokenType.LINE_END);

        } else {
            codeBlock.add(parseExpression());
            tokens.consume(TokenType.LINE_END);
        }
    }

    private AbstractSyntaxTreeNode parseExpression() {
        // Convert to simple arithmetic by removing brackets, attribute getting, and function calls -------------------
        // We do attribute getting in first bit as function calls come afterwards.
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

            // Handles variable assignments
            } else if (tokens.consumeIf(TokenType.OPERATOR, OperatorType.SET_VARIABLE)) {
                AbstractSyntaxTreeNode value = parseExpression();
                current = new Operation(OperatorType.SET_VARIABLE, current, value);

            // Handles attribute getting calls
            } else if (tokens.consumeIf(TokenType.OPERATOR, OperatorType.GET_ATTRIBUTE)) {
                current = new Operation(OperatorType.GET_ATTRIBUTE, current,
                        new Identifier((String) tokens.consume().primaryStorage));

            // Handles function calls
            } else if (tokens.consumeIf(TokenType.BRACKET, BracketType.ROUND_OPEN)) {
                current = new Operation(OperatorType.CALL, current, parseFunctionArguments());
                tokens.consume(TokenType.BRACKET, BracketType.ROUND_CLOSE);

            } else {
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
     * This will parse function arguments, this means it will try and parse until it gets
     * {@link BracketType#ROUND_CLOSE}. If there are no arguements then the {@link FunctionArguments#arguments}
     * will be empty.
     */
    private FunctionArguments parseFunctionArguments() {
        ArrayList<AbstractSyntaxTreeNode> arguments = new ArrayList<>();

        if (!(tokens.next().isBracket(BracketType.ROUND_CLOSE))) { // Check there are items in the list
            // No more comma, then args are over, next should be a bracket which gets consumed externally
            do {
                arguments.add(parseExpression());
            } while (tokens.consumeIf(TokenType.COMMA));
        }

        return new FunctionArguments(arguments.toArray(AbstractSyntaxTreeNode[]::new));
    }

    /**
     * Parse a list of just identifiers that are separated by {@link TokenType#COMMA}. This requires there to be at
     * least one identifier.
     */
    private Identifier[] parseIdentifierList() {
        ArrayList<Identifier> identifiers = new ArrayList<>();

        do {
            identifiers.add(new Identifier((String) tokens.consume(TokenType.IDENTIFIER).primaryStorage));
        } while (tokens.consumeIf(TokenType.COMMA));

        return identifiers.toArray(Identifier[]::new);
    }

    /**
     * Parse a command. This requires the command token to be at the next location.
     */
    private AbstractSyntaxTreeNode parseCommand() {
        CommandType commandType = (CommandType) tokens.consume().primaryStorage;

        return switch (commandType) {
            case ADD -> {
                AbstractSyntaxTreeNode input_1 = parseExpression();
                tokens.consume(TokenType.COMMA);
                AbstractSyntaxTreeNode input_2 = parseExpression();
                if (tokens.consumeIf(TokenType.COMMA)) { // We have an output
                    Identifier output = new Identifier((String) tokens.consume().primaryStorage); // We are storing something in a certain
                                                                                                  // location so has to be an identifier
                    yield new CommandAdd(input_1, input_2, output);
                } else {
                    yield new CommandAdd(input_1, input_2);
                }
            }
            case WRITE_TO_STREAM -> {
                AbstractSyntaxTreeNode stream = parseExpression();
                tokens.consume(TokenType.COMMA);
                AbstractSyntaxTreeNode data = parseExpression();

                yield new CommandWriteToStream(stream, data);
            }
        };
    }

    private void error() {

    }
}