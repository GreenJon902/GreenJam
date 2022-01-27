package com.greenjon902.greenJam;

import com.greenjon902.greenJam.config.Config;
import com.greenjon902.greenJam.types.tokens.CharacterToken;
import com.greenjon902.greenJam.types.tokens.IntegerToken;
import com.greenjon902.greenJam.types.tokens.OperatorToken;
import com.greenjon902.greenJam.types.tokens.Token;
import com.greenjon902.greenJam.types.TokenList;
import com.greenjon902.greenJam.types.UnclassifiedToken;
import com.greenjon902.greenJam.types.UnclassifiedTokenList;

import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class TokenClassifier {
    public TokenList classifyList(UnclassifiedTokenList unclassifiedTokenList, Config config) {
        TokenList tokenList = new TokenList();

        for (UnclassifiedToken unclassifiedToken : unclassifiedTokenList.toArray()) {
            tokenList.append(classify(unclassifiedToken, config));
        }

        return tokenList;

    }


    private static final String ACC_TRUE = "TRUE";
    private static final String ACC_FALSE = "FALSE";

    public Token classify(UnclassifiedToken unclassifiedToken, Config config) {
        for (String[] script : config.tokenClassifierScripts.classifyScripts) {
            String correct = null;
            HashMap<Integer, String> tokenArgs = new HashMap<>();

            String accumulator_value = "";

            int current_instruction_index = 0;
            while (current_instruction_index < script.length){
                String instruction = script[current_instruction_index];


                boolean skip = false;

                String[] command_and_arg = instruction.split(" ", 2);
                Command command = Command.getFromCode(command_and_arg[0]);

                String arg;
                if (command_and_arg.length == 2) {
                    arg = command_and_arg[1];
                } else {
                    arg = "";
                }


                switch (command) {
                    case loadTokenType:
                        accumulator_value = unclassifiedToken.type;
                        break;
                    case loadTokenValue:
                        accumulator_value = unclassifiedToken.value;
                        break;
                    case equals:
                        if (accumulator_value.equals(parseString(arg))) {
                            accumulator_value = ACC_TRUE;
                        } else {
                            accumulator_value = ACC_FALSE;
                        }
                        break;
                    case flip:
                        if (accumulator_value.equals(ACC_TRUE)) {
                            accumulator_value = ACC_FALSE;
                        } else if (accumulator_value.equals(ACC_FALSE)) {
                            accumulator_value = ACC_TRUE;
                        } else {
                            Logging.error("Could not flip accumulator value as it is neither \"TRUE\" nor \"FALSE\", it is \"" + accumulator_value + "\"");
                        }
                        break;
                    case setTokenType:
                        correct = parseString(arg);
                        break;
                    case setTokenArgument:
                        tokenArgs.put(parseInt(arg), accumulator_value);
                        break;
                    case loadTokenArgument:
                        accumulator_value = tokenArgs.get(parseInt(arg));
                        break;
                    case error:
                        String out = parseString(arg);
                        out = out.replace("%acc", accumulator_value);
                        String out_new = out;

                        int location = 0;
                        while (location < out.length()) {
                            if (out.charAt(location) == '%') {
                                location += 4; // Skip the %arg

                                int arg_number = parseInt(String.valueOf(location));
                                String string_arg_number = String.valueOf(arg_number);
                                location += string_arg_number.length();

                                out_new = out_new.replace("%arg" + string_arg_number, tokenArgs.get(arg_number));

                            } else {
                                location++;
                            }
                        }

                        Logging.error(out_new);
                        break;
                    case skipIfAccumulatorContainsTrue:
                        if (accumulator_value.equals(ACC_TRUE)) {
                            skip = true;
                        }
                        break;
                    case skipIfAccumulatorContainsFalse:
                        if (accumulator_value.equals(ACC_FALSE)) {
                            skip = true;
                        }
                        break;
                    case skip:
                        skip = true;
                        break;
                    case jumpIfAccumulatorContainsTrue:
                        if (accumulator_value.equals(ACC_TRUE)) {
                            current_instruction_index = parseInt(arg);
                        }
                        break;
                    case jumpIfAccumulatorContainsFalse:
                        if (accumulator_value.equals(ACC_FALSE)) {
                            current_instruction_index = parseInt(arg);
                        }
                        break;
                    case jump:
                        current_instruction_index = parseInt(arg);
                        break;
                    case moveIfAccumulatorContainsTrue:
                        if (accumulator_value.equals(ACC_TRUE)) {
                            current_instruction_index += parseInt(arg) - 1; // Minus one because it gets raised by one at the end and this would mean the value is not correct
                        }
                        break;
                    case moveIfAccumulatorContainsFalse:
                        if (accumulator_value.equals(ACC_FALSE)) {
                            current_instruction_index += parseInt(arg) - 1; // Minus one because it gets raised by one at the end and this would mean the value is not correct
                        }
                        break;
                    case move:
                        current_instruction_index += parseInt(arg) - 1; // Minus one because it gets raised by one at the end and this would mean the value is not correct
                        break;
                    case setAccumulatorAs:
                        accumulator_value = parseString(arg);
                        break;
                    default:
                        Logging.error("Unknown command \"" + command + "\"");
                }

                if (skip) {
                    break;
                }
                current_instruction_index++;
            }
             if (correct != null) {

                 Token token = null;
                 switch (correct) {
                     case "integer":
                         token = new IntegerToken(Integer.parseInt(tokenArgs.get(0)));
                         break;
                     case "character":
                         token = new CharacterToken(tokenArgs.get(0).charAt(0));
                         break;
                     case "operator":
                         token = new OperatorToken(tokenArgs.get(0), tokenArgs.get(1));
                         break;
                     default:
                         Logging.error("Unknown token type \"" + correct + "\"");
                 }

                 return token; // TODO: Token Creation
             }
        }

        Logging.error("Failed to classify token - " + unclassifiedToken);
        return null;
    }

    private String parseString(String string) {
        if (!string.startsWith("\"")) {
            Logging.error("Tried to parse a string that doesnt start with \", string is \"" + string + "\"");
        }

        return new Scanner(string).useDelimiter("\"").next();
    }

    private int parseInt(String string) {
        return Integer.parseInt(string);
    }
}

enum Command {
    loadTokenType("ldt"),
    loadTokenValue("ldv"),
    equals("equ"),
    flip("flp"),
    setTokenType("stt"),
    setTokenArgument("sta"),
    loadTokenArgument("lta"),
    error("err"),
    skipIfAccumulatorContainsTrue("sit"),
    skipIfAccumulatorContainsFalse("sif"),
    skip("skp"),
    jumpIfAccumulatorContainsTrue("jit"),
    jumpIfAccumulatorContainsFalse("jif"),
    jump("jmp"),
    moveIfAccumulatorContainsTrue("mit"),
    moveIfAccumulatorContainsFalse("mif"),
    move("mov"),
    setAccumulatorAs("sav");

    private final String code;

    Command(String code) {
        this.code = code;
    }

    public static Command getFromCode(String code) {
        for (Command command : values()){
            if (command.code.equals(code)){
                return command;
            }
        }

        throw new IllegalArgumentException(code + " is not a valid code");
    }
}