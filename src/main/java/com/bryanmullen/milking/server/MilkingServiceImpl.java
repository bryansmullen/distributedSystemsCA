package com.bryanmullen.milking.server;

import com.bryanmullen.milkingService.*;
import io.grpc.stub.StreamObserver;

public class MilkingServiceImpl extends MilkingServiceGrpc.MilkingServiceImplBase {
    /**
     * This method should be able to represent the total milk currently
     * collected in the collection unit relative to the capacity, to give an
     * indication of when the unit is full.This method will be implemented
     * using Server Streaming.
     *
     * @param request - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkCollection(MilkCollectionRequest request,
                               StreamObserver<MilkCollectionResponse> responseStreamObserver) {
        // Log that method has been called
        System.out.println("Receiving Milk Collection Request");

        // Build a reply
        var reply = MilkCollectionResponse.newBuilder()
                .build();

        // Feed multiple replies to the onNext function
        for (int i = 0; i < 10; i++) {
            responseStreamObserver.onNext(reply);
        }

        // Response complete
        responseStreamObserver.onCompleted();
    }

    /**
     * This method should be able to log the milk produced by the current
     * milking session against a particular cow, based on the ID of the current
     * cow. It should calculate the volume of milk by comparing the current
     * total collection against the last logged value and calculating the
     * difference.This method will be implemented using Unary Method.
     *
     * @param request - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkProduction(MilkProductionRequest request,
                               StreamObserver<MilkProductionResponse> responseStreamObserver) {
        // Log that method has been called
        System.out.println("Receiving Milk Production Request");

        // Build a reply
        var reply = MilkProductionResponse.newBuilder()
                .build();

        // Feed a single reply to the onNext function
        responseStreamObserver.onNext(reply);

        // Response complete
        responseStreamObserver.onCompleted();
    }

    /**
     * This method should be capable of returning the ID of the current cow
     * being milked. This method will be implemented using Server Streaming.
     *
     * @param request - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void milkCurrentCow(MilkCurrentCowRequest request,
                               StreamObserver<MilkCurrentCowResponse> responseStreamObserver) {
        // Log that method has been called
        System.out.println("Receiving Milk Current Cow Request");

        // Build a reply
        var reply = MilkCurrentCowResponse.newBuilder()
                .build();

        // Feed multiple replies to the onNext function
        for (int i = 0; i < 10; i++) {
            responseStreamObserver.onNext(reply);
        }

        // Response complete
        responseStreamObserver.onCompleted();
    }

}
