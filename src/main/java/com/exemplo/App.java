package com.exemplo;

import java.io.Console;
import java.util.Map;
import java.util.Scanner;

import com.exemplo.menus.MenuBibliotecario;
import com.exemplo.menus.MenuMembro;
import com.exemplo.menus.MenuSupervisor;
import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.repositories.UsuarioRepository;
import com.exemplo.ui.ConsoleUI;
import com.exemplo.utils.InputUtils;
import com.exemplo.utils.UsuarioUtils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class App {
    public static void main(String[] args) {

        String login, senha;
        Console console = System.console();
        int opcao = 0;
        do {
            ConsoleUI.header("SISTEMA DE GERENCIAMENTO DE BIBLIOTECA");

            System.out.println("1 - Login");
            System.out.println("2 - Sair");
            opcao = InputUtils.readInt("Escolha:");

            switch (opcao) {
                case 1:
                    ConsoleUI.header("LOGIN");

                    login = InputUtils.readString("Informe o ID:");

                    char[] senhaChars = console.readPassword("Informe a senha:");
                    senha = new String(senhaChars);

                    boolean ehAutenticado = Usuario.autenticar(login, senha);

                    if (ehAutenticado) {
                        systemMenu(login);
                    } else {
                        ConsoleUI.clear();
                        System.out.println("Credenciais inválidas.");
                        ConsoleUI.pause();
                    }
                    break;
                case 2:
                    ConsoleUI.clear();
                    System.out.println("Saindo...");
                    return;

                default:
                    ConsoleUI.clear();
                    System.out.println("Opção Invalida! Tente novamente\n");
                    ConsoleUI.pause();
                    break;
            }
        } while (opcao != 2);

    }

    private static void systemMenu(String id) {

        Map<String, AttributeValue> dadosUsuario = UsuarioRepository.buscarPorId(id);
        if (dadosUsuario == null)
            return;

        Usuario usuario = UsuarioUtils.criaUsuarioBancoDados(dadosUsuario);

        if (usuario instanceof Bibliotecario) {
            Bibliotecario bibliotecario = (Bibliotecario) usuario;
            if (bibliotecario.getCargo().equalsIgnoreCase("supervisor")) {
                MenuSupervisor.exibir(bibliotecario);
            } else {
                MenuBibliotecario.exibir(bibliotecario);
            }
        } else if (usuario instanceof Membro) {
            Membro membro = (Membro) usuario;
            MenuMembro.membroMenu(membro);
        }

    }
}
