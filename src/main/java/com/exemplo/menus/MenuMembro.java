package com.exemplo.menus;

import java.util.List;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Emprestimo;
import com.exemplo.models.Membro;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.EmprestimoUtils;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.LivroUtils;

public class MenuMembro {
    public static void membroMenu(Membro membro) {

        int option;

        do {
            ConsoleUI.header("MENU DO MEMBRO");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Ver livros disponíveis");
            System.out.println("3 - Ver meus empréstimos ativos");
            System.out.println("4 - Ver meus empréstimos atrasados");
            System.out.println("5 - Pegar livro emprestado");
            System.out.println("6 - Devolver livro");
            System.out.println("7 - Logout");

            option = InputUtils.readInt("Escolha: ");
            ConsoleUI.clear();
            switch (option) {
                case 1:
                    membro.verMembro();
                    ConsoleUI.pause();
                    break;

                case 2:
                    LivroUtils.exibirListaLivros();
                    break;

                case 3:
                    List<Emprestimo> emprestimosAtivos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);
                    for (Emprestimo e : emprestimosAtivos) {
                        e.verEmprestimo();
                        System.out.println("-----------------------");
                    }
                    ConsoleUI.pause();
                    break;

                case 4:
                    List<Emprestimo> emprestimosAtrasados = Emprestimo.listarEmprestimosAtrasadosPorMembro(membro);
                    for (Emprestimo e : emprestimosAtrasados) {
                        e.verEmprestimo();
                        System.out.println("-----------------------");
                    }
                    ConsoleUI.pause();
                    break;

                case 5:
                    EmprestimoUtils.realizarEmprestimo(membro);
                    break;

                case 6:
                    // EmprestimoUtils.realizarDevolucao(membro);
                    // ConsoleUI.pause();
                    break;

                case 7:
                    System.out.println("Saindo...");
                    return;

                default:
                    System.out.println("Opção inválida.");
                    ConsoleUI.pause();
                    break;
            }

        } while (option != 7);
    }

}
