package com.greenjon902.greenJam.types;

import com.greenjon902.greenJam.types.tokens.Token;

import java.util.ArrayList;

public class TokenList {
    private final ArrayList<Token> tokens = new ArrayList<>();

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }

    public void append(Token token) {
        tokens.add(token);
    }
}
