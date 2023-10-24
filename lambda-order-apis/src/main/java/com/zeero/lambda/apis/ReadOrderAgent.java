package com.zeero.lambda.apis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.zeero.lambda.apis.dto.Order;

import java.util.HashMap;
import java.util.Map;

public class ReadOrderAgent implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

        Order order = Order.builder()
                .id(123)
                .itemName("MacBook Air/Pro")
                .quantity(1000000)
                .build();

        String orderJson = new Gson().toJson(order);

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(orderJson);
    }
}
