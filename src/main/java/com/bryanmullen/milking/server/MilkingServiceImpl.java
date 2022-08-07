package com.bryanmullen.milking.server;
import com.bryanmullen.milkingService.MilkingRequest;
import com.bryanmullen.milkingService.MilkingResponse;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import io.grpc.stub.StreamObserver;

public class MilkingServiceImpl extends MilkingServiceGrpc.MilkingServiceImplBase {
    @Override
    public void milkingMethod(MilkingRequest request,
                              StreamObserver<MilkingResponse> responseStreamObserver) {
        System.out.println("receiving dummy request");
        MilkingResponse reply =
                MilkingResponse.newBuilder()
                        .setMessage("Hello " + request.getName())
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }
}
