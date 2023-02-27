package com.greenjon902.greenJam.parser.syntaxMatcher;

import com.greenjon902.greenJam.common.*;
import com.greenjon902.greenJam.instructionHandler.InstructionToken;

import java.util.Objects;

/**
 * This is a syntax rule that optimizes the parsing of complex expressions which can be parsed with a
 * {@link SimpleSyntaxRule} but faster. It only supports operations with two operands (given as an
 * {@link InstructionToken.InstructionTokenType#IDENTIFIER}) and one operator (also given as an
 * {@link InstructionToken.InstructionTokenType#IDENTIFIER}).
 */
public class ExpressionSyntaxRule extends SyntaxRule {
    private final String operandGroup;
    private final String operatorGroup;
    private final boolean operandAsString;
    private final boolean operatorAsString;

    public ExpressionSyntaxRule(String operandGroup, String operatorGroup) {
        this(operandGroup, operatorGroup, false, false);
    }

    public ExpressionSyntaxRule(String operandGroup, String operatorGroup, boolean operandAsString, boolean operatorAsString) {
        this.operandGroup = operandGroup;
        this.operatorGroup = operatorGroup;
        this.operandAsString = operandAsString;
        this.operatorAsString = operatorAsString;
    }



    @Override
    public AstNode match(StringInputStream string, SyntaxContext syntaxContext) {
        while (string.consumeIfAny(syntaxContext.getIgnored()));

        Object last = getOperand(string, syntaxContext);
        if (last == null) {
            return null;
        }
        int lastPrecedence = -1; // No operator yet so set it to the smallest precedence

        while (true) {
            int stringLocationSave = string.location;
            Tuple.Two<Object, Integer> operatorAndPrecedence = matchOperatorWithPrecedence(string, syntaxContext);
            Object next = getOperand(string, syntaxContext);
            if (operatorAndPrecedence == null || next == null) {
                string.location = stringLocationSave;
                break; // Expression end
            }

            Object operator = operatorAndPrecedence.A;
            int operatorPrecedence = operatorAndPrecedence.B;

            if (lastPrecedence < operatorPrecedence) { // If lest one is evaluated first (also takes the -1 into account)
                last = new AstNode(operator, last, next);
            } else {
                ((AstNode) last).storage[2] = new AstNode(operator, ((AstNode) last).storage[2], next);
            }

            lastPrecedence = operatorPrecedence;
        }

        return (last instanceof AstNode) ? ((AstNode) last) : new AstNode(last);
    }

    /**
     * Tries to match an operand. Returns an object as can return {@link String} or {@link AstNode} depending on
     * {@link #operandAsString}.
     */
    public Object getOperand(StringInputStream string, SyntaxContext syntaxContext) {
        int stringStartLocation = string.location;
        AstNode operand = SyntaxRule.match(string, operandGroup, syntaxContext);
        if (operand == null) {
            return null;
        } else if (operandAsString) {
            return string.string.substring(stringStartLocation, string.location);
        } else {
            return operand;
        }
    }

    /**
     * Tries to match an operator and return with the precedence of the operator (order of operations, like how *
     * is before +) (lower means evaluate first (the *)). Returns null if none were found. Returns an object as can
     * return {@link String} or {@link AstNode} depending on {@link #operatorAsString}.
     */
    public Tuple.Two<Object, Integer> matchOperatorWithPrecedence(StringInputStream string, SyntaxContext syntaxContext) {
        int precedence = 0;
        for (SyntaxRule syntaxRule : syntaxContext.getRules(operatorGroup)) {

            int stringStartLocation = string.location;
            AstNode astNode = syntaxRule.match(string, syntaxContext);
            if (astNode != null) {
                return new Tuple.Two<>((operatorAsString ?
                        string.string.substring(stringStartLocation, string.location) : astNode),
                    precedence);
            }
            precedence += 1;
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionSyntaxRule that = (ExpressionSyntaxRule) o;
        return operandAsString == that.operandAsString && operatorAsString == that.operatorAsString && operandGroup.equals(that.operandGroup) && operatorGroup.equals(that.operatorGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operandGroup, operatorGroup, operandAsString, operatorAsString);
    }

    @Override
    public String toString() {
        return "ExpressionSyntaxRule{" +
                "operandGroup='" + operandGroup + '\'' +
                ", operatorGroup='" + operatorGroup + '\'' +
                ", operandAsString=" + operandAsString +
                ", operatorAsString=" + operatorAsString +
                '}';
    }

    @Override
    public String format() {
        return "<1{" + operandGroup + "}>1 <0{" + operatorGroup + "}>0 <2{" + operandGroup + "}>2";
    }
}
