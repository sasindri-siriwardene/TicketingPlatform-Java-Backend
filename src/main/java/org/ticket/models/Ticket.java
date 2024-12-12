package org.ticket.models;

public class Ticket {

    private int ticketId;
    private int ticketPrice;

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

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketID='" + ticketId + '\'' +
                ", ticketPrice=" + ticketPrice +
                '}';
    }
}
