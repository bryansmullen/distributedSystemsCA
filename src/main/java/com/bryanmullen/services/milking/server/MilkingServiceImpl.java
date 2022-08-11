package com.bryanmullen.services.milking.server;

import com.bryanmullen.milkingService.*;
import com.google.protobuf.Duration;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MilkingServiceImpl extends MilkingServiceGrpc.MilkingServiceImplBase {
    Logger logger = LoggerFactory.getLogger(MilkingServiceImpl.class); //
    // Logger for this class so we can log messages to the console.
    final double COLLECTION_CAPACITY = 10; // The capacity of the milking machine.
    Random random = new Random(); // Random number generator - the values we
    // use in this project will be randomly generated as we are not connected
    // to real life sensors. In reality these values would be taken from
    // sensors, and we would not require this random object.


    /**
     * This method should be able to represent the total milk currently
     * collected in the collection unit relative to the capacity, to give an
     * indication of when the unit is full.This method will be implemented
     * using Server Streaming.
     *
     * @param request                - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkCollection(MilkCollectionRequest request,
                               StreamObserver<MilkCollectionResponse> responseStreamObserver) {

        // log the checker id that is sending the request
        logger.info("Milk Collection Request received from checker " + request.getCheckedBy());


        // how close together the responses will be sent. Responses will
        // continue to be sent until the milk collection is full.
        int TIME_BETWEEN_RESPONSES_IN_MILLIS = 1000;

        // Variables to keep track of the milk collection and to exit the loop
        boolean isFull = false;
        double currentVolume;

        while (!isFull) { // while the collection is not full
            currentVolume = getCurrentMilkVolume(); // get the current volume
            isFull = currentVolume / COLLECTION_CAPACITY >= 0.9; // check if
            // the collection is full - we assume 90% is considered "full" to
            // allow for some error in the sensor readings.

            // create the response object and send it to the client
            responseStreamObserver.onNext(MilkCollectionResponse.newBuilder()
                    .setCapacityInLitres(COLLECTION_CAPACITY)
                    .setMilkVolumeInLitres(currentVolume)
                    .setIsFull(isFull)
                    .build());

            // wait for a second before sending the next response message to
            // the client - this is to simulate the time between responses.
            try {
                Thread.sleep(TIME_BETWEEN_RESPONSES_IN_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        // Response complete
        responseStreamObserver.onCompleted();
    }

    /**
     * This method should be able to represent the total milk currently
     * collected in the collection unit - this method will be implemented
     * using a sensor. Here we will use a random number generator to simulate
     * the sensor readings.
     *
     * @return the current volume of the collection unit
     */
    private double getCurrentMilkVolume() {
        return random.nextDouble(COLLECTION_CAPACITY);// This value would in
        // reality be the current volume of the collection unit which would
        // be pulled from a sensor or other device.
    }

    /**
     * This method should be able to log the milk produced by the current
     * milking session against a particular cow, based on the ID of the current
     * cow. It should calculate the volume of milk by comparing the current
     * total collection against the last logged value and calculating the
     * difference.This method will be implemented using Unary Method.
     *
     * @param request                - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkProduction(MilkProductionRequest request,
                               StreamObserver<MilkProductionResponse> responseStreamObserver) {
        // log the checker id that is sending the request
        logger.info("Milk Collection Request received from checker " + request.getCheckedBy());

        var currentVolume = getCurrentMilkVolume(); // get the current volume
        var lastLoggedVolume = getLastLoggedVolume(); // get the last

        // TODO: connect to a database and persist the volume of milk produced

        // Build a reply
        var reply = MilkProductionResponse.newBuilder()
                .setCurrentCowId(getCurrentCowId())
                .setMilkVolumeLogged(currentVolume - lastLoggedVolume)
                .build();

        // Feed a single reply to the onNext function
        responseStreamObserver.onNext(reply);

        // Response complete
        responseStreamObserver.onCompleted();
    }

    /**
     * This method should return the last logged volume of milk in the database.
     * Since we are not connected to a database, we will return the current
     * volume - 10. This would in reality be the last logged volume of milk in
     * the database.
     */
    private double getLastLoggedVolume() {
        return getCurrentMilkVolume() - 10;
    }
    /**
     * This method should be capable of returning the ID of the current cow
     * being milked. This method will be implemented using Server Streaming.
     *
     * @param request                - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkCurrentCow(MilkCurrentCowRequest request,
                               StreamObserver<MilkCurrentCowResponse> responseStreamObserver) {

        // log the checker id that is sending the request
        logger.info("Milk Collection Request received from checker " + request.getCheckedBy());

        // find which cow is currently being milked - this would be done by
        // a sensor or other device. For simplicity, we will just use a random
        // number.
        var currentCowBeingMilkedId = getCurrentCowId();

        // log the current start time of the milking session. We would likely
        // persist this value in a database since this should not change
        // between requests. However since we do not have a database we will
        // just use the current time.
        Timestamp startTime =
                Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond())
                        .setNanos(Instant.now().getNano()).build();

        // variable to hold the current time of the milking session
        Timestamp currentTime;

        // duration between the responses to be sent to the client.
        final int TIME_BETWEEN_RESPONSES_IN_MILLIS = 1000;

        // We will send a total of 6 responses to the client, at a rate of 1
        // response per second. This could be edited to allow for a different
        // rate of responses.
        for (int i = 0; i < 6; i++) {

            // Update the current time of the milking session
            currentTime =
                    Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond())
                            .setNanos(Instant.now().getNano()).build();

            // create the response object to be sent to the client
            var res =
                    MilkCurrentCowResponse.newBuilder()
                            .setCurrentCowId(currentCowBeingMilkedId)
                            .setStartTime(startTime)
                            .setDuration(Duration.newBuilder()
                                    .setSeconds((int) (currentTime.getSeconds() - startTime.getSeconds()))
                                    .setNanos(currentTime.getNanos() - startTime.getNanos())
                                    .build())
                            .build();

            // send the response to the client
            responseStreamObserver.onNext(res);

            // wait for a second before sending the next response message to
            try {
                Thread.sleep(TIME_BETWEEN_RESPONSES_IN_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Response complete
        responseStreamObserver.onCompleted();
    }

    /**
     * This method generated a cow ID of the cow currently being milked. This
     * would in reality be implemented by a sensor or other device. Here we
     * just use a random number generated from a predefined array.
     *
     * @return - the ID of the cow currently being milked
     */
    private int getCurrentCowId() {
        int[] cowIds = {1, 2, 3};
        return cowIds[random.nextInt(cowIds.length)];
    }

}
