package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SyntaxMatcher {
    public static AstNode match(StringInputStream string, String group, SyntaxContext syntaxContext) {
        for (SyntaxRule syntaxRule : syntaxContext.getRules(group)) {
            AstNode astNode = match(string, syntaxRule, syntaxContext);
            if (astNode != null) {
                return astNode;
            }
        }
        return null;
    }

    public static AstNode match(StringInputStream string, SyntaxRule rule, SyntaxContext syntaxContext) {
        int stringLocationSave = string.location;

        HashMap<Integer, Integer> currentRecordings = new HashMap<>();
        Object[] memoryLocations = new Object[rule.memoryLocations];

        for (int instructionIndex = 0; instructionIndex < rule.syntaxInstructions.length; instructionIndex++) {
            SyntaxInstruction instruction = rule.syntaxInstructions[instructionIndex];
            Object data = rule.syntaxInstructionData[instructionIndex];

            switch (instruction) {
                case MATCH_LITERAL -> {
                    if (!string.consumeIf((String) data)) {
                        string.location = stringLocationSave;
                        return null;
                    }
                }
                case START_RECORD -> {
                    if (currentRecordings.containsKey((Integer) data)) Errors.syntaxMatcher_alreadyRecording(string, rule, (Integer) data);
                    currentRecordings.put((Integer) data, string.location);
                }
                case STOP_RECORD -> {
                    if (!currentRecordings.containsKey((Integer) data)) Errors.syntaxMatcher_triedToStopRecordingWhenNotRecording(string, rule, (Integer) data);
                    if (memoryLocations[(Integer) data] == null) memoryLocations[(Integer) data] = "";
                    memoryLocations[(Integer) data] += string.string.substring(currentRecordings.get(data), string.location);
                    currentRecordings.remove((Integer) data);
                }
                case MATCH_GROUP -> {
                    if (!syntaxContext.hasGroup((String) data)) Errors.syntaxMatcher_unknownGroup(string, rule, (String) data);
                    AstNode astNode = match(string, (String) data, syntaxContext);
                    if (astNode == null) {
                        string.location = stringLocationSave;
                        return null;
                    }
                }
                case RECORD_GROUP -> {
                    if (!syntaxContext.hasGroup(((Tuple.Two<Integer, String>) data).B)) Errors.syntaxMatcher_unknownGroup(string, rule, ((Tuple.Two<Integer, String>) data).B);
                    if (memoryLocations[(Integer) data] != null) Errors.syntaxMatcher_triedToRerecordNode(string, rule, ((Tuple.Two<Integer, String>) data).A, ((Tuple.Two<Integer, String>) data).B);;
                    AstNode astNode = match(string, ((Tuple.Two<Integer, String>) data).B, syntaxContext);
                    if (astNode == null) {
                        string.location = stringLocationSave;
                        return null;
                    }
                    memoryLocations[((Tuple.Two<Integer, String>) data).A] = astNode;


                }
                default -> throw new RuntimeException();
            }
        }

        return new AstNode(memoryLocations);
    }
}
