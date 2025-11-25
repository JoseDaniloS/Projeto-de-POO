package com.exemplo.menus;

import java.util.Scanner;

import com.exemplo.models.Membro;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.InputUtils;

public class MenuMembro {
    public static void membroMenu(Membro membro) {

        int option;

        do {
            ConsoleUI.header("MENU DO MEMBRO");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Ver livros disponíveis");
            System.out.println("3 - Ver meus empréstimos");
            System.out.println("4 - Logout");

            option = InputUtils.readInt("Escolha: ");

            switch (option) {
                case 1:
                    membro.verMembro();
                    ConsoleUI.pause();
                    break;
                case 2:
                    // listarLivros();
                    break;
                case 3:
                    // listarEmprestimos(membro);
                    break;
                case 4:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (option != 4);
    }

}
