package org.ticket.models;

import org.ticket.data.TicketPool;
import org.ticket.websocket.TicketingHandler;
/**
 * Represents a vendor who adds tickets to the pool.
 */
public class Vendor implements Runnable {

    private TicketPool ticketPool;
    private int ticketReleaseRate;
    private int totalTicketsPerVendor;

    private TicketingHandler handler;


    /**
     * Constructs a Vendor with the specified parameters.
     *
     * @param ticketReleaseRate the rate at which tickets are released
     * @param totalTicketsPerVendor the total number of tickets per vendor
     * @param ticketPool the pool of tickets
     * @param handler the WebSocket handler
     */
    public Vendor(int ticketReleaseRate, int totalTicketsPerVendor, TicketPool ticketPool, TicketingHandler handler) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTicketsPerVendor = totalTicketsPerVendor;
        this.handler = handler;
    }
    /**
     * Runs the vendor thread to add tickets to the pool.
     */
    @Override
    public void run() {
        while (handler.isRunning()) {
            System.out.println("Vendor is running");
            for(int i = 0; i < totalTicketsPerVendor; i++) {
                Ticket ticket = new Ticket(i, 1000);
                ticketPool.addTicket(ticket);
                try {
                    Thread.sleep(ticketReleaseRate * 1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Vendor is stopped");

    }
}
