package com.exemplo.menus;

import java.util.List;
import java.util.Scanner;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Emprestimo;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.BibliotecarioUtils;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.EmprestimoUtils;
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
            System.out.println("4 - Gerenciar Empréstimos");
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
                    gerenciarEmprestimos();
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

            Bibliotecario novoBibliotecario =
                    Bibliotecario.criarBibliotecario(nome, cpf, login, senha, cargo);

            DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(novoBibliotecario),
                    "UsuariosPOO");
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
                        String id = InputUtils.readString("Informe o ID do bibliotecário: ");

                        Usuario usuarioBanco = UsuarioUtils
                                .criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));

                        if (!(usuarioBanco instanceof Bibliotecario)) {
                            System.out.println("O ID informado não é de um bibliotecário!");
                            ConsoleUI.pause();
                            return;
                        }

                        Bibliotecario b = (Bibliotecario) usuarioBanco;

                        BibliotecarioUtils.editar(b);
                        break;
                    case 3:
                        desativarBibliotecario();
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
            System.out.println(e.getMessage());
            ConsoleUI.pause();
        }
    }

    private static void gerenciarEmprestimos() {
        int opcao;

        do {
            ConsoleUI.header("GERENCIAR EMPRÉSTIMOS");

            System.out.println("1 - Listar todos os empréstimos");
            System.out.println("2 - Listar empréstimos ativos");
            System.out.println("3 - Listar empréstimos atrasados");
            System.out.println("4 - Excluir empréstimo");
            System.out.println("5 - Gerenciar emprestimo de um membro");
            System.out.println("6 - Voltar");

            opcao = InputUtils.readInt("Escolha: ");
            ConsoleUI.clear();

            switch (opcao) {

                case 1:
                    List<Emprestimo> listaEmprestimos = Emprestimo.listarEmprestimos();
                    EmprestimoUtils.mostrarEmprestimos(listaEmprestimos);
                    break;

                case 2:
                    List<Emprestimo> listaEmprestimosAtivos = Emprestimo.listarEmprestimosAtivos();
                    EmprestimoUtils.mostrarEmprestimos(listaEmprestimosAtivos);
                    break;

                case 3:
                    List<Emprestimo> listaEmprestimosAtrasados =
                            Emprestimo.listarEmprestimosAtrasados();
                    EmprestimoUtils.mostrarEmprestimos(listaEmprestimosAtrasados);
                    break;

                case 4:
                    // excluirEmprestimoPorId();
                    break;
                case 5:
                    gerenciarEmprestimosDoMembro();
                    return;
                case 6:
                    return;

                default:
                    System.out.println("Opção inválida!");
                    ConsoleUI.pause();
                    break;
            }

        } while (opcao != 6);
    }

    private static void gerenciarEmprestimosDoMembro() {
        String cpf = InputUtils.readString("Informe o CPF do membro");

        Usuario usuario = UsuarioRepository.buscarPorCpf(cpf);
        if (usuario == null) {
            System.out.println("CPF não encontrado");
            ConsoleUI.pause();
            return;
        }
        if (!(usuario instanceof Membro)) {
            System.out.println("CPF não é de um membro!");
            ConsoleUI.pause();
            return;
        }

        Membro membro = (Membro) usuario;

        int opcao;
        do {
            ConsoleUI.header("EMPRÉSTIMOS DE: " + membro.getNome().toUpperCase());

            System.out.println("1 - Ver empréstimos ativos");
            System.out.println("2 - Ver empréstimos atrasados");
            System.out.println("3 - Editar emprestimo");
            System.out.println("4 - Registrar devolução");
            System.out.println("5 - Novo empréstimo");
            System.out.println("6 - Voltar");

            opcao = InputUtils.readInt("Escolha: ");
            ConsoleUI.clear();

            switch (opcao) {

                case 1:
                    EmprestimoUtils.mostrarEmprestimos(
                            Emprestimo.listarEmprestimosAtivosPorMembro(membro));
                    break;

                case 2:
                    EmprestimoUtils.mostrarEmprestimos(
                            Emprestimo.listarEmprestimosAtrasadosPorMembro(membro));
                    break;
                case 3:
                    ConsoleUI.header("EDITAR EMPRÉSTIMO");

                    // Buscar empréstimos ativos
                    List<Emprestimo> listaEmprestimosAtivos =
                            Emprestimo.listarEmprestimosAtivosPorMembro(membro);

                    // Se lista estiver vazia, parar
                    if (listaEmprestimosAtivos.isEmpty()) {
                        System.out.println("Nenhum empréstimo ativo encontrado.");
                        ConsoleUI.pause();
                        break;
                    }

                    EmprestimoUtils.mostrarEmprestimos(listaEmprestimosAtivos);

                    // Pegar índice válido (base 1)
                    int indexEmprestimoAtualizacao;

                    do {
                        indexEmprestimoAtualizacao = InputUtils
                                .readInt("Informe o número do empréstimo que deseja editar: ");

                        if (indexEmprestimoAtualizacao < 1
                                || indexEmprestimoAtualizacao > listaEmprestimosAtivos.size()) {

                            System.out.println("Número inválido! Escolha entre 1 e "
                                    + listaEmprestimosAtivos.size());
                        }

                    } while (indexEmprestimoAtualizacao < 1
                            || indexEmprestimoAtualizacao > listaEmprestimosAtivos.size());

                    // Selecionar empréstimo correto (convertendo para índice base 0)
                    Emprestimo emprestimoEditar =
                            listaEmprestimosAtivos.get(indexEmprestimoAtualizacao - 1);

                    EmprestimoUtils.submenuEditarEmprestimo(emprestimoEditar);

                    break;


                case 4:
                    MenuMembro.realizarDevolucao(membro);
                    break;

                case 5:
                    EmprestimoUtils.realizarEmprestimo(membro);
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Opção inválida!");
                    ConsoleUI.pause();
                    break;
            }

        } while (opcao != 6);
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
