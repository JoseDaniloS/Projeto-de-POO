package com.exemplo.menus;

import java.util.List;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
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

            DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(novoBibliotecario), "UsuariosPOO");
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

    private static void editarBibliotecario() {
        String id = InputUtils.readString("Informe o ID do bibliotecario a ser editado:");
        Usuario usuarioBancoDados = UsuarioUtils.criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));
        if (usuarioBancoDados instanceof Bibliotecario == false) {
            System.out.println("Usuario informado não é um bibliotecario!");
            ConsoleUI.pause();
            return;
        }
        Bibliotecario bibliotecario = (Bibliotecario) usuarioBancoDados;
        try {
            int option;
            do {
                ConsoleUI.header("EDITAR MEMBRO");
                System.out.println("1 - NOME");
                System.out.println("2 - CPF");
                System.out.println("3 - LOGIN");
                System.out.println("4 - CARGO");
                System.out.println("5 - SENHA");
                System.out.println("6 - VOLTAR");
                option = InputUtils.readInt("Escolha:");
                switch (option) {
                    case 1:
                        String nome = InputUtils.readString("Informe o nome:");
                        bibliotecario.setNome(nome);
                        break;
                    case 2:
                        String cpf = InputUtils.readString("Informe o cpf:");
                        bibliotecario.setCpf(cpf);
                        break;
                    case 3:
                        String login = InputUtils.readString("Informe o login:");
                        bibliotecario.setLogin(login);
                        break;
                    case 4:
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
                        bibliotecario.setCargo(cargo);
                        break;
                    case 5:
                        String senha = InputUtils.readString("Informe a senha:");
                        bibliotecario.setSenha(senha);
                        break;

                    case 6:
                        System.out.println("Voltando...");
                        ConsoleUI.pause();
                        return;

                    default:
                        System.out.println("Opção inválida.");
                        ConsoleUI.pause();
                        return;
                }
                DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(bibliotecario), "UsuariosPOO");
                System.out.println("Bibliotecario editado com sucesso!");
                ConsoleUI.pause();
            } while (option != 6);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Erro ao editar bibliotecario: " + e.getMessage());
            ConsoleUI.pause();

        }

    }

    private static void desativarBibliotecario() {
        String id = InputUtils.readString("Informe o ID do Bibliotecario a ser desativado: ");
        // bucar no banco de dados
        Usuario usuario = UsuarioUtils.criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));
        if (usuario instanceof Bibliotecario == false) {
            System.out.println("Usuario informado não é um Bibliotecario!");
            ConsoleUI.pause();
            return;
        }
        if (usuario == null) {
            System.out.println("Bibliotecario não encontrado!");
            ConsoleUI.pause();
            return;
        }
        Usuario.desativarUsuario(usuario);
        usuario.verUsuario();
        DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(usuario), "UsuariosPOO");
        System.out.println("Bibliotecario desativado com sucesso!");
        ConsoleUI.pause();

    }
}
