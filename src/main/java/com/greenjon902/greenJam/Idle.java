package com.greenjon902.greenJam;

import java.io.InputStream;
import java.util.Scanner;

public class Idle {
    private final Lexer lexer;
    private final Config config;

    public Idle() {
        lexer = new Lexer();
        config = new Config();
    }

    public void start(InputStream in) {
        while (true) {
            Scanner input = new Scanner(in);
            System.out.print(">>>  ");
            String jam = input.nextLine();

            lexer.analyzeString(jam, config);
        }
    }
}
