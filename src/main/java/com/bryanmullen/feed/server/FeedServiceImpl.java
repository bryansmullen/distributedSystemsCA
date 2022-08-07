package com.bryanmullen.feed.server;

import com.bryanmullen.feedService.FeedRequest;
import com.bryanmullen.feedService.FeedResponse;
import com.bryanmullen.feedService.FeedServiceGrpc;
import io.grpc.stub.StreamObserver;


public class FeedServiceImpl extends FeedServiceGrpc.FeedServiceImplBase {
    @Override
    public void feedMethod(FeedRequest request,
                           StreamObserver<FeedResponse> responseStreamObserver) {
        System.out.println("receiving dummy request");
        FeedResponse reply =
                FeedResponse.newBuilder()
                        .setMessage("Hello " + request.getName())
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }
}
