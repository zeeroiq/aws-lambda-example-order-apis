package com.zeero.lambda.apis;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.zeero.lambda.apis.dto.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CreateOrderAgentTest {

    @InjectMocks
    private CreateOrderAgent createOrderAgent;

    @Mock
    private APIGatewayProxyRequestEvent mockInput;

    @Mock
    private Context mockContext;
    @Mock
    private AmazonDynamoDB amazonDynamoDB;
    @InjectMocks
    private DynamoDB dynamoDB;
    @Mock
    private Table orderTable;


    //Keeping session object in a field so that we can complete session in 'tear down' method.
    //It is recommended to hide the session object, along with 'setup' and 'tear down' methods in a base class / runner.
    //Keep in mind that you can use Mockito's JUnit runner or rule instead of MockitoSession and get the same behavior.
    MockitoSession mockito;

    @BeforeEach
    void setup() {
        //initialize session to start mocking
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.LENIENT)
                .startMocking();
        // Mock the AmazonDynamoDBClientBuilder to return a mock DynamoDB client
//        try {
//            Field dnmDB = createOrderAgent.getClass().getDeclaredField("dynamoDB");
//            dnmDB.setAccessible(true);
//            dnmDB.set(createOrderAgent, dynamoDB);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
    }

    @AfterEach
    void tearDown() {
        //It is necessary to finish the session so that Mockito
        // can detect incorrect stubbing and validate Mockito usage
        //'finishMocking()' is intended to be used in your test framework's 'tear down' method.
        mockito.finishMocking();
    }


    @Test
    @Disabled
    void testHandleRequest() {
        // Create a sample order
        Order order = Order.builder()
                .id(123)
                .quantity(100000)
                .itemName("Dell Latitude 3440")
                .build();
        // Serialize the order to JSON
        String orderJson = new Gson().toJson(order);

        // Mock the input
        when(orderTable.putItem(any(Item.class))).thenReturn(null);
        when(dynamoDB.getTable("ORDER_TABLE")).thenReturn(orderTable);
        when(mockInput.getBody()).thenReturn(orderJson);

        // Invoke the Lambda function
        APIGatewayProxyResponseEvent response = createOrderAgent.handleRequest(mockInput, mockContext);

        // Verify the response
        assertEquals(200, response.getStatusCode());
        assertEquals("Order ID - 123", response.getBody());

        // Optionally, you can further assert the response headers as well
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
        assertEquals("application/json", response.getHeaders().get("X-Custom-Header"));
    }

}