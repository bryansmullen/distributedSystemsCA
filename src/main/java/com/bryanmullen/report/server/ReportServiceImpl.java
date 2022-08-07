package com.bryanmullen.report.server;

import com.bryanmullen.reportService.*;
import io.grpc.stub.StreamObserver;

public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
    @Override
    public void cowReport(CowReportRequest request,
                          StreamObserver<CowReportResponse> responseStreamObserver){
        System.out.println("Receiving Cow Report Request");
        var reply =
                CowReportResponse.newBuilder()
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void herdReport(HerdReportRequest request,
                           StreamObserver<HerdReportResponse> responseStreamObserver){
        System.out.println("Receiving Herd Report Request");
        var reply =
                HerdReportResponse.newBuilder()
                        .build();
        responseStreamObserver.onNext(reply);
        responseStreamObserver.onCompleted();
    }
}
