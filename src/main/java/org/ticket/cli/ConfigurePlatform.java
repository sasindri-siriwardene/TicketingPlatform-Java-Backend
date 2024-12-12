package org.ticket.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticket.configuration.SystemConfig;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Read user configuration from CLI.
 */
public class ConfigurePlatform {

    public static void getUserInput() {
        Scanner scanner = new Scanner(System.in);
        int totalTickets = 0;
        double ticketReleaseRate = 0;
        double customerRetrievalRate = 0;
        int maxTicketCapacity = 0;

        System.out.println("Welcome to the ticketing platform!");

        do {
            System.out.println("Total number of tickets: ");
            try {
                totalTickets = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input invalid.total number of tickets must be a valid input");
                scanner.next(); // Clear the invalid input
            }
            if (totalTickets < 1) {
                System.out.println("Enter positive value");
            }
        } while (totalTickets < 1);

        do {
            System.out.println("Ticket release rate(Tickets released per second): ");
            try {
                ticketReleaseRate = scanner.nextDouble();
                if (ticketReleaseRate <= 0) {
                    System.out.println("Ticket release rate must be positive");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ticket release rate must be a valid input");
                scanner.next(); // Clear the invalid input
            }
        } while (ticketReleaseRate <= 0);


        do {
            System.out.println("Customer retrieval rate(Customer buying per second): ");
            try {
                customerRetrievalRate = scanner.nextDouble();
                if (customerRetrievalRate <= 0 || customerRetrievalRate > ticketReleaseRate) {
                    System.out.println("Customer retrieval  rate must be positive and higher " +
                            "than ticket release rate");
                }
            } catch (InputMismatchException e) {
                System.out.println("Customer retrieval rate must be a valid input");
                scanner.next(); // Clear the invalid input
            }
        } while (customerRetrievalRate <= 0 || customerRetrievalRate > ticketReleaseRate);


        do {
            System.out.println("Maximum ticket capacity: ");
            try {
                maxTicketCapacity = scanner.nextInt();
                if (maxTicketCapacity <= 0 || maxTicketCapacity >= totalTickets) {
                    System.out.println("Maximum ticket capacity should be positive and" +
                            " less than or equal to total tickets");
                }
            } catch (InputMismatchException e) {
                System.out.println("Maximum ticket capacity must be a valid input");
                scanner.next(); // Clear the invalid input
            }
        } while (maxTicketCapacity <= 0 || maxTicketCapacity >= totalTickets);

        // Create a configuration object
        SystemConfig configuration = new
                SystemConfig(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        // Write the configuration object to a JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("src/main/resources/config.json"), configuration);
            System.out.println("Configuration saved to config.json");
        } catch (IOException e) {
            System.out.println("Failed to save configuration: " + e.getMessage());
        }
    }
}
