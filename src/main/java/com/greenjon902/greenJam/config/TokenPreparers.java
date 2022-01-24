package com.greenjon902.greenJam.config;

/**
 * A group of instructions used to prepare UnclassifiedTokens to be converted into their respective token classes!<br/>
 * <br/>
 * <u> Token Argument Requirements </u><br/>
 * IntegerToken(int integer)<br/>
 * CharacterToken(char character)<br/>
 * StringToken(String string)<br/>
 * OperatorToken(OperatorType operatorType, String operator)<br/>
 *
 * <table>
 *   <tr>
 *       <th>Command</th>
 *       <th>Use</th>
 *   </tr>
 *   <tr>
 *       <td>ldt</td>
 *       <td>Load the token type to the accumulator</td>
 *   </tr>
 *   <tr>
 *       <td>ldv</td>
 *       <td>Load the token value to the accumulator</td>
 *   </tr>
 *   <tr>
 *       <td>sse (string)</td>
 *       <td>If the value in the accumulator is equal to the value in (string) then skip to the next preparer, else carry on</td>
 *   </tr>
 *   <tr>
 *       <td>cse (string)</td>
 *       <td>If the value in the accumulator is equal to the value in (string) then continue, else skip to the next preparer</td>
 *   </tr>
 *   <tr>
 *       <td>sac</td>
 *       <td>Set the current preparer as correct and will exit when finishes the preparer</td>
 *   </tr>
 *   <tr>
 *       <td>sta (arg_number)</td>
 *       <td>Set the argument at (arg_number)'s value to the current value in the accumulator. If the arg number is higher then the amount of args that the function takes then it is ignored</td>
 *   </tr>
 * </table>
 */
public class TokenPreparers {
    public final String integerPreparer =
            "ldt;" +
            "cse \"integer\";" +
            "sac;" +
            "ldv;" +
            "set 0";

}
