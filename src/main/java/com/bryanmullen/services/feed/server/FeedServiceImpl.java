package com.bryanmullen.services.feed.server;

import com.bryanmullen.feedService.*;
import io.grpc.stub.StreamObserver;


public class FeedServiceImpl extends FeedServiceGrpc.FeedServiceImplBase {
    /**
     * This method should allow the client to open a delivery chute for feed
     * for the animals. Further to this it should log the current weight before
     * and after delivery in order to log the amount consumed.This method will
     * be implemented using Bi Directional Streaming.
     *
     * @param responseStreamObserver - Stream Observer of Request Objects
     * @return - Stream Observer of Response Objects
     */
    @Override
    public StreamObserver<AddToFeedRequest> addToFeedAvailable(StreamObserver<AddToFeedResponse> responseStreamObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(AddToFeedRequest value) {
                System.out.println("Receiving Add To Feed Available Request " +
                        "Request");

                // build a reply on each on next
                var reply = AddToFeedResponse.newBuilder().build();

                // feed reply to streamed response observer
                responseStreamObserver.onNext(reply);
                System.out.println("Response Sent");

            }

            @Override
            public void onError(Throwable t) {
                // feed error back to response
                responseStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                // response complete
                responseStreamObserver.onCompleted();
                System.out.println("Response completed");
            }
        };
    }

    /**
     * This method should simply return the current water available by means of
     * a weight/volume sensor, for ensuring there is adequate water available
     * .This method will be implemented using Server Streaming.
     *
     * @param request                - The Request Object
     * @param responseStreamObserver - The Response Object
     */
    @Override
    public void currentWaterAvailable(CurrentWaterRequest request,
                                      StreamObserver<CurrentWaterResponse> responseStreamObserver) {
        // Log that method has been called
        System.out.println("Receiving Current Water Available Request");

        // Build a reply
        var reply = CurrentWaterResponse.newBuilder()
                .build();

        // Feed multiple replies to the onNext function
        for (int i = 0; i < 10; i++) {
            responseStreamObserver.onNext(reply);
        }

        // Response complete
        responseStreamObserver.onCompleted();
        System.out.println("Response completed");

    }

    /**
     * This method should return a report outlining the feed consumed in a
     * particular time period. This could be used either for predicting supply
     * levels or to monitor animal health.This method will be implemented using
     * Unary Method.
     *
     * @param request                - The Request Object
     * @param responseStreamObserver - The Response Object
     */
    @Override
    public void feedConsumption(FeedConsumptionRequest request,
                                StreamObserver<FeedConsumptionResponse> responseStreamObserver) {
        // Log that method has been called
        System.out.println("Receiving Feed Consumption Request");

        // Build a reply
        var reply = FeedConsumptionResponse.newBuilder()
                .build();

        // Feed a single reply to the onNext function
        responseStreamObserver.onNext(reply);

        // Response complete
        responseStreamObserver.onCompleted();
        System.out.println("Response completed");

    }
}