package com.exemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class UsuarioRepository {
    private static final String TABLE_NAME = "UsuariosPOO";

    public static Map<String, AttributeValue> buscarPorId(String id) {
        DynamoDbClient client = AWSConfig.getClient();

        GetItemRequest request = GetItemRequest.builder().tableName(TABLE_NAME)
                .key(Map.of("id", AttributeValue.builder().s(id).build())).build();

        GetItemResponse response = client.getItem(request);

        if (!response.hasItem() || response.item().isEmpty()) {
            System.out.println("Usuario não encontrado!");
            return null;
        }

        return response.item();

    }

    public static List<Map<String, AttributeValue>> buscarTodosUsuarios() {
        DynamoDbClient client = AWSConfig.getClient();

        ScanRequest request = ScanRequest.builder().tableName(TABLE_NAME).build();

        ScanResponse response = client.scan(request);

        if (!response.hasItems()) {
            System.out.println("Nenhum usuário encontrado!");
            return new ArrayList<>(); // lista vazia
        }

        return response.items();
    }
}
