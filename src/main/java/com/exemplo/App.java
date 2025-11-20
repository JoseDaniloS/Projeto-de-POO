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
import com.exemplo.utils.SystemUtils;
import com.exemplo.utils.UsuarioUtils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class App {
    public static void main(String[] args) {

        System.out.println("=================");
        System.out.println("SISTEMA DE GERENCIAMENTO DE BIBLIOTECA");
        String login, senha;
        Scanner scan = new Scanner(System.in);
        Console console = System.console();
        
        System.out.println("=== LOGIN ===");
        System.out.println("Informe o ID:");
        login = scan.nextLine();
        char[] senhaChars = console.readPassword("Informe a senha:");
        senha = new String(senhaChars);
        boolean ehAutenticado = Usuario.autenticar(login, senha);
        if (ehAutenticado) {
            SystemUtils.clearConsole();
            systemMenu(login);
        }

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
