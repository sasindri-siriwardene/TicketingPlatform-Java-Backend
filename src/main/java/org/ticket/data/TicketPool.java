package org.ticket.data;

import org.ticket.models.Ticket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketPool {
    /**
     * Manages the pool of tickets, including adding and buying tickets.
     */

    private int maxTicketCount;
    private final BlockingQueue<Ticket> tickets;

    private AtomicInteger soldTickets;
    /**
     * Constructs a TicketPool with the specified maximum ticket count.
     *
     * @param ticketPoolSize the maximum number of tickets in the pool
     */
    public TicketPool(int ticketPoolSize) {
        this.maxTicketCount = ticketPoolSize;
        this.tickets = new LinkedBlockingQueue<>(ticketPoolSize);
        this.soldTickets = new AtomicInteger(0);
    }

    /**
     * Adds a ticket to the pool.
     *
     * @param ticket the ticket to add
     */
    public synchronized void addTicket(Ticket ticket) {
        synchronized (tickets) {
            while (tickets.size() >= maxTicketCount) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace(); // For command line interface (CLI)
                    throw new RuntimeException(e.getMessage()); // For Client-Server application
                }
            }
            this.tickets.add(ticket);
            notifyAll();
            System.out.println("Ticket added by - " + Thread.currentThread().getName());
            System.out.println("Available tickets - " + tickets.size());
        }
    }
    /**
     * Buys a ticket from the pool.
     *
     * @return the bought ticket
     */
    public synchronized Ticket buyTicket() {

        while (tickets.isEmpty()) {
            try {
                //wait while tickets are available
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        Ticket ticket = this.tickets.poll();
        soldTickets.incrementAndGet();
        notifyAll();
        System.out.println("Ticket bought by - " + Thread.currentThread().getName() );
        System.out.println("Available tickets - " + tickets.size());
        return ticket;
    }
    /**
     * Gets the number of available tickets in the pool.
     *
     * @return the number of available tickets
     */
    public int getAvailableTickets() {
        return tickets.size();
    }
    /**
     * Gets the number of tickets sold.
     *
     * @return the number of tickets sold
     */

    public int getTicketsSold() {
        return soldTickets.get();
    }

}
