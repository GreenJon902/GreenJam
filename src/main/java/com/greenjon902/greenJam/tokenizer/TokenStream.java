package com.greenjon902.greenJam.tokenizer;

public class TokenStream {
    private int location = 0;
    private final Token[] tokens;

    public TokenStream(Token[] tokens) {
        this.tokens = tokens;
    }

    /**
     * Are the more tokens to go through?
     */
    public boolean hasNext() {
        return location < tokens.length;
    }

    /**
     * {@link #consume() Consume}s the next token if it is of the specified type and does not hold the specified value.
     * @throws RuntimeException If the next token is not of the specified type or has a different primaryStorage.
     */
    public Token consume(TokenType tokenType, Object primaryStorage) throws RuntimeException {
        Token token = tokens[location];
        if (token.type != tokenType || token.primaryStorage != primaryStorage) {
            throw new RuntimeException("Failed to consume token as is not of type " + tokenType);
        }
        location += 1;
        return token;
    }

    /**
     * {@link #consume() Consume}s the next token if it is of the specified type.
     * @throws RuntimeException If the next token is not of the specified type.
     */
    public Token consume(TokenType tokenType) throws RuntimeException {
        Token token = tokens[location];
        if (token.type != tokenType) {
            throw new RuntimeException("Failed to consume token as is not of type " + tokenType);
        }
        location += 1;
        return token;
    }

    /**
     * {@link #consume() Consume}s the next token if it is of the specified type and does not hold the specified value.
     * @return Whether a token was consumed or not.
     */
    public boolean consumeIf(TokenType tokenType, Object primaryStorage) {
        Token token = tokens[location];
        if (token.type == tokenType && token.primaryStorage == primaryStorage) {
            location += 1;
            return true;
        }
        return false;
    }

    /**
     * {@link #consume() Consume}s the next token if it is of the specified type.
     * @return Whether a token was consumed or not.
     */
    public boolean consumeIf(TokenType tokenType) {
        Token token = tokens[location];
        if (token.type == tokenType) {
            location += 1;
            return true;
        }
        return false;
    }

    /**
     * {@link #consume() Consume}s the number of tokens that was given.
     */
    public void consume(int number) throws RuntimeException {
        location += number;
    }

    /**
     * Consumes the next token and returns it. This means it is effectively removed as the location is move to the next
     * value.
     */
    public Token consume() throws RuntimeException {
        Token token = tokens[location];
        location += 1;
        return token;
    }

    /**
     * Gets the token that is number after the current location.
     */
    public Token next(int number) {
        return tokens[location + number];
    }

    /**
     * Gets the next token.
     */
    public Token next() {
        return next(0);
    }
}
