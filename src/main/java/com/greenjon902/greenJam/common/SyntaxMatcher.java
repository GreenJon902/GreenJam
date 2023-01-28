package com.greenjon902.greenJam.common;

public interface SyntaxMatcher {
    public AstNode parse(StringInputStream inputStream);
}
