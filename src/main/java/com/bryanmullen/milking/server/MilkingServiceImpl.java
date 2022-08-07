package com.bryanmullen.milking.server;

import com.bryanmullen.milkingService.DummyRequest;
import com.bryanmullen.milkingService.DummyResponse;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MilkingServiceImpl extends MilkingServiceGrpc.MilkingServiceImplBase {
    @Override
    public void dummyMethod(DummyRequest request,
                      StreamObserver<DummyResponse> responseStreamObserver) {
        System.out.println("receiving dummy request");
        DummyResponse reply =
                DummyResponse.newBuilder()
                        .setMessage("Hello " + request.getName())
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }
}
