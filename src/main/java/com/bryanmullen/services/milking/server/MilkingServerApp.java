package com.bryanmullen.services.milking.server;

import java.io.IOException;

/**
 * MilkingServerApp is the main class for the MilkingServer.
 */
public class MilkingServerApp {
    public static void main(String[] args) throws IOException {
        // Create a new MilkingServer and pass in the properties file path and the service to bind.
        var milkingServer = new MilkingServer("src/main/resources/milking.properties", new MilkingServiceImpl());

        // Run the server.
        milkingServer.run();
    }
}
