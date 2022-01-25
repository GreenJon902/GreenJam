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
 *       <td>equ (string)</td>
 *       <td>If the value in the accumulator is equal to the value in (string) then set the accumulator to "TRUE", if it is not then set the accumulator to "FALSE"</td>
 *   </tr>
 *   <tr>
 *       <td>flp</td>
 *       <td>If the value in the accumulator contains "TRUE" then set it to "FALSE" and vice-versa, If it does not equal either of them then an error is thrown</td>
 *   </tr>
 *   <tr>
 *       <td>stt (string)</td>
 *       <td>Set the token type as (string) and exit when finishes the preparer</td>
 *   </tr>
 *   <tr>
 *       <td>sta (arg_number)</td>
 *       <td>Set the argument at (arg_number)'s value to the current value in the accumulator. If the arg number is higher then the amount of args that the function takes then it is ignored. This can also be used as memory</td>
 *   </tr>
 *   <tr>
 *       <td>lta (arg_number)</td>
 *       <td>Load the argument at (arg_number)'s value to the accumulator, crashes if not set</td>
 *   </tr>
 *   <tr>
 *       <td>err (error_message)</td>
 *       <td>Throw an error with the message inside of (error_message). This can include %acc to format in the accumulator value or %arg(arg_number) to format a token argument value from the location (arg_number) in</td>
 *  </tr>
 *  <tr>
 *      <td>sit</td>
 *      <td>Skip to the next preparer script if accumulator contains "TRUE"</td>
 *  </tr>
 *  <tr>
 *      <td>sif</td>
 *      <td>Skip to the next preparer script if accumulator contains "FALSE"</td>
 *  </tr>
 *  <tr>
 *      <td>skp</td>
 *      <td>Skip to the next preparer script</td>
 *  </tr>
 *  <tr>
 *      <td>jit (location)</td>
 *      <td>Jump to the instruction at (location) if accumulator contains "TRUE"</td>
 *  </tr>
 *  <tr>
 *      <td>jif (location)</td>
 *      <td>Jump to the instruction at (location) if accumulator contains "FALSE"</td>
 *  </tr>
 *  <tr>
 *      <td>jmp (location)</td>
 *      <td>Jump to the instruction at (location)</td>
 *  </tr>
 *  <tr>
 *      <td>mit (location)</td>
 *      <td>Move to the instruction that is the current location + (location) if accumulator contains "TRUE"</td>
 *  </tr>
 *  <tr>
 *      <td>mif (location)</td>
 *      <td>Move to the instruction that is the current location + (location) if accumulator contains "FALSE"</td>
 *  </tr>
 *  <tr>
 *      <td>mov (location)</td>
 *      <td>Move to the instruction that is the current location + (location)</td>
 *  </tr>
 * </table>
 */
public class TokenClassifierScripts {
    public final String[][] classifyScripts = {{
            "ldt",
            "equ \"integer\"",
            "sif",
            "stt \"integer\"",
            "ldv",
            "sta 0"
    }, {
            "ldt",
            "equ \"character\"",
            "sif",
            "stt \"character\"", // TODO: Add error if character length is over 1
            "ldv",
            "sta 0"
    }};
}
