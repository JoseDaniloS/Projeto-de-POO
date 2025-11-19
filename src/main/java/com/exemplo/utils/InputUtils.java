package com.exemplo.utils;

import java.util.Scanner;

public class InputUtils {

    private static final Scanner scan = new Scanner(System.in);

    public static String readString(String mensagem) {
        System.out.println(mensagem);
        return scan.nextLine();
    }

    public static int readInt(String mensagem) {
        System.out.println(mensagem);
        return Integer.parseInt(scan.nextLine());
    }
}
