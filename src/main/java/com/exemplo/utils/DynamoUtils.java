package com.exemplo.utils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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
}
