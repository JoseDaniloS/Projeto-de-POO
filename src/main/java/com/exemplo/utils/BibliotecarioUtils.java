package com.exemplo.utils;

import com.exemplo.models.Bibliotecario;
import com.exemplo.ui.ConsoleUI;

public class BibliotecarioUtils {
    public static void editar(Bibliotecario b) {

        ConsoleUI.header("EDITAR BIBLIOTECÁRIO");
        b.verUsuario();
        System.out.println("--------------------------");

        String nome = InputUtils.solicitar("Nome", b.getNome());
        String cpf = InputUtils.solicitar("CPF", b.getCpf());
        String login = InputUtils.solicitar("Login", b.getLogin());
        String senha = InputUtils.solicitar("Senha", b.getSenha());

        boolean ativo = InputUtils.solicitarBoolean("Usuário ativo?", b.isAtivo());

        String cargo = solicitarCargo(b.getCargo());

       
        Bibliotecario.editarBibliotecario(b, nome, cpf, login, senha, ativo, cargo);

        System.out.println("Bibliotecário atualizado com sucesso!");
        ConsoleUI.pause();
    }

 

    private static String solicitarCargo(String cargoAtual) {
        System.out.println("Cargo atual: " + cargoAtual);
        System.out.println("1 - Estagiário");
        System.out.println("2 - Atendente");
        System.out.println("ENTER mantém atual");

        String entrada = InputUtils.readString("Escolha: ").trim();

        switch (entrada) {
            case "1": return "estagiario";
            case "2": return "atendente";
            default: 
                return cargoAtual;
        }
    }
}
