package com.bryanmullen.services.milking.client.cli;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.milkingService.MilkCollectionRequest;
import com.bryanmullen.milkingService.MilkCurrentCowRequest;
import com.bryanmullen.milkingService.MilkProductionRequest;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * MilkingClient - A CLI client for the milking service.
 */
public class MilkingClient extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(MilkingClient.class);

    // constructor
    public MilkingClient(String propertiesFilePath) throws IOException {
        // call the superclass constructor with the properties file path
        super(propertiesFilePath);
    }

    /**
     * This method should be able to represent the total milk currently collected in the collection unit relative to the
     * capacity, to give an indication of when the unit is full.This method will be implemented using Server Streaming.
     */
    public void milkCollection() {
        // log the start of the call
        logger.info("Starting to do Milk Collection method...");

        // create the client stub for the service
        var stub = MilkingServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        stub.withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                // within 5 seconds, the call will fail
                .milkCollection(MilkCollectionRequest
                        .newBuilder()
                        .setCheckedBy("Bryan")
                        .build())
                .forEachRemaining(milkCollectionResponse -> logger.info("Milk Collection Response received from server: " + milkCollectionResponse));

        // close the channel
        super.closeChannel();
    }

    /**
     * This method should be able to log the milk produced by the current milking session against a particular cow,
     * based on the ID of the current cow. It should calculate the volume of milk by comparing the current total
     * collection against the last logged value and calculating the difference.This method will be implemented using
     * Unary Method.
     */
    public void milkProduction() {
        // log the start of the call
        logger.info("Starting to do Milk Production method...");

        // create the client stub for the service
        var stub = MilkingServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.withInterceptors(new ClientInterceptor())
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .milkProduction(MilkProductionRequest.newBuilder().setCheckedBy("Bryan").build());

        // print the response to the console
        System.out.println("Milk Production Response: " + response);

        // close the channel
        super.closeChannel();

    }

    /**
     * This method should be capable of returning the ID of the current cow being milked. This method will be
     * implemented using Server Streaming.
     */
    public void milkCurrentCow() {
        // log the start of the call
        logger.info("Starting to do Milk Current Cow method...");

        // create the client stub for the service
        var stub = MilkingServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        stub.withInterceptors(new ClientInterceptor())
                .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                .milkCurrentCow(MilkCurrentCowRequest
                        .newBuilder()
                        .setCheckedBy("Bryan")
                        .build())
                .forEachRemaining(milkCurrentCowResponse -> logger.info("Milk Current Cow Response received from " +
                        "server: " + milkCurrentCowResponse));

        // close the channel
        super.closeChannel();
    }
}
