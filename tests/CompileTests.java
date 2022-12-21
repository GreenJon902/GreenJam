import com.greenjon902.greenJam.parser.AbstractSyntaxTree;
import com.greenjon902.greenJam.parser.Parser;
import com.greenjon902.greenJam.tokenizer.Token;
import com.greenjon902.greenJam.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class CompileTests {
    private void compileFileWhilePrintingSteps(String path) throws IOException {
        System.out.println("Reading and tokenizing \"" + path + "\"");

        Scanner scan = new Scanner(new File(path));
        scan.useDelimiter("\\Z");
        String content = scan.next();

        Tokenizer tokenizer = new Tokenizer(content);
        Token[] tokens = tokenizer.getTokens();
        System.out.println(Arrays.toString(tokens));

        Parser parser = new Parser(tokens);
        AbstractSyntaxTree ast = parser.getParsed();
        ast.prettyPrint();
    }

    @Test
    public void testCompileCommands() throws IOException {
        compileFileWhilePrintingSteps("tests/commands.jam");
    }

    @Test
    public void testCompileVariableAssignmentAndMath() throws IOException {
        compileFileWhilePrintingSteps("tests/variableAssignmentAndMath.jam");
    }
}
