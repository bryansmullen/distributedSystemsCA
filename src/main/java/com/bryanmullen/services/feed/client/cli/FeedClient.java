package com.bryanmullen.services.feed.client.cli;

import com.bryanmullen.feedService.CurrentWaterRequest;
import com.bryanmullen.feedService.FeedConsumptionRequest;
import com.bryanmullen.feedService.FeedServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FeedClient extends ClientBase {
    Logger logger = LoggerFactory.getLogger(FeedClient.class); //
    // Logger for this class so we can log messages to the console.


    public FeedClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    public void addToFeedAvailable() {
        System.out.println("Starting to do Add To Feed Available method...");
    }

    public void currentWaterAvailable() {
        // log the start of the call
        logger.info("Starting to do Current Water Available method...");

        // create the client stub for the service
        var stub = FeedServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        stub.currentWaterAvailable(CurrentWaterRequest
                        .newBuilder()
                        .setCheckedBy("Bryan")
                        .build())
                .forEachRemaining(currentWaterResponse -> logger.info("Current Water Available Response received from" +
                        " " +
                        "server: " + currentWaterResponse));

        // close the channel
        super.closeChannel();

    }

    public void feedConsumption() {
        // log the start of the call
        logger.info("Starting to do Milk Production method...");

        // create the client stub for the service
        var stub = FeedServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.feedConsumption(FeedConsumptionRequest
                        .newBuilder()
                        .setStartDate(Timestamp.newBuilder().setNanos(123456789).build())
                        .setEndDate(Timestamp.newBuilder().setNanos(234567891).build())
                        .setCheckedBy("Bryan")
                        .build());

        // print the response to the console
        System.out.println("Feed Consumption Response: " + response);

        // close the channel
        super.closeChannel();

    }


}
