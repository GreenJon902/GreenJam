package com.greenjon902.greenJam;


import com.greenjon902.greenJam.config.Config;
import com.greenjon902.greenJam.types.TokenList;
import com.greenjon902.greenJam.types.UnclassifiedTokenList;

import java.io.InputStream;
import java.util.Scanner;

public class Idle {
    private final Lexer lexer;
    private final TokenPreparer tokenPreparer;
    private final Config config;

    public Idle() {
        lexer = new Lexer();
        tokenPreparer = new TokenPreparer();
        config = new Config();
    }

    public void start(InputStream in) {
        while (true) {
            Scanner input = new Scanner(in);
            System.out.print(">>>  ");
            String jam = input.nextLine();

            UnclassifiedTokenList unclassifiedTokenList = lexer.analyzeString(jam, config);
            System.out.println(unclassifiedTokenList.toString());
            TokenList tokenList = tokenPreparer.prepareList(unclassifiedTokenList, config);
            System.out.println(tokenList);
        }
    }
}
