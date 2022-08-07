package com.bryanmullen.report.server;

import com.bryanmullen.reportService.ReportRequest;
import com.bryanmullen.reportService.ReportResponse;
import com.bryanmullen.reportService.ReportServiceGrpc;
import io.grpc.stub.StreamObserver;

public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
    @Override
    public void reportMethod(ReportRequest request,
                             StreamObserver<ReportResponse> responseStreamObserver){
            System.out.println("receiving dummy request");
            ReportResponse reply =
                    ReportResponse.newBuilder()
                            .setMessage("Hello " + request.getName())
                            .build();
            responseStreamObserver.onNext(reply);
            responseStreamObserver.onCompleted();
    }
}
