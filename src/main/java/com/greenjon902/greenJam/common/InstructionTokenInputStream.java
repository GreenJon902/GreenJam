package com.greenjon902.greenJam.common;

import com.greenjon902.greenJam.instructionHandler.InstructionKeyword;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

public class InstructionTokenInputStream {

    public final InstructionToken[] instructionTokens;
    public int location = 0;
    public final String fileName;
    public final int lineNumber;

    private String cachedString;
    private int cachedStringLocation;
    private int cachedStringLength;
    private int cacheLocation = -1;

    public InstructionTokenInputStream(String fileName, int lineNumber, InstructionToken[] instructionTokens) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.instructionTokens = instructionTokens;
    }

    public String format() {
        updateCachedInfo();
        return cachedString;
    }

    public int getStringPosition() {
        updateCachedInfo();
        return cachedStringLocation;
    }

    public int getStringLength() {
        updateCachedInfo();
        return cachedStringLength;
    }

    public void updateCachedInfo() {
        if (cacheLocation != location) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < instructionTokens.length; i++) {
                InstructionToken instructionToken = instructionTokens[i];

                if (location == i) {
                    cachedStringLocation = stringBuilder.length();
                }
                switch (instructionToken.type) {
                    case KEYWORD -> stringBuilder.append(((InstructionKeyword) instructionToken.storage).string);
                    case IDENTIFIER -> stringBuilder.append(((String) instructionToken.storage));
                    default -> stringBuilder.append(instructionToken.storage.toString());
                }
                if (location == i) {
                    cachedStringLength = stringBuilder.length() - cachedStringLocation;
                }

                stringBuilder.append(' ');
            }
            cacheLocation = location;
            cachedString = stringBuilder.toString();
        }
    }

    public InstructionToken next() {
        return instructionTokens[location];
    }

    public InstructionToken next(int n) {
        return instructionTokens[location + n];
    }

    public InstructionToken consume() {
        InstructionToken instructionToken = next();
        location += 1;
        return instructionToken;
    }

    public boolean hasLeft(int n) {
        return instructionTokens.length - location == n;
    }

    public String getString() {
        updateCachedInfo();
        return cachedString;
    }
}
