package com.exemplo.repositories;

import java.util.List;
import java.util.Map;

import com.exemplo.AWSConfig;
import com.exemplo.models.Usuario;
import com.exemplo.utils.UsuarioUtils;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class UsuarioRepository {
    private static final String TABLE_NAME = "UsuariosPOO";

    public static Map<String, AttributeValue> buscarPorId(String id) {
        DynamoDbClient client = AWSConfig.getClient();

        try {
            GetItemRequest request = GetItemRequest.builder().tableName(TABLE_NAME)
                    .key(Map.of("id", AttributeValue.builder().s(id).build())).build();

            GetItemResponse response = client.getItem(request);

            if (!response.hasItem() || response.item().isEmpty()) {
                throw new RuntimeException("Usuario não encontrado!");

            }

            return response.item(); // Retorna a lista de itens
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuario por ID " + e.getMessage());

        }

    }

    public static Usuario buscarPorCpf(String cpf) {
        DynamoDbClient client = AWSConfig.getClient();
        Map<String, AttributeValue> key = Map.of(":cpfValue", AttributeValue.fromS(cpf));

        try {
            QueryRequest request = QueryRequest.builder().tableName(TABLE_NAME).indexName("cpf-index")
                    .keyConditionExpression("cpf = :cpfValue").expressionAttributeValues(key).build();

            QueryResponse response = client.query(request);

            if (!response.hasItems() || response.items().isEmpty()) {
                System.out.println("Usuário não encontrado!");
                return null; // Retorna vazio
            }
            return UsuarioUtils.criaUsuarioBancoDados(response.items().get(0));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuario por CPF " + e.getMessage());
        }
    }

    public static List<Map<String, AttributeValue>> buscarTodosBibliotecarios() {
        DynamoDbClient client = AWSConfig.getClient();

        try {
            ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME)
                    .filterExpression("attribute_exists(cargo)")
                    .build();

            ScanResponse response = client.scan(request);

            if (!response.hasItems()) {
                System.out.println("Nenhum usuário encontrado!");
                return null;// Retorna vazio
            }
            return response.items(); // Retorna a lista de itens
        } catch (Exception e) {
            System.out.println("Erro ao buscar todos os usuarios");
            System.out.println(e.getMessage());
            return null; // Retorna vazio
        }
    }

    public static List<Map<String, AttributeValue>> buscarTodosMembros() {
        DynamoDbClient client = AWSConfig.getClient();

        try {
            ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME)
                    .filterExpression("attribute_not_exists(cargo)")
                    .build();

            ScanResponse response = client.scan(request);

            if (!response.hasItems()) {
                System.out.println("Nenhum usuário encontrado!");
                return null;// Retorna vazio
            }
            return response.items(); // Retorna a lista de itens
        } catch (Exception e) {
            System.out.println("Erro ao buscar todos os usuarios");
            System.out.println(e.getMessage());
            return null; // Retorna vazio
        }
    }
}
