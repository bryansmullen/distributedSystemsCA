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

public class FeedClient extends ClientBase {
    Logger logger = LoggerFactory.getLogger(FeedClient.class); //
    // Logger for this class so we can log messages to the console.


    public FeedClient(String propertiesFilePath) throws IOException {
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

        var stub = FeedServiceGrpc.newStub(getChannel());

        CountDownLatch latch = new CountDownLatch(1);

        var streamObserver = stub.withInterceptors(new ClientInterceptor()).addToFeedAvailable(new StreamObserver<>() {
            @Override
            public void onNext(AddToFeedResponse response) {
                logger.info("Received response: " + response);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });
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
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);
    }

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
        stub.withInterceptors(new ClientInterceptor()).currentWaterAvailable(CurrentWaterRequest
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
