package com.bryanmullen.services.feed.client.cli;

import com.bryanmullen.services.shared.ClientBase;

import java.io.IOException;

public class FeedClient extends ClientBase {

    public FeedClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    public void addToFeedAvailable() {
        System.out.println("Starting to do Add To Feed Available method...");
    }

    public void currentWaterAvailable() {
        System.out.println("Starting to do Current Water Available method...");

    }

    public void feedConsumption() {
        System.out.println("Starting to do Feed Consumption method...");

    }


}
