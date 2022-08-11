package com.bryanmullen.services.feed.server;

import com.bryanmullen.feedService.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;


public class FeedServiceImpl extends FeedServiceGrpc.FeedServiceImplBase {
    Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class); //
    // Logger for this class so we can log messages to the console.
    final double FEED_TRAY_CAPACITY_IN_KG = 10; // The capacity of the feed tray.
    Random random = new Random(); // Random number generator - the values we
    // use in this project will be randomly generated as we are not connected
    // to real life sensors. In reality these values would be taken from
    // sensors, and we would not require this random object.

    /**
     * This method should allow the client to open a delivery chute for feed for the animals. Further to this it should
     * log the current weight before and after delivery in order to log the amount consumed.This method will be
     * implemented using Bi Directional Streaming.
     *
     * @param responseStreamObserver - Stream Observer of Request Objects
     * @return - Stream Observer of Response Objects
     */
    @Override
    public StreamObserver<AddToFeedRequest> addToFeedAvailable(StreamObserver<AddToFeedResponse> responseStreamObserver) {
        return new StreamObserver<>() {
            double totalFeedAdded = 0; // The total amount of feed added to the
            final double currentFeedTrayWeight = getFeedTrayWeight();

            @Override
            public void onNext(AddToFeedRequest request) {
                // log the checker id that is sending the request
                logger.info("Add To Feed Request received from checker " + request.getAddedBy());

                // check that the weight of the feed tray is not full - if it is full then we should not add any more feed
                if (currentFeedTrayWeight + totalFeedAdded + request.getFeedMassToAdd() > FEED_TRAY_CAPACITY_IN_KG ) {
                    // in the case that the feed tray is full, we should not add any more feed, however we should
                    // still send a response to the client to let them know that the feed tray is full as well as the
                    // amount of feed that was added.
                    responseStreamObserver.onNext(AddToFeedResponse.newBuilder()
                            .setFeedMassAdded(totalFeedAdded)
                            .setCurrentFeedMass(currentFeedTrayWeight + totalFeedAdded)
                            .setMessage("Feed Tray is full")
                            .build());
                } else {
                    // in the case that the feed tray is not full, we should add the feed to the feed tray and send a
                    // response to the client to let them know that the feed tray is not full as well as the amount
                    // of feed that was added.  We should also update the total feed added.
                    addToFeed(request.getFeedMassToAdd());
                    totalFeedAdded += request.getFeedMassToAdd();
                    responseStreamObserver.onNext(AddToFeedResponse.newBuilder()
                            .setFeedMassAdded(totalFeedAdded)
                            .setCurrentFeedMass(currentFeedTrayWeight + totalFeedAdded)
                            .setMessage("Feed Added")
                            .build());
                }

            }

            private void addToFeed(double feedMassToAdd) {
                // add the feed to the feed tray - in reality this would be a controller for a physical feed tray and
                // this would be a call to the physical feed tray to add the feed.
                logger.info( feedMassToAdd + "kg of Feed Added to Feed Tray");
            }

            private double getFeedTrayWeight() {
                return random.nextDouble(FEED_TRAY_CAPACITY_IN_KG);
            }

            @Override
            public void onError(Throwable t) {
                // feed error back to response
                responseStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                // response complete
                logger.info(totalFeedAdded + " kg of feed added to the feed tray");
                responseStreamObserver.onCompleted();
                System.out.println("Response completed");
            }
        };
    }

    /**
     * This method should simply return the current water available by means of a weight/volume sensor, for ensuring
     * there is adequate water available .This method will be implemented using Server Streaming.
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
     * This method should return a report outlining the feed consumed in a particular time period. This could be used
     * either for predicting supply levels or to monitor animal health.This method will be implemented using Unary
     * Method.
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