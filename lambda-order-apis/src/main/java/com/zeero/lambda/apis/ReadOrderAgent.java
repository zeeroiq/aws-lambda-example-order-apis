package com.zeero.lambda.apis;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.zeero.lambda.apis.dto.Order;

import java.util.List;

public class ReadOrderAgent implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private static final String ORDER_TABLE = "ORDER_TABLE";
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

        ScanResult scanResult = dynamoDB.scan(new ScanRequest().withTableName(System.getenv(ORDER_TABLE)));
        assert scanResult != null;
        List<Order> orderList = scanResult.getItems()
                .parallelStream()
                .map(item -> Order.builder()
                        .id(Integer.parseInt(item.get("id").getN()))
                        .itemName(item.get("itemName").getS())
                        .quantity(Integer.parseInt(item.get("quantity").getN()))
                        .build())
                .toList();

        String orderJson = new Gson().toJson(orderList);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(orderJson);
    }
}
