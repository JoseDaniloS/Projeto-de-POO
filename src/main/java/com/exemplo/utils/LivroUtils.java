package com.exemplo.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exemplo.models.Bibliotecario;
import com.exemplo.models.Livro;
import com.exemplo.models.Membro;
import com.exemplo.models.Usuario;
import com.exemplo.ui.ConsoleUI;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class LivroUtils {
    public static Map<String, AttributeValue> toMap(Livro livro) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("isbn", DynamoUtils.criarAttributeValueDynamoDB(livro.getIsbn()));
        item.put("titulo", DynamoUtils.criarAttributeValueDynamoDB(livro.getTitulo()));
        item.put("autor", DynamoUtils.criarAttributeValueDynamoDB(livro.getAutor()));
        item.put("anoPublicacao", DynamoUtils.criarAttributeValueDynamoDB(livro.getAnoPublicacao()));
        item.put("numeroCopias", DynamoUtils.criarAttributeValueDynamoDB(livro.getNumeroCopias()));
        item.put("disponiveis", DynamoUtils.criarAttributeValueDynamoDB(livro.getDisponiveis()));

        return item;
    }

    public static Livro criaLivroBancoDados(Map<String, AttributeValue> dadosLivro) {

        return new Livro(
                dadosLivro.get("isbn").s(),
                dadosLivro.get("titulo").s(),
                dadosLivro.get("autor").s(),
                Integer.parseInt(dadosLivro.get("anoPublicacao").n()),
                Integer.parseInt(dadosLivro.get("numeroCopias").n()),
                Integer.parseInt(dadosLivro.get("disponiveis").n()));

    }

    public static void exibirListaLivros() {
        List<Livro> livros = Livro.listarLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum Livro cadastrado.");
        } else {
            ConsoleUI.header("LISTA DE LIVROS");
            for (Livro l : livros) {
                l.verLivro();
                System.out.println("-----------------------");
            }
        }
        ConsoleUI.pause();
    }
}
