package com.exemplo.menus;

import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.SystemUtils;
import com.exemplo.utils.UsuarioUtils;

public class MenuSupervisor {
    public static void exibir(Bibliotecario bibliotecario) {
        int option;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.println("\n===== MENU SUPERVISOR =====");
            System.out.println("1 - Cadastrar Bibliotecário");
            System.out.println("2 - Gerenciar Bibliotecários");
            System.out.println("3 - Menu Bibliotecario");
            System.out.println("4 - Gerenciar Livros");
            System.out.println("5 - Processar Empréstimos");
            System.out.println("6 - Gerenciar Membros");
            System.out.println("7 - Logout");
            System.out.print("Escolha: ");
            option = scan.nextInt();
            SystemUtils.clearConsole();
            switch (option) {
                case 1:
                    cadastrarBibliotecario();
                    break;
                case 2:

                    break;
                case 3:
                    MenuBibliotecario.exibir(bibliotecario);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
                    System.out.println("Saindo....");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } while (option != 7);
    }

    private static void cadastrarBibliotecario() {
        try {
            System.out.println("\n===== CADASTRAR BIBLIOTECARIO =====");
            String nome = InputUtils.readString("Informe o nome:");
            String cpf = InputUtils.readString("Informe o CPF:");
            String login = InputUtils.readString("Informe o Login (ID):");
            String senha = InputUtils.readString("Informe a Senha:");

            System.out.println("Selecione o Cargo:");
            System.out.println("1 - Estagiário");
            System.out.println("2 - Atendente");
            int opcaoCargo = InputUtils.readInt("Escolha:");

            String cargo;
            switch (opcaoCargo) {
                case 1:
                    cargo = "estagiario";
                    break;
                case 2:
                    cargo = "atendente";
                    break;
                default:
                    System.out.println("Cargo inválido! Cadastro cancelado.");
                    return;
            }

            Bibliotecario novoBibliotecario = Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);

            UsuarioRepository.enviarElementoBancoDeDados(UsuarioUtils.toMap(novoBibliotecario), "UsuariosPOO");
            SystemUtils.clearConsole();
            System.out.println("Bibliotecario " + nome + " cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar um novo Bibliotecario!");
            System.out.println(e.getMessage());
        }

    }
}
