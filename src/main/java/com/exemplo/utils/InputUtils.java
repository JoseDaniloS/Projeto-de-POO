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
    
    public static String solicitar(String label, String atual) {
        System.out.println(label + " atual: " + atual);
        String entrada = InputUtils.readString("Novo " + label + " (ENTER mantém): ");
        return entrada.trim().isEmpty() ? atual : entrada;
    }

    public static boolean solicitarBoolean(String label, boolean atual) {
        System.out.println(label + " (atual: " + (atual ? "Sim" : "Não") + ")");
        String entrada = InputUtils.readString("s/n (ENTER mantém): ");

        if (entrada.trim().isEmpty())
            return atual;
        return entrada.equalsIgnoreCase("s");
    }
    
    public static int solicitarInt(String label, int atual) {
        System.out.println(label + " atual: " + atual);
        String entrada = InputUtils.readString("Novo " + label + " (ENTER mantém): ");

        if (entrada.trim().isEmpty())
            return atual;

        try {
            return Integer.parseInt(entrada);
        } catch (Exception e) {
            System.out.println("Valor inválido! Mantendo valor atual.");
            return atual;
        }
    }
}
