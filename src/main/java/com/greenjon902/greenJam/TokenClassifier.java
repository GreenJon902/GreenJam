package com.greenjon902.greenJam;

import com.greenjon902.greenJam.config.Config;
import com.greenjon902.greenJam.types.tokens.IntegerToken;
import com.greenjon902.greenJam.types.tokens.Token;
import com.greenjon902.greenJam.types.TokenList;
import com.greenjon902.greenJam.types.UnclassifiedToken;
import com.greenjon902.greenJam.types.UnclassifiedTokenList;

import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class TokenClassifier {
    public TokenList classify(UnclassifiedTokenList unclassifiedTokenList, Config config) {
        TokenList tokenList = new TokenList();

        for (UnclassifiedToken unclassifiedToken : unclassifiedTokenList.toArray()) {
            tokenList.append(prepareUnclassifiedToken(unclassifiedToken, config));
        }

        return tokenList;

    }


    private static final String commandLoadType = "ldt";
    private static final String commandLoadValue = "ldv";
    private static final String commandSkipIfStringEquals = "sse";
    private static final String commandContinueIfStringEquals = "cse";
    private static final String commandSetTokenType = "stt";
    private static final String commandStoreTokenArg = "sta";

    private Token prepareUnclassifiedToken(UnclassifiedToken unclassifiedToken, Config config) {
        for (String[] script : config.tokenPreparers.prepareScripts) {
            String correct = null;
            HashMap<Integer, String> tokenArgs = new HashMap<>();

            String accumulator_value = "";

            for (String instruction : script) {
                boolean skip = false;

                String[] command_and_arg = instruction.split(" ", 2);
                String command = command_and_arg[0];

                String arg;
                if (command_and_arg.length == 2) {
                    arg = command_and_arg[1];
                } else {
                    arg = "";
                }


                switch (command) {
                    case commandLoadType:
                        accumulator_value = unclassifiedToken.type;
                        break;
                    case commandLoadValue:
                        accumulator_value = unclassifiedToken.value;
                        break;
                    case commandSkipIfStringEquals:
                        if (Objects.equals(accumulator_value, parseString(arg))) {
                            skip = true;
                        }
                        break;
                    case commandContinueIfStringEquals:
                        if (!Objects.equals(accumulator_value, parseString(arg))) {
                            skip = true;
                        }
                        break;
                    case commandSetTokenType:
                        correct = parseString(arg);
                        break;
                    case commandStoreTokenArg:
                        tokenArgs.put(parseInt(arg), accumulator_value);
                        break;
                    default:
                        Logging.error("Unknown TokenPreparer command \"" + command + "\"");
                }

                if (skip) {
                    break;
                }
            }
             if (correct != null) {

                 Token token = null;
                 switch (correct) {
                     case "integer":
                         token = new IntegerToken(Integer.parseInt(tokenArgs.get(0)));
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
