package com.greenjon902.greenJam;

import com.greenjon902.greenJam.types.JamFile;

import java.io.InputStream;
import java.util.Scanner;

public class Idle {
    public void start(InputStream in) {
        Scanner input = new Scanner(in);
        System.out.print(">>>  ");
        String jam = input.nextLine();

    }
}
