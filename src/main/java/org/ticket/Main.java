package org.ticket;

import org.ticket.cli.ConfigurePlatform;
import org.ticket.data.TicketPool;
import org.ticket.models.Consumer;
import org.ticket.models.Vendor;
import org.ticket.websocket.SpringBootStarter;
import org.ticket.websocket.TicketingHandler;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cli = scanner.nextInt();
            if (cli == 1) {
                //run cli
                ConfigurePlatform.getUserInput();
                TicketPool ticketPool = new TicketPool(10);
// Start vendor and consumer threads
                Vendor[] vendors = new Vendor[5];
                Consumer[] consumers = new Consumer[3];
                TicketingHandler ticketingHandler = new TicketingHandler();
                for(int i = 0; i < vendors.length; i++) {
                    vendors[i] = new Vendor(3,10, ticketPool, ticketingHandler);
                    new Thread(vendors[i], "Vendor ID-" + i).start();
                }
                for (int i = 0; i < consumers.length; i++) {
                    consumers[i] = new Consumer(2,2, ticketPool, ticketingHandler);
                    new Thread(consumers[i], "Customer ID-" + i).start();
                }

            } else {
                // run backend
                SpringBootStarter.main(args);

            }
    }
}