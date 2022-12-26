import com.diogonunes.jcolor.AnsiFormat;
import com.greenjon902.greenJam.parser.AbstractSyntaxTree;
import com.greenjon902.greenJam.parser.Parser;
import com.greenjon902.greenJam.tokenizer.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class CompileTests {
    AnsiFormat stageHeaderFormat = new AnsiFormat(BOLD(), GREEN_TEXT());
    AnsiFormat testInfoFormat = new AnsiFormat(GREEN_TEXT());
    AnsiFormat testWarningFormat = new AnsiFormat(RED_TEXT());

    private void test(String path, Token[] expectedTokens) throws IOException {
        System.out.println(colorize("Reading \"" + path + "\"...", testInfoFormat));
        Scanner scan = new Scanner(new File(path));
        scan.useDelimiter("\\Z");
        String content = scan.next();

        System.out.println(colorize("Tokenizing...", testInfoFormat));
        Tokenizer tokenizer = new Tokenizer(content);
        Token[] tokens = tokenizer.getTokens();
        System.out.println(Arrays.toString(tokens));
        if (expectedTokens != null) {
            Assertions.assertArrayEquals(expectedTokens, tokens);
        } else {
            System.out.println(colorize("No expected tokens given", testWarningFormat));
        }

        System.out.println(colorize("Parsing...", testInfoFormat));
        Parser parser = new Parser(new TokenStream(tokens));
        AbstractSyntaxTree ast = parser.getParsed();
        ast.prettyPrint();

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
        test("tests/simpleCommand.jam", expectedTokens);
    }

    @Test
    public void testCompileVariableAssignmentAndMath() throws IOException {
        System.out.println(colorize("Variable Assignment And Math -----------", stageHeaderFormat));
        test("tests/variableAssignmentAndMath.jam", null);
    }
}
