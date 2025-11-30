package com.exemplo.utils;

import java.util.Date;
import java.util.Map;

import com.exemplo.AWSConfig;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoUtils {
    public static <T> AttributeValue criarAttributeValueDynamoDB(T campo) {
        if (campo == null) {
            return AttributeValue.builder().nul(true).build();
        }

        if (campo instanceof String) {
            return AttributeValue.builder().s((String) campo).build();
        }

        if (campo instanceof Integer || campo instanceof Long || campo instanceof Double) {
            return AttributeValue.builder().n(String.valueOf(campo)).build();
        }

        if (campo instanceof Boolean) {
            return AttributeValue.builder().bool((Boolean) campo).build();
        }

        throw new IllegalArgumentException("Tipo não suportado: " + campo.getClass());
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

    public static String formatarDataParaBancoDeDados(Date data) {
        if (data == null) {
            return null; // ou "null" ou "0000-00-00" dependendo da sua regra
        }

        return new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(data);
    }

    public static Date parseDateFromDynamoDB(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            // Converter string ISO 8601 para Instant → Date
            return Date.from(java.time.Instant.parse(dateString));
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de data inválido: " + dateString);
        }
    }
}
