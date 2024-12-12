package org.ticket.models;
/**
 * Represents a ticket with an ID and price.
 */
public class Ticket {

    private int ticketId;
    private int ticketPrice;
    /**
     * Constructs a Ticket with the specified ID and price.
     *
     * @param id the ID of the ticket
     * @param ticketPrice the price of the ticket
     */
    public Ticket(int id, int ticketPrice) {
        this.ticketId = id;
        this.ticketPrice = ticketPrice;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    /**
     * Returns a string representation of the ticket.
     *
     * @return a string representation of the ticket
     */
    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID='" + ticketId + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
