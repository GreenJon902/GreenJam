package com.greenjon902.greenJam;

import com.greenjon902.greenJam.types.JamFile;

import java.io.InputStream;
import java.util.Scanner;

public class Idle {
    private final Lexer lexer;

    public Idle() {
        lexer = new Lexer();
    }

    public void start(InputStream in) {
        while (true) {
            Scanner input = new Scanner(in);
            System.out.print(">>>  ");
            String jam = input.nextLine();

            lexer.analyzeString(jam);
        }
    }
}
