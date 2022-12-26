import com.diogonunes.jcolor.AnsiFormat;
import com.greenjon902.greenJam.parser.*;
import com.greenjon902.greenJam.parser.commands.CommandWriteToStream;
import com.greenjon902.greenJam.tokenizer.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class CompileTests {
    AnsiFormat stageHeaderFormat = new AnsiFormat(BOLD(), GREEN_TEXT());
    AnsiFormat testInfoFormat = new AnsiFormat(GREEN_TEXT());
    AnsiFormat testWarningFormat = new AnsiFormat(RED_TEXT());

    /**
     * Test the compilation of a file at the path, it can check if tokens are current and if the string versions of
     * {@link AbstractSyntaxTree}s match. Use strings as expected as easier to make and
     * {@link Assertions#assertEquals(Object, Object)} gives a comparison between strings which is usefull for
     * debugging.
     */
    private void test(String path, String expectedTokens, String expectedAst) throws IOException {
        System.out.println(colorize("Reading \"" + path + "\"...", testInfoFormat));
        Scanner scan = new Scanner(new File(path));
        scan.useDelimiter("\\Z");
        String content = scan.next();

        System.out.println(colorize("Tokenizing...", testInfoFormat));
        Tokenizer tokenizer = new Tokenizer(content);
        Token[] tokens = tokenizer.getTokens();
        System.out.println(Arrays.toString(tokens));
        if (expectedTokens != null) {
            Assertions.assertEquals(expectedTokens, Arrays.toString(tokens));
        } else {
            System.out.println(colorize("No expected tokens given", testWarningFormat));
        }

        System.out.println(colorize("Parsing...", testInfoFormat));
        Parser parser = new Parser(new TokenStream(tokens));
        AbstractSyntaxTree ast = parser.getParsed();
        System.out.println(ast.prettyPrint());
        if (expectedAst != null) {
            // Strings are easier to check and get examples of, formatted version to make debugging easier.
            Assertions.assertEquals(expectedAst, ast.prettyPrint());
        } else {
            System.out.println(colorize("No expected ast given", testWarningFormat));
        }

        System.out.println("\n");
    }

    @Test
    public void testCompileCommands() throws IOException {
        System.out.println(colorize("Simple Command -----------", stageHeaderFormat));

        Token[] expectedTokens = {
                new Token(TokenType.COMMAND, CommandType.WRITE_TO_STREAM),
                new Token(TokenType.IDENTIFIER, "STD_OUT"),
                new Token(TokenType.LITERAL, "Testing"),
                new Token(TokenType.LINE_END, null)
        };
        AbstractSyntaxTree expectedAbstractSyntaxTree = new AbstractSyntaxTree(
                new CodeBlock(
                        List.of(
                                new CommandWriteToStream(
                                        new Identifier("STD_OUT"),
                                        new Literal("Testing")
                                )
                        )
                )
        );
        test("tests/simpleCommand.jam", Arrays.toString(expectedTokens),
                expectedAbstractSyntaxTree.prettyPrint());
    }

    @Test
    public void testCompileVariableAssignmentAndMath() throws IOException {
        System.out.println(colorize("Variable Assignment And Math -----------", stageHeaderFormat));

        String expectedTokens = "[Token{type=IDENTIFIER, primaryStorage=\"foo\"}, Token{type=OPERATOR, primaryStorage=\"SET_VARIABLE\"}, Token{type=LITERAL, primaryStorage=\"5\"}, Token{type=OPERATOR, primaryStorage=\"ADD\"}, Token{type=LITERAL, primaryStorage=\"7\"}, Token{type=OPERATOR, primaryStorage=\"GET_ATTRIBUTE\"}, Token{type=IDENTIFIER, primaryStorage=\"test2\"}, Token{type=OPERATOR, primaryStorage=\"MULTIPLY\"}, Token{type=LITERAL, primaryStorage=\"8\"}, Token{type=OPERATOR, primaryStorage=\"SUBTRACT\"}, Token{type=LITERAL, primaryStorage=\"5\"}, Token{type=OPERATOR, primaryStorage=\"DIVIDE\"}, Token{type=BRACKET, primaryStorage=\"ROUND_OPEN\"}, Token{type=LITERAL, primaryStorage=\"87.6\"}, Token{type=OPERATOR, primaryStorage=\"ADD\"}, Token{type=LITERAL, primaryStorage=\"4\"}, Token{type=BRACKET, primaryStorage=\"ROUND_CLOSE\"}, Token{type=OPERATOR, primaryStorage=\"GET_ATTRIBUTE\"}, Token{type=IDENTIFIER, primaryStorage=\"test\"}, Token{type=LINE_END, primaryStorage=\"null\"}]";
        String expectedAbstractSyntaxTree = //<editor-fold desc="Identifier Characters" defaultstate="collapsed">
                "AbstractSyntaxTree{\n" +
                "\tCodeBlock{\n" +
                "\t\t\tOperation{SET_VARIABLE\n" +
                "\t\t\ta={\n" +
                "\t\t\t\tIdentifier{\"foo\"}\n" +
                "\t\t\t},\n" +
                "\t\t\tb={\n" +
                "\t\t\t\tOperation{SUBTRACT\n" +
                "\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\tOperation{ADD\n" +
                "\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\tLiteral{\"5\"}\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\tOperation{MULTIPLY\n" +
                "\t\t\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\t\t\tOperation{GET_ATTRIBUTE\n" +
                "\t\t\t\t\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tLiteral{\"7\"}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tIdentifier{\"test2\"}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\t\t\tLiteral{\"8\"}\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\tOperation{DIVIDE\n" +
                "\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\tLiteral{\"5\"}\n" +
                "\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\tOperation{GET_ATTRIBUTE\n" +
                "\t\t\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\t\t\tOperation{ADD\n" +
                "\t\t\t\t\t\t\t\t\t\t\ta={\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tLiteral{\"87.6\"}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\tLiteral{\"4\"}\n" +
                "\t\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t\t},\n" +
                "\t\t\t\t\t\t\t\t\tb={\n" +
                "\t\t\t\t\t\t\t\t\t\tIdentifier{\"test\"}\n" +
                "\t\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
                //</editor-fold>

        test("tests/variableAssignmentAndMath.jam", expectedTokens, expectedAbstractSyntaxTree);
    }
}
