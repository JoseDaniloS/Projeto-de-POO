package com.exemplo.menus;

import java.util.List;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Livro;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.LivrosRepository;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.DynamoUtils;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.LivroUtils;
import com.exemplo.utils.MembroUtils;
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
            System.out.println("6 - Logout");
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
                    cadastrarLivro();
                    break;
                case 5:
                    gerenciarLivros();
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (option != 6);

    }

    private static void cadastrarLivro() {
        try {
            ConsoleUI.header("CADASTRAR LIVRO");
            String isbn = InputUtils.readString("Informe o ISBN:");
            String titulo = InputUtils.readString("Informe o Titulo:");
            String autor = InputUtils.readString("Informe Autor:");
            int anoPublicacao = InputUtils.readInt("Informe o Ano de Publicação:");
            int endereco = InputUtils.readInt("Informe o Número de Cópias:");

            Livro novoLivro = Livro.criarLivro(isbn, titulo, autor, anoPublicacao, endereco);

            DynamoUtils.enviarElementoBancoDeDados(LivroUtils.toMap(novoLivro), "LivrosPOO");
            ConsoleUI.clear();
            System.out.println("Livro " + titulo + " cadastrado com sucesso!");
            ConsoleUI.pause();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar um novo Livro!");
            // System.out.println(e.getMessage());
            ConsoleUI.pause();
        }
    }

    private static void gerenciarLivros() {
        try {
            int option = 0;
            do {
                ConsoleUI.header("GERENCIAMENTO DE LIVROS");
                System.out.println("1 - Listar Livros");
                System.out.println("2 - Editar Livro");
                System.out.println("3 - Excluir Livro");
                System.out.println("4 - Voltar");
                option = InputUtils.readInt("Escolha:");
                ConsoleUI.clear();
                switch (option) {
                    case 1:
                        LivroUtils.exibirListaLivros();
                        break;
                    case 2:
                        String isbn =
                                InputUtils.readString("Informe o ISBN do livro a ser editado: ");

                        Livro livro = LivroUtils
                                .criaLivroBancoDados(LivrosRepository.buscarPorIsbn(isbn));

                        if (livro == null) {
                            System.out.println("Livro não encontrado!");
                            ConsoleUI.pause();
                            return;
                        }
                        LivroUtils.editar(livro);
                        break;
                    case 3:
                        excluirLivro();
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



    private static void excluirLivro() {
        String isbn = InputUtils.readString("Informe o ISBN do Livro a ser desativado: ");
        Livro livro = LivroUtils.criaLivroBancoDados(LivrosRepository.buscarPorIsbn(isbn));

        if (livro == null) {
            System.out.println("Livro não encontrado!");
            ConsoleUI.pause();
            return;
        }
        Livro.excluirLivro(livro);

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
                        if (membros.isEmpty()) {
                            System.out.println("Nenhum membro cadastrado.");
                        } else {
                            ConsoleUI.header("LISTA DE MEMBROS");
                            for (Membro m : membros) {
                                m.verMembro();
                                System.out.println("-----------------------");
                            }
                        }
                        ConsoleUI.pause();
                        break;
                    case 2:
                        String id = InputUtils.readString("Informe o ID do membro a ser editado: ");

                        Usuario usuarioBanco = UsuarioUtils
                                .criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));

                        if (!(usuarioBanco instanceof Membro)) {
                            System.out.println("O ID informado não pertence a um membro!");
                            ConsoleUI.pause();
                            return;
                        }

                        Membro membro = (Membro) usuarioBanco;

                        MembroUtils.editar(membro);
                        break;
                    case 3:
                        desativarMembro();
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
            System.out.println("Erro ao gerenciar membros: " + e.getMessage());
            ConsoleUI.pause();
        }
    }

    private static void desativarMembro() {
        String id = InputUtils.readString("Informe o ID do Bibliotecario a ser desativado: ");
        // bucar no banco de dados
        Usuario usuario = UsuarioUtils.criaUsuarioBancoDados(UsuarioRepository.buscarPorId(id));
        if (usuario == null) {
            System.out.println("Membro não encontrado!");
            ConsoleUI.pause();
            return;
        }
        
        if (usuario instanceof Membro == false) {
            System.out.println("Usuario informado não é um Membro!");
            ConsoleUI.pause();
            return;
        }
        
        Usuario.desativarUsuario(usuario);
        usuario.verUsuario();
        DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(usuario), "UsuariosPOO");
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

            Membro novoMembro =
                    Membro.criarMembro(nome, cpf, login, senha, endereco, telefone, email);

            DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(novoMembro), "UsuariosPOO");
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
