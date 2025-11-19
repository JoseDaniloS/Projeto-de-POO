package com.exemplo.menus;

import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.utils.SystemUtils;

public class MenuBibliotecario {
    public static void exibir(Bibliotecario bibliotecario) {
        Scanner scan = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n===== MENU BIBLIOTECÁRIO =====");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Cadastrar Membro");
            System.out.println("3 - Gerenciar Membros");
            System.out.println("4 - Cadastrar Livro");
            System.out.println("5 - Gerenciar Livros");
            System.out.println("6 - Empréstimos/Devoluções");
            System.out.println("7 - Logout");
            System.out.print("Escolha: ");
            option = scan.nextInt();
            scan.nextLine();
            SystemUtils.clearConsole();
            switch (option) {
                case 1:
                    bibliotecario.verUsuario();
                    break;
                case 2:
                    // cadastrarMembro();
                    break;
                case 3:
                    // gerenciarMembros();
                    break;
                case 4:
                    // cadastrarLivro();
                    break;
                case 5:
                    // gerenciarLivros();
                    break;
                case 6:
                    // processosEmprestimo();
                    break;
                case 7:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (option != 7);
    }
}
