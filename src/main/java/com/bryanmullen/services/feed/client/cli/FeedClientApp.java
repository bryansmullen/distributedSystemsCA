package com.bryanmullen.services.feed.client.cli;

import java.io.IOException;

public class FeedClientApp {
    public static void main(String[] args) throws IOException, InterruptedException {
        var feedClient = new FeedClient("src/main/resources/feed.properties");

        feedClient.getService();
        feedClient.addToFeedAvailable();
        feedClient.currentWaterAvailable();
        feedClient.feedConsumption();
    }
}
