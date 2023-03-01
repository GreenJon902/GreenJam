package com.greenjon902.greenJam.parser;

import com.greenjon902.greenJam.common.AstNode;
import com.greenjon902.greenJam.common.StringInputStream;
import com.greenjon902.greenJam.common.SyntaxContext;
import com.greenjon902.greenJam.instructionHandler.StandardInstructionHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {
    @Test
    void testSampleScript() {
        SyntaxContext syntaxContext = new SyntaxContext();
        StandardInstructionHandler instructionHandler = new StandardInstructionHandler(syntaxContext);
        Parser parser = new Parser(instructionHandler, syntaxContext);

        StringInputStream string = new StringInputStream("<TestFile.jam>",
                """
                        ;; SYNTAX IGNORED ADD "\\n";
                        
                        ;; SYNTAX RULE ADD character `a`;
                        ;; SYNTAX RULE ADD character `f`;
                        ;; SYNTAX RULE ADD character `o`;
                        
                        ;; SYNTAX RULE ADD word REPEATING character;
                        ;; SYNTAX RULE ADD identifier `<[word]>`;
                        
                        ;; SYNTAX RULE ADD keyword `<0public>0 `;
                        ;; SYNTAX RULE ADD keyword `<0private>0 `;
                        ;; SYNTAX RULE ADD keyword `<0static>0 `;
                        ;; SYNTAX RULE ADD keywords REPEATING keyword;
                        
                        ;; SYNTAX RULE ADD number `1`;
                        ;; SYNTAX RULE ADD number `2`;
                        ;; SYNTAX RULE ADD number `3`;
                        ;; SYNTAX RULE ADD number `4`;
 
                        ;; SYNTAX RULE ADD list_item `<[number]>, `;
                        ;; SYNTAX RULE ADD list_contents REPEATING list_item;
                        ;; SYNTAX RULE ADD list `\\{{0list_contents}<1[number]>1\\}`;
                        
                        ;; SYNTAX RULE ADD variable_declaration `{1keywords}{0identifier}`;
                        ;; SYNTAX RULE ADD variable_declaration `{identifier}`;
                        ;; SYNTAX RULE ADD variable_assignment `{0variable_declaration} = {1list}`;
                        
                        ;; ROOT_NODE SET variable_assignment;
                        
                        public static foo = {1, 2, 3, 4}
                        """
        );
        AstNode node = parser.parse(string);
        System.out.println(node.toString());
        System.out.println(node.format());

        assertTrue(string.isEnd());
    }
}