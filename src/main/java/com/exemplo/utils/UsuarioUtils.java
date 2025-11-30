package com.exemplo.utils;

import java.util.HashMap;
import java.util.Map;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.ui.ConsoleUI;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class UsuarioUtils {

    public static Usuario criaUsuarioBancoDados(Map<String, AttributeValue> dadosUsuario) {
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
        } else {
            usuario = Membro.criarMembro(
                    dadosUsuario.get("nome").s(),
                    dadosUsuario.get("cpf").s(),
                    dadosUsuario.get("id").s(),
                    dadosUsuario.get("senha").s(),
                    dadosUsuario.get("endereco").s(),
                    dadosUsuario.get("telefone").s(),
                    dadosUsuario.get("email").s());
        }

        return usuario;
    }

    public static Map<String, AttributeValue> toMap(Usuario usuario) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", DynamoUtils.criarAttributeValueDynamoDB(usuario.getLogin()));
        item.put("nome", DynamoUtils.criarAttributeValueDynamoDB(usuario.getNome()));
        item.put("cpf", DynamoUtils.criarAttributeValueDynamoDB(usuario.getCpf()));
        item.put("senha", DynamoUtils.criarAttributeValueDynamoDB(usuario.getSenha()));
        item.put("ativo", DynamoUtils.criarAttributeValueDynamoDB(usuario.isAtivo()));
        if (usuario instanceof Bibliotecario) {
            Bibliotecario b = (Bibliotecario) usuario;
            item.put("cargo", DynamoUtils.criarAttributeValueDynamoDB(b.getCargo()));
        } else if (usuario instanceof Membro) {
            Membro m = (Membro) usuario;
            item.put("endereco", DynamoUtils.criarAttributeValueDynamoDB(m.getEndereco()));
            item.put("telefone", DynamoUtils.criarAttributeValueDynamoDB(m.getTelefone()));
            item.put("email", DynamoUtils.criarAttributeValueDynamoDB(m.getEmail()));
        }

        return item;
    }

    public static boolean isBibliotecario(Usuario usuario) {
        if (usuario instanceof Membro == false) {
            ConsoleUI.clear();
            System.out.println("Usuario informado não é um Membro!");
            ConsoleUI.pause();
            return false;
        }
        return true;
    }
}
