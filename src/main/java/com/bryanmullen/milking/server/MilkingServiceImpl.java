package com.bryanmullen.milking.server;
import com.bryanmullen.milkingService.*;
import io.grpc.stub.StreamObserver;

public class MilkingServiceImpl extends MilkingServiceGrpc.MilkingServiceImplBase {
    @Override
    public void milkCollection(MilkCollectionRequest request,
                               StreamObserver<MilkCollectionResponse> responseStreamObserver) {
        System.out.println("Receiving Milk Collection Request");
        var reply = MilkCollectionResponse.newBuilder()
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void milkProduction(MilkProductionRequest request,
                               StreamObserver<MilkProductionResponse> responseStreamObserver){
        System.out.println("Receiving Milk Production Request");
        var reply = MilkProductionResponse.newBuilder()
                .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void milkCurrentCow(MilkCurrentCowRequest request,
                               StreamObserver<MilkCurrentCowResponse> responseStreamObserver){
        System.out.println("Receiving Milk Current Cow Request");
        var reply = MilkCurrentCowResponse.newBuilder()
                .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

}
