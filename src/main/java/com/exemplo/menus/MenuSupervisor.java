package com.exemplo.menus;

import java.util.List;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.UsuarioUtils;

public class MenuSupervisor {
    public static void exibir(Bibliotecario bibliotecario) {
        int option;
        do {
            ConsoleUI.header("MENU SUPERVISOR");
            System.out.println("1 - Cadastrar Bibliotecário");
            System.out.println("2 - Gerenciar Bibliotecários");
            System.out.println("3 - Menu Bibliotecario");
            System.out.println("4 - Processar Empréstimos");
            System.out.println("5 - Logout");
            option = InputUtils.readInt("Escolha:");
            ConsoleUI.clear();
            switch (option) {
                case 1:
                    cadastrarBibliotecario();
                    break;
                case 2:
                    gerenciarBibliotecarios();
                    break;
                case 3:
                    MenuBibliotecario.exibir(bibliotecario);
                    break;
                case 4:
                    // processarEmprestimos();
                    break;
                case 5:
                    System.out.println("Saindo....");
                    break;

                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } while (option != 5);

    }

    private static void cadastrarBibliotecario() {
        try {
            ConsoleUI.header("CADASTRAR BIBLIOTECARIO");
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
            ConsoleUI.clear();
            System.out.println("Bibliotecario " + nome + " cadastrado com sucesso!");
            ConsoleUI.pause();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar um novo Bibliotecario!");
            // System.out.println(e.getMessage());
            ConsoleUI.pause();
        }

    }

    private static void gerenciarBibliotecarios() {
        try {
            int option = 0;
            do {
                ConsoleUI.header("GERENCIAMENTO DE BIBLIOTECARIOS");
                System.out.println("1 - Listar Bibliotecarios");
                System.out.println("2 - Editar Bibliotecario");
                System.out.println("3 - Desativar Bibliotecario");
                System.out.println("4 - Voltar");
                option = InputUtils.readInt("Escolha:");
                ConsoleUI.clear();
                switch (option) {
                    case 1:
                        List<Bibliotecario> bibliotecarios = Bibliotecario.listaBibliotecarios();
                        for (Bibliotecario b : bibliotecarios) {
                            b.verBibliotecario();
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
}
