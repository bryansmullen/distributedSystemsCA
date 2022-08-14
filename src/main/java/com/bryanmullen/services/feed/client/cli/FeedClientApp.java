package com.bryanmullen.services.feed.client.cli;

import java.io.IOException;

/**
 * FeedClientApp - This class is the main class for the feed client application.
 */
public class FeedClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        // create a new feed client object and pass in the properties file path
        var feedClient = new FeedClient("src/main/resources/feed.properties");

        // get the service info from the service discovery
        feedClient.getService();

        // call the remote procedures calls
        feedClient.addToFeedAvailable();
        feedClient.currentWaterAvailable();
        feedClient.feedConsumption();
    }
}
