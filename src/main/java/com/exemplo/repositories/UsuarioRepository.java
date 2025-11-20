package com.exemplo.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.exemplo.AWSConfig;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
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
                System.out.println("Usuario não encontrado!");
                return null; // Retorna vazio
            }

            return response.item(); // Retorna a lista de itens
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuario por ID");
            System.out.println(e.getMessage());
            return null; // Retorna vazio
        }

    }

    public static void enviarElementoBancoDeDados(Map<String, AttributeValue> elemento, String tableName) {
        DynamoDbClient client = AWSConfig.getClient();
        try {
            PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(elemento).build();
            client.putItem(request);
        } catch (Exception e) {
            System.out.println("Erro ao salvar no DynamoDB: " + e.getMessage());
        }
    }

    public static List<Map<String, AttributeValue>> buscarTodosUsuarios() {
        DynamoDbClient client = AWSConfig.getClient();

        try {
            ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();

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
