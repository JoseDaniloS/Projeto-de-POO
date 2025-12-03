package com.exemplo.utils;

import com.exemplo.models.Membro;
import com.exemplo.ui.ConsoleUI;

import static com.exemplo.utils.InputUtils.lerCpf;
import static com.exemplo.utils.InputUtils.lerEmail;
import static com.exemplo.utils.InputUtils.lerEndereco;
import static com.exemplo.utils.InputUtils.lerNome;
import static com.exemplo.utils.InputUtils.lerSenha;
import static com.exemplo.utils.InputUtils.lerTelefone;

public class MembroUtils {
    public static void editar(Membro m) {

        ConsoleUI.header("EDITAR MEMBRO");
        m.verUsuario();
        System.out.println("--------------------------");

        String nome = InputUtils.solicitar("Nome", m.getNome());
        lerNome();
        String cpf = InputUtils.solicitar("CPF", m.getCpf());
        lerCpf();
        String login = InputUtils.solicitar("Login", m.getLogin());
        String senha = InputUtils.solicitar("Senha", m.getSenha());
        lerSenha();
        String endereco = InputUtils.solicitar("Endereço", m.getEndereco());
        lerEndereco();  
        String telefone = InputUtils.solicitar("Telefone", m.getTelefone());
        lerTelefone();
        String email = InputUtils.solicitar("Email", m.getEmail());
        lerEmail();

        // Atualizando o objeto com o que o usuário informou
        Membro.editarMembro(m, nome, cpf, login, senha, true, endereco, telefone, email);

        // Salvar no DynamoDB
        DynamoUtils.enviarElementoBancoDeDados(UsuarioUtils.toMap(m), "UsuariosPOO");

        ConsoleUI.pause();
    }
}
