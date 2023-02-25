package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.common.SyntaxInstruction;
import com.greenjon902.greenJam.common.SyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.LinkSyntaxRule;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;

import java.util.function.Consumer;

import static com.greenjon902.greenJam.instructionHandler.InstructionToken.InstructionTokenType.*;

public class StandardInstructionHandler extends InstructionHandlerBase {
    private final SyntaxContext syntaxContext;

    public StandardInstructionHandler(SyntaxContext syntaxContext) {
        this.syntaxContext = syntaxContext;

        final Consumer<InstructionToken[]> syntaxRuleAddSimple = (InstructionToken[] instructions) -> syntaxContext.add((String) instructions[0].storage, (SimpleSyntaxRule) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveSimple = (InstructionToken[] instructions) -> syntaxContext.remove((String) instructions[0].storage, (SimpleSyntaxRule) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleAddLink = (InstructionToken[] instructions) -> syntaxContext.addLink((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveLink = (InstructionToken[] instructions) -> syntaxContext.removeLink((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxIgnoredAdd = (InstructionToken[] instructions) -> syntaxContext.ignore((String) instructions[0].storage);
        final Consumer<InstructionToken[]> syntaxIgnoredRemove = (InstructionToken[] instructions) -> syntaxContext.removeIgnore((String) instructions[0].storage);

        final InstructionToken SYNTAX = InstructionKeyword.SYNTAX.instructionToken;
        final InstructionToken RULE = InstructionKeyword.RULE.instructionToken;
        final InstructionToken IGNORED = InstructionKeyword.IGNORED.instructionToken;
        final InstructionToken ADD = InstructionKeyword.ADD.instructionToken;
        final InstructionToken REMOVE = InstructionKeyword.REMOVE.instructionToken;
        final InstructionToken SYNTAX_RULE_ARG = InstructionToken.makeArgument(SYNTAX_RULE);
        final InstructionToken IDENTIFIER_ARG = InstructionToken.makeArgument(IDENTIFIER);
        final InstructionToken STRING_ARG = InstructionToken.makeArgument(STRING);

        addPathway(syntaxRuleAddSimple, SYNTAX, RULE, ADD, IDENTIFIER_ARG, SYNTAX_RULE_ARG);
        addPathway(syntaxRuleRemoveSimple, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, SYNTAX_RULE_ARG);
        addPathway(syntaxRuleAddLink, SYNTAX, RULE, ADD, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxRuleRemoveLink, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxIgnoredAdd, SYNTAX, IGNORED, ADD, STRING_ARG);
        addPathway(syntaxIgnoredRemove, SYNTAX, IGNORED, REMOVE, STRING_ARG);
    }
}