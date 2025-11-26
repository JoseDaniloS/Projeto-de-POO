package com.exemplo.menus;

import java.util.List;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
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
            ConsoleUI.clear();
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
                    ConsoleUI.pause();
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
                        editarMembro();
                        break;
                    case 3:
                        desativarMembro();
                        break;
                    case 4:
                        System.out.println("Voltando...");
                        break;
                    default:
                        ConsoleUI.clear();
                        System.out.println("Opção inválida.");
                        ConsoleUI.pause();
                }
            } while (option != 4);
            return;
        } catch (Exception e) {
            System.out.println("Erro ao gerenciar membros: " + e.getMessage());
            ConsoleUI.pause();
        }
    }

    private static void editarMembro() {
        // Informar o ID;
        String id = InputUtils.readString("Informe o ID do membro a ser editado:");
        Usuario usuarioBancoDados = UsuarioUtils.criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));
        if (usuarioBancoDados instanceof Membro == false) {
            ConsoleUI.clear();
            System.out.println("Usuario informado não é um Membro!");
            ConsoleUI.pause();
            return;
        }
        Membro membro = (Membro) usuarioBancoDados;
        try {
            int option;
            do {
                ConsoleUI.header("EDITAR MEMBRO");
                System.out.println("1 - NOME");
                System.out.println("2 - CPF");
                System.out.println("3 - LOGIN");
                System.out.println("4 - SENHA");
                System.out.println("5 - ENDERCO");
                System.out.println("6 - TELEFONE");
                System.out.println("7 - EMAIL");
                System.out.println("8 - VOLTAR");
                option = InputUtils.readInt("Escolha:");
                switch (option) {
                    case 1:
                        String nome = InputUtils.readString("Informe o nome:");
                        membro.setNome(nome);
                        break;
                    case 2:
                        String cpf = InputUtils.readString("Informe o cpf:");
                        membro.setCpf(cpf);
                        break;
                    case 3:
                        String login = InputUtils.readString("Informe o login:");
                        membro.setLogin(login);
                        break;
                    case 4:
                        String senha = InputUtils.readString("Informe a senha:");
                        membro.setSenha(senha);
                        break;
                    case 5:
                        String endereco = InputUtils.readString("Informe o enderço:");
                        membro.setEndereco(endereco);
                        break;
                    case 6:
                        String telefone = InputUtils.readString("Informe o telefone:");
                        membro.setTelefone(telefone);
                        break;
                    case 7:
                        String email = InputUtils.readString("Informe o email:");
                        membro.setEmail(email);
                        break;
                    case 8:
                        System.out.println("Voltando...");
                        return;

                    default:
                        System.out.println("Opção inválida.");
                        ConsoleUI.pause();
                        break;

                }
                UsuarioRepository.enviarElementoBancoDeDados(UsuarioUtils.toMap(membro), "UsuariosPOO");
                ConsoleUI.clear();
                System.out.println("Membro editado com sucesso!");
                ConsoleUI.pause();

            } while (option != 8);
        } catch (Exception e) {
            // TODO: handle exception
            ConsoleUI.clear();
            System.out.println("Erro ao editar membro" + e.getMessage());
            ConsoleUI.pause();
        }

    }

    private static void desativarMembro() {
        String id = InputUtils.readString("Informe o ID do Bibliotecario a ser desativado: ");
        // bucar no banco de dados
        Usuario usuario = UsuarioUtils.criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));
        if (usuario instanceof Membro == false) {
            System.out.println("Usuario informado não é um Membro!");
            ConsoleUI.pause();
            return;
        }
        if (usuario == null) {
            System.out.println("Membro não encontrado!");
            ConsoleUI.pause();
            return;
        }
        Usuario.desativarUsuario(usuario);
        usuario.verUsuario();
        UsuarioRepository.enviarElementoBancoDeDados(UsuarioUtils.toMap(usuario), "UsuariosPOO");
        ConsoleUI.clear();
        System.out.println("Membro desativado com sucesso!");
        ConsoleUI.pause();
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
