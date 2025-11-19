package com.exemplo;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.regions.Region;

public class AWSConfig {

    public static DynamoDbClient getClient() {
        return DynamoDbClient.builder()
                .region(Region.US_EAST_1) // ajuste para sua região
                .build();
    }

}
