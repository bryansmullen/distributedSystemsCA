package com.bryanmullen.services.feed.client.cli;

import com.bryanmullen.feedService.*;
import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.services.shared.ClientBase;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * FeedClient - A CLI client for the feed service.
 */
public class FeedClient extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(FeedClient.class);

    // constructor
    public FeedClient(String propertiesFilePath) throws IOException {
        // call the superclass constructor with the properties file path
        super(propertiesFilePath);
    }

    /**
     * This method should allow the client to open a delivery chute for feed for the animals. Further to this it should
     * log the current weight before and after delivery in order to log the amount consumed.This method will be
     * implemented using Bi Directional Streaming.
     */
    public void addToFeedAvailable() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Add To Feed Available method...");

        // Create a new FeedService.FeedServiceBlockingStub object using the channel created in the super class.
        var stub = FeedServiceGrpc.newStub(getChannel());

        // create a new latch to wait for the response from the server.
        CountDownLatch latch = new CountDownLatch(1);

        // asynchronously call the addToFeedAvailable method on the server.
        var streamObserver = stub
                .withInterceptors(new ClientInterceptor()) // Add the interceptor to the client. This will allow us
                // to log the hostname and timestamp
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                .addToFeedAvailable(new StreamObserver<>() {
                    // onNext is called when the server sends a response.
                    @Override
                    public void onNext(AddToFeedResponse response) {
                        // log the response
                        logger.info("Received response: " + response);
                    }

                    // onError is called if the server returns an error.
                    @Override
                    public void onError(Throwable t) {
                        // error handling - log the error and set the text area to display the error.
                        logger.info("Error: " + t.getMessage());
                    }

                    // onCompleted is called when the server returns a response.
                    @Override
                    public void onCompleted() {
                        // decrement the latch so the program can continue.
                        latch.countDown();
                    }
                });

        // create multiple requests by creating a list of data to send and then sending each one individually.
        Arrays.asList(
                new feedMassToAdd(1, "Adam"),
                new feedMassToAdd(2, "Eve"),
                new feedMassToAdd(3, "Jack")
        ).forEach(requestData -> {
                    streamObserver.onNext(
                            AddToFeedRequest.newBuilder()
                                    .setFeedMassToAdd(requestData.feedMassToAdd)
                                    .setAddedBy(requestData.addedBy)
                                    .build());

                    // wait for 3000 milliseconds before sending the next request.
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        // send the final message to the server.
        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);
    }

    /**
     * private record to allow us to pass data into the request array
     * @param feedMassToAdd
     * @param addedBy
     */
    private record feedMassToAdd(int feedMassToAdd, String addedBy) {
    }

    /**
     * This method should simply return the current water available by means of a weight/volume sensor, for ensuring
     * there is adequate water available .This method will be implemented using Server Streaming.
     */
    public void currentWaterAvailable() {
        // log the start of the call
        logger.info("Starting to do Current Water Available method...");

        // create the client stub for the service
        var stub = FeedServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        stub.withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                .currentWaterAvailable(CurrentWaterRequest
                        .newBuilder()
                        .setCheckedBy("Bryan")
                        .build())
                .forEachRemaining(currentWaterResponse -> logger.info("Current Water Available Response received from" +
                        " " +
                        "server: " + currentWaterResponse));

        // close the channel
        super.closeChannel();

    }

    /**
     * This method should return a report outlining the feed consumed in a particular time period. This could be used
     * either for predicting supply levels or to monitor animal health.This method will be implemented using Unary
     * Method.
     */
    public void feedConsumption() {
        // log the start of the call
        logger.info("Starting to do Milk Production method...");

        // create the client stub for the service
        var stub = FeedServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.withInterceptors(new ClientInterceptor())
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .feedConsumption(FeedConsumptionRequest
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
