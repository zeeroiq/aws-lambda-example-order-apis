package com.zeero.lambda.apis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.zeero.lambda.apis.dto.Order;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateOrderAgentTest {

    private CreateOrderAgent lambdaFunction;

    @Mock
    private APIGatewayProxyRequestEvent mockInput;

    @Mock
    private Context mockContext;

    //Keeping session object in a field so that we can complete session in 'tear down' method.
    //It is recommended to hide the session object, along with 'setup' and 'tear down' methods in a base class / runner.
    //Keep in mind that you can use Mockito's JUnit runner or rule instead of MockitoSession and get the same behavior.
    MockitoSession mockito;

    @BeforeEach
    void setup() {
        //initialize session to start mocking
        mockito = Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();
        lambdaFunction = new CreateOrderAgent();
    }

    @AfterEach
    void tearDown() {
        //It is necessary to finish the session so that Mockito
        // can detect incorrect stubbing and validate Mockito usage
        //'finishMocking()' is intended to be used in your test framework's 'tear down' method.
        mockito.finishMocking();
    }


    @Test
    void testHandleRequest() {
        // Create a sample order
        Order order = new Order();
        order.setId(123);

        // Serialize the order to JSON
        String orderJson = new Gson().toJson(order);

        // Mock the input
        Mockito.when(mockInput.getBody()).thenReturn(orderJson);

        // Invoke the Lambda function
        APIGatewayProxyResponseEvent response = lambdaFunction.handleRequest(mockInput, mockContext);

        // Verify the response
        assertEquals(200, response.getStatusCode());
        assertEquals("Order ID - 123", response.getBody());

        // Optionally, you can further assert the response headers as well
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
        assertEquals("application/json", response.getHeaders().get("X-Custom-Header"));
    }

}