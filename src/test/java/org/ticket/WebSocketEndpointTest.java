package org.ticket.websocket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketEndpointTest {

    @Autowired
    private WebSocketStompClient stompClient;

    @Test
    public void testGetTicketsEndpoint() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        String url = "ws://localhost:8080/gettickets";
        StompSession session = stompClient.connect(url, new StompSessionHandlerAdapter() {
        }).get(30, TimeUnit.SECONDS);

        session.subscribe("/topic/tickets", new StompSessionHandlerAdapter() {
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                // Assuming the payload is a JSON string representing ticket information
                String expectedResponse = "Available tickets: 10 - Sold tickets: 5 - Total tickets: 15";
                assertEquals(expectedResponse, payload.toString());
                latch.countDown();
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }
        });

        session.send("/gettickets", null);

        assertTrue(latch.await(3, TimeUnit.SECONDS));
    }
}