package com.greenjon902.greenJam.instructionHandler;

import com.greenjon902.greenJam.common.Contexts;
import com.greenjon902.greenJam.common.ErrorContext;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.parser.ParserContext;
import com.greenjon902.greenJam.parser.syntaxMatcher.SimpleSyntaxRule;

import java.util.function.Consumer;

import static com.greenjon902.greenJam.instructionHandler.InstructionToken.InstructionTokenType.*;

public class StandardInstructionHandler extends InstructionHandlerBase {
    private final SyntaxContext syntaxContext;
    private final ParserContext parserContext;
    private final ErrorContext errorContext;

    public StandardInstructionHandler(Contexts contexts) {
        syntaxContext = contexts.syntax;
        parserContext = contexts.parser;
        errorContext = contexts.error;

        final Consumer<InstructionToken[]> syntaxRuleAddSimple = (InstructionToken[] instructions) -> syntaxContext.add((String) instructions[0].storage, (SimpleSyntaxRule) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveSimple = (InstructionToken[] instructions) -> syntaxContext.remove((String) instructions[0].storage, (SimpleSyntaxRule) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleAddLink = (InstructionToken[] instructions) -> syntaxContext.addLink((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveLink = (InstructionToken[] instructions) -> syntaxContext.removeLink((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> syntaxRuleAddRepeating = (InstructionToken[] instructions) -> syntaxContext.addRepeating((String) instructions[0].storage, (String) instructions[1].storage, (boolean) instructions[2].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveRepeating = (InstructionToken[] instructions) -> syntaxContext.removeRepeating((String) instructions[0].storage, (String) instructions[1].storage, (boolean) instructions[2].storage);
        final Consumer<InstructionToken[]> syntaxRuleAddJoin = (InstructionToken[] instructions) -> syntaxContext.addJoin((String) instructions[0].storage, (String) instructions[1].storage, (String) instructions[2].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveJoin = (InstructionToken[] instructions) -> syntaxContext.removeJoin((String) instructions[0].storage, (String) instructions[1].storage, (String) instructions[2].storage);
        final Consumer<InstructionToken[]> syntaxRuleAddExpression = (InstructionToken[] instructions) -> syntaxContext.addExpression((String) instructions[0].storage, (String) instructions[1].storage, (boolean) instructions[2].storage, (String) instructions[3].storage, (boolean) instructions[4].storage, (boolean) instructions[5].storage);
        final Consumer<InstructionToken[]> syntaxRuleRemoveExpression = (InstructionToken[] instructions) -> syntaxContext.removeExpression((String) instructions[0].storage, (String) instructions[1].storage, (boolean) instructions[2].storage, (String) instructions[3].storage, (boolean) instructions[4].storage, (boolean) instructions[5].storage);
        final Consumer<InstructionToken[]> syntaxIgnoredAdd = (InstructionToken[] instructions) -> syntaxContext.ignore((String) instructions[0].storage);
        final Consumer<InstructionToken[]> syntaxIgnoredRemove = (InstructionToken[] instructions) -> syntaxContext.removeIgnore((String) instructions[0].storage);

        final Consumer<InstructionToken[]> assignErrorToGroup = (InstructionToken[] instructions) -> errorContext.assignError((String) instructions[0].storage, (String) instructions[1].storage, (String) instructions[2].storage);
        final Consumer<InstructionToken[]> createReturningNullErrorCondition = (InstructionToken[] instructions) -> errorContext.createReturningNull((String) instructions[0].storage);
        final Consumer<InstructionToken[]> createNextIsErrorCondition = (InstructionToken[] instructions) -> errorContext.createNextIs((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> createNotErrorCondition = (InstructionToken[] instructions) -> errorContext.createNot((String) instructions[0].storage, (String) instructions[1].storage);
        final Consumer<InstructionToken[]> createAndErrorCondition = (InstructionToken[] instructions) -> errorContext.createAnd((String) instructions[0].storage, (String) instructions[1].storage, (String) instructions[2].storage);

        final Consumer<InstructionToken[]> setRootNode = (InstructionToken[] instructions) -> syntaxContext.setRootGroup((String) instructions[0].storage);

        final InstructionToken SYNTAX = InstructionKeyword.SYNTAX.instructionToken;
        final InstructionToken RULE = InstructionKeyword.RULE.instructionToken;
        final InstructionToken IGNORED = InstructionKeyword.IGNORED.instructionToken;
        final InstructionToken ADD = InstructionKeyword.ADD.instructionToken;
        final InstructionToken REMOVE = InstructionKeyword.REMOVE.instructionToken;
        final InstructionToken SET = InstructionKeyword.SET.instructionToken;
        final InstructionToken LINK = InstructionKeyword.LINK.instructionToken;
        final InstructionToken REPEATING = InstructionKeyword.REPEATING.instructionToken;
        final InstructionToken EXPRESSIONS = InstructionKeyword.EXPRESSIONS.instructionToken;
        final InstructionToken JOIN = InstructionKeyword.JOIN.instructionToken;
        final InstructionToken ROOT_NODE = InstructionKeyword.ROOT_NODE.instructionToken;
        final InstructionToken ERROR = InstructionKeyword.ERROR.instructionToken;
        final InstructionToken ASSIGN = InstructionKeyword.ASSIGN.instructionToken;
        final InstructionToken ERROR_CONDITION = InstructionKeyword.ERROR_CONDITION.instructionToken;
        final InstructionToken RETURNING_NULL = InstructionKeyword.RETURNING_NULL.instructionToken;
        final InstructionToken NEXT_IS = InstructionKeyword.NEXT_IS.instructionToken;
        final InstructionToken NOT = InstructionKeyword.NOT.instructionToken;
        final InstructionToken AND = InstructionKeyword.AND.instructionToken;
        final InstructionToken CREATE = InstructionKeyword.CREATE.instructionToken;

        final InstructionToken SYNTAX_RULE_ARG = InstructionToken.makeArgument(SYNTAX_RULE);
        final InstructionToken IDENTIFIER_ARG = InstructionToken.makeArgument(IDENTIFIER);
        final InstructionToken BOOLEAN_ARG = InstructionToken.makeArgument(BOOLEAN);
        final InstructionToken STRING_ARG = InstructionToken.makeArgument(STRING);

        addPathway(syntaxRuleAddSimple, SYNTAX, RULE, ADD, IDENTIFIER_ARG, SYNTAX_RULE_ARG);
        addPathway(syntaxRuleRemoveSimple, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, SYNTAX_RULE_ARG);
        addPathway(syntaxRuleAddLink, SYNTAX, RULE, ADD, LINK, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxRuleRemoveLink, SYNTAX, RULE, REMOVE, LINK, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxRuleAddRepeating, SYNTAX, RULE, ADD, IDENTIFIER_ARG, REPEATING, IDENTIFIER_ARG, BOOLEAN_ARG);
        addPathway(syntaxRuleRemoveRepeating, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, REPEATING, IDENTIFIER_ARG, BOOLEAN_ARG);
        addPathway(syntaxRuleAddExpression, SYNTAX, RULE, ADD, IDENTIFIER_ARG, EXPRESSIONS, IDENTIFIER_ARG, BOOLEAN_ARG, IDENTIFIER_ARG, BOOLEAN_ARG, BOOLEAN_ARG);
        addPathway(syntaxRuleRemoveExpression, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, EXPRESSIONS, IDENTIFIER_ARG, BOOLEAN_ARG, IDENTIFIER_ARG, BOOLEAN_ARG, BOOLEAN_ARG);
        addPathway(syntaxRuleAddJoin, SYNTAX, RULE, ADD, IDENTIFIER_ARG, JOIN, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxRuleRemoveJoin, SYNTAX, RULE, REMOVE, IDENTIFIER_ARG, JOIN, IDENTIFIER_ARG, IDENTIFIER_ARG);
        addPathway(syntaxIgnoredAdd, SYNTAX, IGNORED, ADD, STRING_ARG);
        addPathway(syntaxIgnoredRemove, SYNTAX, IGNORED, REMOVE, STRING_ARG);

        addPathway(assignErrorToGroup, ERROR, ASSIGN, IDENTIFIER_ARG, IDENTIFIER_ARG, STRING_ARG);
        addPathway(createReturningNullErrorCondition, ERROR_CONDITION, CREATE, IDENTIFIER_ARG, RETURNING_NULL);
        addPathway(createNextIsErrorCondition, ERROR_CONDITION, CREATE, IDENTIFIER_ARG, NEXT_IS, STRING_ARG);
        addPathway(createNotErrorCondition, ERROR_CONDITION, CREATE, IDENTIFIER_ARG, NOT, IDENTIFIER_ARG);
        addPathway(createAndErrorCondition, ERROR_CONDITION, CREATE, IDENTIFIER_ARG, AND, IDENTIFIER_ARG, IDENTIFIER_ARG);

        addPathway(setRootNode, ROOT_NODE, SET, IDENTIFIER_ARG);
    }
}
