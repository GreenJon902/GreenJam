package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.parser.commands.CommandAdd;
import com.greenjon902.greenJam.parser.commands.CommandWriteToStream;
import com.greenjon902.greenJam.tokenizer.*;

import java.util.ArrayList;
import java.util.Set;

public class Parser {
    private TokenStream tokens;

    public Parser(TokenStream tokens) {
        this.tokens = tokens;
    }

    public AbstractSyntaxTree getParsed() {
        return new AbstractSyntaxTree(parseCodeBlock());
    }

    private CodeBlock parseCodeBlock() {
        ArrayList<AbstractSyntaxTreeNode> codeBlock = new ArrayList<>();

        while (tokens.hasNext() && !tokens.next().isBracket(BracketType.CURLY_CLOSE)) {
            parseLineInto(codeBlock);
        }

        return new CodeBlock(codeBlock);
    }

    private void parseLineInto(ArrayList<AbstractSyntaxTreeNode> codeBlock) {
        if (tokens.consumeIf(TokenType.LINE_END)) { // Ignore random line ends, we process one at end of line to make
                                                    // sure the programmer included one, but you cant stop them putting
                                                    // them when aren't needed! E.G.:
                                                    // `int foo() {return 1};` vs `int foo() {return 1}`
                                                    // So it's parsed for the second one and this if statement protects
                                                    // against the first one.
            return;
        }

        // Some things can only occur at the start of a line so are parsed separately --------------
        if (isDeclarer(tokens.next()) && tokens.next(1).type == TokenType.IDENTIFIER) {

            if (tokens.next(2).isBracket(BracketType.ROUND_OPEN)) { // Function Declaration -------
                AbstractSyntaxTreeNode returnType = getDeclarer(tokens.consume());
                Identifier functionName = new Identifier((String) tokens.consume(TokenType.IDENTIFIER).primaryStorage);
                tokens.consume(TokenType.BRACKET, BracketType.ROUND_OPEN);

                ArrayList<FunctionDeclaration.Argument> arguments = new ArrayList<>();
                if (!(tokens.next().isBracket(BracketType.ROUND_CLOSE))) { // Check there are any arguements
                    do {
                        arguments.add(new FunctionDeclaration.Argument(
                                getDeclarer(tokens.consume()),
                                new Identifier((String) tokens.consume(TokenType.IDENTIFIER).primaryStorage)));
                    } while (tokens.consumeIf(TokenType.COMMA));
                }
                tokens.consume(TokenType.BRACKET, BracketType.ROUND_CLOSE);

                tokens.consume(TokenType.BRACKET, BracketType.CURLY_OPEN);
                CodeBlock functionCodeBlock = parseCodeBlock();
                tokens.consume(TokenType.BRACKET, BracketType.CURLY_CLOSE);

                codeBlock.add(new FunctionDeclaration(functionName, returnType,
                        arguments.toArray(FunctionDeclaration.Argument[]::new), functionCodeBlock));

                return; // Doesn't require a line end



            } else { // Variable Declaration -------
                // Declaring and setting in same line results in two nodes:
                // int a = 5;
                // turns into {DECLARE, type="int", identifier="a"}, {SET, identifier="a", value=5}

                AbstractSyntaxTreeNode type = getDeclarer(tokens.consume());

                boolean isInizializing = tokens.next(1).isOperator(OperatorType.SET_VARIABLE);
                int savedLocation = tokens.currentLocation();

                Identifier[] identifiers = parseIdentifierList();
                codeBlock.add(new VariableDeclaration(type, identifiers));

                if (isInizializing) {
                    tokens.setLocation(savedLocation); // So it's back to the location of the variable name
                    codeBlock.add(parseExpression());
                }

            }


        } else if (tokens.next().isKeyword(KeywordName.IF)) { // If statement -------
            ArrayList<Conditional> conditionals = new ArrayList<>();

            CodeBlock elseCodeBlock = null;
            do {
                if (tokens.consumeIf(TokenType.KEYWORD, KeywordName.IF)) {

                    tokens.consume(TokenType.BRACKET, BracketType.ROUND_OPEN);
                    AbstractSyntaxTreeNode expression = parseExpression();
                    tokens.consume(TokenType.BRACKET, BracketType.ROUND_CLOSE);

                    tokens.consume(TokenType.BRACKET, BracketType.CURLY_OPEN);
                    CodeBlock conditionalCodeBlock = parseCodeBlock();
                    tokens.consume(TokenType.BRACKET, BracketType.CURLY_CLOSE);

                    conditionals.add(new Conditional(expression, conditionalCodeBlock));

                } else {
                    tokens.consume(TokenType.BRACKET, BracketType.CURLY_OPEN);
                    elseCodeBlock = parseCodeBlock();
                    tokens.consume(TokenType.BRACKET, BracketType.CURLY_CLOSE);
                    break;
                }
            } while  (tokens.consumeIf(TokenType.KEYWORD, KeywordName.ELSE));

            codeBlock.add(new If(conditionals.toArray(Conditional[]::new), elseCodeBlock));

            return; // Doesn't require a line end


        } else if (tokens.consumeIf(TokenType.KEYWORD, KeywordName.RETURN)) { // Return statement -------
            // Is it returning a value?
            if (tokens.next().type == TokenType.LINE_END) { // No
                codeBlock.add(new Return());
            } else { // Yes
                codeBlock.add(new Return(parseExpression()));
            }


        } else { // There is nothing special so parse stuff that can be in middle of line ---------------------
            codeBlock.add(parseExpression());
        }

        tokens.consume(TokenType.LINE_END);
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

    /**
     * Checks if a token can be used to declare a variable, e.g.
     * <pre>
     *     int a; // Uses a keyword that is a primitive
     *     String b; // Uses an identifier
     *     ) c; // Is not valid as bracket cannot hold a type
     *     return d; // Is not valid as return is a keyword but is not a type
     * </pre>
     */
    private boolean isDeclarer(Token token) {
        return (token.type == TokenType.IDENTIFIER) ||
                (token.type == TokenType.KEYWORD &&
                        ((KeywordName) token.primaryStorage).type == KeywordName.KeywordType.PRIMITIVE);
    }

    /**
     * Get a token that can be used a declarer, expects you have already checked with {@link #isDeclarer}.
     * <pre>
     *     int a; // Uses a keyword that is a primitive
     *     String b; // Uses an identifier
     * </pre>
     */
    private AbstractSyntaxTreeNode getDeclarer(Token token) {
        return (token.type == TokenType.IDENTIFIER) ?
                new Identifier((String) token.primaryStorage) :
                new Primitive((KeywordName) token.primaryStorage);
    }

    private void error() {

    }
}