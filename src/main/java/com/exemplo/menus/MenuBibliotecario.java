package com.exemplo.menus;

import java.util.List;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Membro;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.UsuarioUtils;

public class MenuBibliotecario {
    public static void exibir(Bibliotecario bibliotecario) {
        int option;

        do {
            ConsoleUI.header("MENU BIBLIOTECÁRIO");
            System.out.println("1 - Ver meus dados");
            System.out.println("2 - Cadastrar Membro");
            System.out.println("3 - Gerenciar Membros");
            System.out.println("4 - Cadastrar Livro");
            System.out.println("5 - Gerenciar Livros");
            System.out.println("6 - Empréstimos/Devoluções");
            System.out.println("7 - Logout");
            option = InputUtils.readInt("Escolha: ");
            switch (option) {
                case 1:
                    bibliotecario.verBibliotecario();
                    ConsoleUI.pause();
                    break;
                case 2:
                    cadastrarMembro();
                    break;
                case 3:
                    gerenciarMembros();
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

    private static void gerenciarMembros() {
        try {
            int option = 0;
            do {
                ConsoleUI.header("GERENCIAMENTO DE MEMBROS");
                System.out.println("1 - Listar Membros");
                System.out.println("2 - Editar Membro");
                System.out.println("3 - Desativar Membro");
                System.out.println("4 - Voltar");
                option = InputUtils.readInt("Escolha:");
                ConsoleUI.clear();
                switch (option) {
                    case 1:
                        List<Membro> membros = Membro.listaMembros();
                        for (Membro m : membros) {
                            m.verMembro();
                            System.out.println("-----------------------");
                        }
                        ConsoleUI.pause();
                        break;
                    case 2:
                        // editarBibliotecario();
                        break;
                    case 3:
                        // desativarBibliotecario();
                        break;
                    case 4:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        ConsoleUI.pause();
                }
            } while (option != 4);
            return;
        } catch (Exception e) {
            // TODO: handle exception
            ConsoleUI.pause();
        }
    }

    private static void cadastrarMembro() {
        try {
            ConsoleUI.header("CADASTRAR MEMBRO");
            String nome = InputUtils.readString("Informe o nome:");
            String cpf = InputUtils.readString("Informe o CPF:");
            String login = InputUtils.readString("Informe o Login (ID):");
            String senha = InputUtils.readString("Informe a Senha:");
            String endereco = InputUtils.readString("Informe o Endereço:");
            String telefone = InputUtils.readString("Informe o telefone");
            String email = InputUtils.readString("Informe o email:");

            Membro novoMembro = Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);

            UsuarioRepository.enviarElementoBancoDeDados(UsuarioUtils.toMap(novoMembro), "UsuariosPOO");
            ConsoleUI.clear();
            System.out.println("Membro " + nome + " cadastrado com sucesso!");
            ConsoleUI.pause();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar um novo Membro!");
            // System.out.println(e.getMessage());
            ConsoleUI.pause();
        }
    }
}
