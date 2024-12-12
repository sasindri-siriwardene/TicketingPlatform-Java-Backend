package org.ticket.models;

import org.ticket.data.TicketPool;
import org.ticket.websocket.TicketingHandler;
/**
 * Represents a customer who buys tickets.
 */
public class Consumer implements Runnable {

    private int customerRetrievalRate;
    private int buyingAmount;
    private TicketPool ticketPool;

    private TicketingHandler handler;


    /**
     * Constructs a Consumer with the specified parameters.
     *
     * @param buyRate the rate at which the customer buys tickets
     * @param numberOfTickets the number of tickets the customer buys
     * @param ticketPool the pool of tickets
     * @param handler the WebSocket handler
     */
    public Consumer(int buyRate, int numberOfTickets, TicketPool ticketPool, TicketingHandler handler) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = buyRate;
        this.buyingAmount = numberOfTickets;
        this.handler = handler;
    }
    /**
     * Runs the consumer thread to buy tickets.
     */
    @Override
    public void run() {

        while (handler.isRunning()) {
            System.out.println("Consumer is running");
            for (int i = 0; i < buyingAmount; i++) {
                Ticket ticket = ticketPool.buyTicket();
                System.out.println("Ticket is - " + ticket);
                try {
                    //mimick customer retrieval rate
                    Thread.sleep(customerRetrievalRate * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
