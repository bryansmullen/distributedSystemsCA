package com.bryanmullen.feed.server;

import com.bryanmullen.feedService.*;
import io.grpc.stub.StreamObserver;


public class FeedServiceImpl extends FeedServiceGrpc.FeedServiceImplBase {
    @Override
    public void addToFeedAvailable(AddToFeedRequest request,
                                   StreamObserver<AddToFeedResponse> responseStreamObserver){
        System.out.println("Receiving Add To Feed Available Request");
        var reply = AddToFeedResponse.newBuilder()
                .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void currentWaterAvailable(CurrentWaterRequest request,
                                      StreamObserver<CurrentWaterResponse> responseStreamObserver){
        System.out.println("Receiving Current Water Available Request");
        var reply = CurrentWaterResponse.newBuilder()
                .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void feedConsumption(FeedConsumptionRequest request,
                                   StreamObserver<FeedConsumptionResponse> responseStreamObserver){
        System.out.println("Receiving Feed Consumption Request");
        var reply = FeedConsumptionResponse.newBuilder()
                .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }
}