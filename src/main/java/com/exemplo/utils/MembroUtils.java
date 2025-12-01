package com.exemplo.utils;

import com.exemplo.models.Membro;
import com.exemplo.ui.ConsoleUI;

public class MembroUtils {
    public static void editar(Membro m) {

        ConsoleUI.header("EDITAR MEMBRO");
        m.verUsuario();
        System.out.println("--------------------------");

        String nome = InputUtils.solicitar("Nome", m.getNome());
        String cpf = InputUtils.solicitar("CPF", m.getCpf());
        String login = InputUtils.solicitar("Login", m.getLogin());
        String senha = InputUtils.solicitar("Senha", m.getSenha());
        String endereco = InputUtils.solicitar("Endereço", m.getEndereco());
        String telefone = InputUtils.solicitar("Telefone", m.getTelefone());
        String email = InputUtils.solicitar("Email", m.getEmail());

        // Atualizando o objeto com o que o usuário informou
        Membro.editarMembro(m, nome, cpf, login, senha, true, endereco, telefone, email);

        // Salvar no DynamoDB
        DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(m), "UsuariosPOO");

        ConsoleUI.pause();
    }
}
