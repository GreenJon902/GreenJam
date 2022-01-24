package com.greenjon902.greenJam.types;

import java.util.ArrayList;
import java.util.Arrays;

public class UnclassifiedTokenList {
    private final ArrayList<UnclassifiedToken> unclassifiedTokens = new ArrayList<>();

    @Override
    public String toString() {
        return "UnclassifiedTokenList{" +
                "unclassifiedTokens=" + unclassifiedTokens +
                '}';
    }

    public void append(UnclassifiedToken unclassifiedToken) {
        unclassifiedTokens.add(unclassifiedToken);
    }

    public UnclassifiedToken[] toArray() {
        return unclassifiedTokens.toArray(new UnclassifiedToken[0]);
    }
}
