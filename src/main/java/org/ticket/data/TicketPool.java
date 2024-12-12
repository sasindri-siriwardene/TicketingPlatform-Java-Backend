package org.ticket.data;

import org.ticket.models.Ticket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketPool {


    private int maxTicketCount;
    private final BlockingQueue<Ticket> tickets;

    private AtomicInteger soldTickets;

    public TicketPool(int ticketPoolSize) {
        this.maxTicketCount = ticketPoolSize;
        this.tickets = new LinkedBlockingQueue<>(ticketPoolSize);
        this.soldTickets = new AtomicInteger(0);
    }

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

    public int getAvailableTickets() {
        return tickets.size();
    }

    public int getTicketsSold() {
        return soldTickets.get();
    }

}
