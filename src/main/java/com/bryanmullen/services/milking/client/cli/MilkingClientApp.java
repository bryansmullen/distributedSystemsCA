package com.bryanmullen.services.milking.client.cli;

import java.io.IOException;

/**
 * MilkingClientApp - This class is the main class for the milking client application.
 */
public class MilkingClientApp {
    public static void main(String[] args) throws IOException {
        // create a new milking client object and pass in the properties file path
        var milkingClient = new MilkingClient("src/main/resources/milking" +
                ".properties");

        // get the service info from the service discovery
        milkingClient.getService();

        // call the remote procedures calls
        milkingClient.milkCollection();
        milkingClient.milkProduction();
        milkingClient.milkCurrentCow();
    }
}
