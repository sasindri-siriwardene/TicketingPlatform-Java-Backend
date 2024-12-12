package org.ticket.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.ticket.configuration.SystemConfig;
import org.ticket.data.TicketPool;
import org.ticket.models.Consumer;
import org.ticket.models.Vendor;

import java.io.File;
import java.io.IOException;
import java.util.Random;
/**
 * Handles WebSocket connections for ticketing.
 */
@Component
public class TicketingHandler extends TextWebSocketHandler {
    private TicketPool ticketPool;
    Random random = new Random();

    @Value("classpath:config.json")
    private org.springframework.core.io.Resource resource;

    private int releasrate;
    private int buyrate;
    private int totaltickets;
    private int maxticketcapacity;

    private volatile boolean running = true;

    /**
     * Loads the system configuration from a JSON file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Replace with your file path
            File jsonFile = new File("src/main/resources/config.json");

            // Deserialize JSON into a Java object (example with a generic object)
            SystemConfig config = mapper.readValue(jsonFile, SystemConfig.class);

            // Print the object or process it as needed
            this.releasrate = (int) config.getTicketReleaseRate();
            this.buyrate = (int) config.getCustomerRetrievalRate();
            this.totaltickets = config.getTotalTickets();
            this.maxticketcapacity = config.getMaxTicketCapacity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Called after a WebSocket connection is established.
     *
     * @param session the WebSocket session
     * @throws Exception if an error occurs
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        System.out.println("Connection established");
        this.loadConfig();
        this.running = true;
        this.ticketPool = new TicketPool(maxticketcapacity);

        // Start vendor and consumer threads
        Vendor[] vendors = new Vendor[random.nextInt(20)];
        Consumer[] consumers = new Consumer[random.nextInt(20)];

        for (int i = 0; i < vendors.length; i++) {
            vendors[i] = new Vendor(releasrate, totaltickets, ticketPool,this);
            new Thread(vendors[i], "Vendor ID-" + i).start();
        }
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(buyrate, 5, ticketPool, this);
            new Thread(consumers[i], "Customer ID-" + i).start();
        }
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if ("close".equalsIgnoreCase(message.getPayload())) {
            System.out.println("Received close message");
            closeConnection(session);
        } else {
            // Send ticket information to the client
            session.sendMessage(new TextMessage("Available tickets: " + ticketPool.getAvailableTickets() +
                    " Sold tickets: " + ticketPool.getTicketsSold()
            ));
        }
    }
    /**
     * Closes the WebSocket connection and stops the threads.
     *
     * @param session the WebSocket session
     * @throws IOException if an I/O error occurs
     */
    private void closeConnection(WebSocketSession session) throws IOException {
        running = false; // Stop the threads
        System.out.println("running stat" + this.running);
        session.close();
        System.out.println("Connection closed and threads reset");
        // Reset threads or perform any necessary cleanup here
    }
    /**
     * Checks if the handler is running.
     *
     * @return true if the handler is running, false otherwise
     */
    public synchronized boolean isRunning() {
        return running;
    }

}
