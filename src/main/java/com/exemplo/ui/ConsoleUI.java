package com.exemplo.ui;

import java.util.Scanner;

public class ConsoleUI {
    public static void clear() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux / MacOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }

        } catch (Exception e) {
            System.out.println("Não foi possível limpar o console.");
        }
    }

    public static void header(String titulo) {
        clear();
        System.out.println("===== " + titulo.toUpperCase() + " =====");
    }

    public static void pause() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nPressione ENTER para continuar...");
        try {
            System.in.read();
            scan.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao pausar o console.");
        }
    }
}
