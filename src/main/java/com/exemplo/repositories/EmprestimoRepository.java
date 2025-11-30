package com.exemplo.repositories;

import java.util.List;
import java.util.Map;

import com.exemplo.AWSConfig;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class EmprestimoRepository {
    public static final String TABLE_NAME = "EmprestimosPOO";

    public static List<Map<String, AttributeValue>> buscarPorMembro(String cpf) {
        DynamoDbClient client = AWSConfig.getClient();

        try {
            Map<String, AttributeValue> key = Map.of(":cpfValue", AttributeValue.fromS(cpf));

            QueryRequest request = QueryRequest.builder().tableName(TABLE_NAME).indexName("cpf-index")
                    .keyConditionExpression("cpf = :cpfValue").expressionAttributeValues(key).build();

            QueryResponse response = client.query(request);
            if (!response.hasItems() || response.items().isEmpty()) {
                System.out.println("Nenhum empréstimo encontrado!");
                return null;
            }
            return response.items();

        } catch (Exception e) {
            System.out.println("Erro ao buscar Emprestimo por Membro");
            System.out.println(e.getMessage());
            return null; // Retorna vazio
        }

    }

    // public static void excluir(String isbn) {
    // DynamoDbClient client = AWSConfig.getClient();

    // try {
    // DeleteItemRequest request = DeleteItemRequest.builder().tableName(TABLE_NAME)
    // .key(Map.of("isbn", AttributeValue.fromS(isbn))).build();

    // client.deleteItem(request);
    // } catch (Exception e) {
    // System.out.println("Erro ao excluir livro");
    // System.out.println(e.getMessage());
    // }

    // }

    // public static List<Map<String, AttributeValue>> buscarTodosLivros() {
    // DynamoDbClient client = AWSConfig.getClient();

    // try {
    // ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME)
    // .build();

    // ScanResponse response = client.scan(request);

    // if (!response.hasItems()) {
    // System.out.println("Nenhum Livro encontrado!");
    // return null;// Retorna vazio
    // }
    // return response.items(); // Retorna a lista de itens
    // } catch (Exception e) {
    // System.out.println("Erro ao buscar todos os Livro");
    // System.out.println(e.getMessage());
    // return null; // Retorna vazio
    // }
    // }
}
