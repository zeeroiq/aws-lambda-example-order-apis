package com.zeero.lambda.apis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.zeero.lambda.apis.dto.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

class ReadOrderAgentTest {
    private ReadOrderAgent lambdaFunction;

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
        lambdaFunction = new ReadOrderAgent();
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

        // Invoke the Lambda function
        APIGatewayProxyResponseEvent response = lambdaFunction.handleRequest(mockInput, mockContext);

        // Verify the response
        assertEquals(200, response.getStatusCode());
        String readObject = response.getBody();

        assertNotNull(readObject);
        assertNotEquals(readObject.trim().length(), 0);

        Order order = new Gson().fromJson(readObject, Order.class);

        assertNotNull(order);
        assertEquals(order.getId(), 123);
        assertEquals(order.getQuantity(), 1000000);
    }
}