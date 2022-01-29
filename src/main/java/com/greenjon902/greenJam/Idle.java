package com.greenjon902.greenJam;


import com.greenjon902.greenJam.config.Config;
import com.greenjon902.greenJam.types.TokenList;
import com.greenjon902.greenJam.types.UnpreparedTokenList;

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

            UnpreparedTokenList unpreparedTokenList = lexer.analyzeString(jam, config);
            System.out.println(unpreparedTokenList.toString());
            TokenList tokenList = tokenPreparer.prepareList(unpreparedTokenList, config);
            System.out.println(tokenList);
        }
    }
}
