package com.exemplo.menus;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Emprestimo;
import com.exemplo.models.Livro;
import com.exemplo.models.Membro;
import com.exemplo.repositories.EmprestimoRepository;
import com.exemplo.repositories.LivrosRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.EmprestimoUtils;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.LivroUtils;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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
                    List<Emprestimo> emprestimosAtivos =
                            Emprestimo.listarEmprestimosAtivosPorMembro(membro);
                    if (emprestimosAtivos == null) {
                        System.out.println("Nenhum emprestimo ativo");
                        ConsoleUI.pause();
                        break;
                    }
                    for (Emprestimo e : emprestimosAtivos) {
                        e.verEmprestimo();
                        System.out.println("-----------------------");
                    }
                    ConsoleUI.pause();
                    break;

                case 4:
                    List<Emprestimo> emprestimosAtrasados =
                            Emprestimo.listarEmprestimosAtrasadosPorMembro(membro);
                    if (emprestimosAtrasados == null) {
                        System.out.println("Nenhum emprestimo atrasado");
                        ConsoleUI.pause();
                        break;
                    }
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
                    realizarDevolucao(membro);
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

    public static void realizarDevolucao(Membro membro) {
        ConsoleUI.header("REALIZAR DEVOLUÇÃO");
        String isbn = InputUtils.readString("Informe o ISBN do livro que vai devolver:");

        List<Emprestimo> emprestimosAtivos = Emprestimo.listarEmprestimosAtivosPorMembro(membro);

        Emprestimo emprestimoParaDevolver = null;

        for (Emprestimo e : emprestimosAtivos) {
            if (e.getLivro().getIsbn().equals(isbn)) {
                emprestimoParaDevolver = e;
                break;
            }
        }

        if (emprestimoParaDevolver == null) {
            System.out.println("Nenhum empréstimo ativo encontrado com esse ISBN.");
            ConsoleUI.pause();
            return;
        }

        emprestimoParaDevolver.registrarDevolucao();
        Map<String, AttributeValue> novoMapa = EmprestimoUtils.toMap(emprestimoParaDevolver);
        String id = EmprestimoRepository.buscarIdporIsbnECpf(isbn, membro.getCpf());
        if (id == null) {
            System.out.println("ERRO: empréstimo encontrado, mas ID não localizado no banco!");
            ConsoleUI.pause();
            return;
        }
        novoMapa.put("id", DynamoUtils.criarAttributeValueDynamoDB(id));
        DynamoUtils.enviarElementoBancoDeDados(novoMapa, EmprestimoRepository.TABLE_NAME);

        System.out.println("Devolução registrada com sucesso!");
        ConsoleUI.pause();
    }

}
