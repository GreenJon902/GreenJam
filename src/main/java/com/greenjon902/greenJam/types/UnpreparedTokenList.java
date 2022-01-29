package com.greenjon902.greenJam.types;

import java.util.ArrayList;

public class UnpreparedTokenList {
    private final ArrayList<UnpreparedToken> unpreparedTokens = new ArrayList<>();

    @Override
    public String toString() {
        return "UnpreparedTokenList{" +
                "unpreparedTokens=" + unpreparedTokens +
                '}';
    }

    public void append(UnpreparedToken unpreparedToken) {
        unpreparedTokens.add(unpreparedToken);
    }

    public UnpreparedToken[] toArray() {
        return unpreparedTokens.toArray(new UnpreparedToken[0]);
    }
}
