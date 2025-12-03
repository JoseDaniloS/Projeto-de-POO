package com.exemplo.utils;

import com.exemplo.models.Bibliotecario;
import com.exemplo.ui.ConsoleUI;

import static com.exemplo.utils.InputUtils.lerCpf;
import static com.exemplo.utils.InputUtils.lerNome;
import static com.exemplo.utils.InputUtils.lerSenha;

public class BibliotecarioUtils {
    public static void editar(Bibliotecario b) {

        ConsoleUI.header("EDITAR BIBLIOTECÁRIO");
        b.verUsuario();
        System.out.println("--------------------------");

        String nome = InputUtils.solicitar("Nome", b.getNome());
        lerNome();
        String cpf = InputUtils.solicitar("CPF", b.getCpf());
        lerCpf();   
        String login = InputUtils.solicitar("Login", b.getLogin());
        String senha = InputUtils.solicitar("Senha", b.getSenha());
        lerSenha();

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
