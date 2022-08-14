package com.bryanmullen.services.feed.server;

import java.io.IOException;

/**
 * FeedServerApp is the main class for the FeedServer.
 */
public class FeedServerApp {
    public static void main(String[] args) throws IOException {
        // Create a new FeedServer and pass in the properties file path and the service to bind.
        var feedServer = new FeedServer("src/main/resources/feed.properties",
                new FeedServiceImpl());

        // Run the server.
        feedServer.run();
    }
}
