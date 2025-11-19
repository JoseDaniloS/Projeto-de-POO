package com.exemplo;

import java.util.Map;
import java.util.Scanner;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class App {
    public static void main(String[] args) {
        int option;
        Scanner scan = new Scanner(System.in);

        do {
            System.out.println("=================");
            System.out.println("SISTEMA DE GERENCIAMENTO DE BIBLIOTECA");
            System.out.println("OPÇÕES");
            System.out.println("1 - LOGIN");
            System.out.println("2 - CADASTRAR");
            System.out.println("Selecione uma opção:");
            option = scan.nextInt();
            scan.nextLine(); // limpar o ENTER que sobre
            switch (option) {
                case 1:
                    loginMenu();
                    break;
                case 2:

                    break;

                default:
                    System.out.println("Opção Invalida! Digite Novamente\n");
                    break;
            }
        } while (option != 1 && option != 2);
    }

    private static void loginMenu() {
        String login, senha;
        Scanner scan = new Scanner(System.in);

        System.out.println("=== LOGIN ===");
        System.out.println("Informe o ID:");
        login = scan.nextLine();
        System.out.println("Informe a senha:");
        senha = scan.nextLine();
        boolean ehAutenticado = Usuario.autenticar(login, senha);
        if (ehAutenticado) {
            systemMenu(login);
        }
    }

    private static void systemMenu(String id) {
        Map<String, AttributeValue> dadosUsuario = UsuarioRepository.buscarPorId(id);
        String cargo = dadosUsuario.containsKey("cargo") ? dadosUsuario.get("cargo").s() : "";

        Usuario usuario;
        if (!cargo.isEmpty()) {
            // É bibliotecário
            usuario = Bibliotecario.criarBibliotecario(
                    dadosUsuario.get("nome").s(),
                    dadosUsuario.get("cpf").s(),
                    dadosUsuario.get("id").s(),
                    dadosUsuario.get("senha").s(),
                    cargo);

            System.out.println("Logado como Bibliotecário!");
            System.out.println("Bem vindo! " + usuario.getNome());
        } else {
            usuario = Membro.criarMembro(
                    dadosUsuario.get("nome").s(),
                    dadosUsuario.get("cpf").s(),
                    dadosUsuario.get("id").s(),
                    dadosUsuario.get("senha").s(),
                    dadosUsuario.get("endereco").s(),
                    dadosUsuario.get("telefone").s(),
                    dadosUsuario.get("email").s());
            System.out.println("Logado como Membro!");
            System.out.println("Bem vindo! " + usuario.getNome());
        }

    }
}
