package com.greenjon902.greenJam.parser;

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
                        ;; SYNTAX RULE ADD identifier `<|{word}> `;
                        
                        ;; SYNTAX RULE ADD keyword `<0public>0 `;
                        ;; SYNTAX RULE ADD keyword `<0private>0 `;
                        ;; SYNTAX RULE ADD keyword `<0static>0 `;
                        ;; SYNTAX RULE ADD keywords REPEATING keyword;
                        
                        ;; SYNTAX RULE ADD number `1`;
                        ;; SYNTAX RULE ADD number `2`;
                        ;; SYNTAX RULE ADD number `3`;
                        ;; SYNTAX RULE ADD number `4`;
 
                        ;; SYNTAX RULE ADD list_item `<0|{number}>0, `;
                        ;; SYNTAX RULE ADD list_contents REPEATING list_item;
                        ;; SYNTAX RULE ADD list `\\{<0{list_contents}>0<1|{number}>1\\}`;
                        
                        ;; SYNTAX RULE ADD variable_declaration `<1{keywords}>1<0{identifier}>0`;
                        ;; SYNTAX RULE ADD variable_declaration `<0{identifier}>0`;
                        ;; SYNTAX RULE ADD variable_assignment `<0{variable_declaration}>0= <1{list}>1`;
                        
                        ;; ROOT_NODE SET variable_assignment;
                        
                        public static foo = {1, 2, 3, 4}
                        """
        );

        System.out.println(parser.parse(string));

        assertTrue(string.isEnd());
    }
}
