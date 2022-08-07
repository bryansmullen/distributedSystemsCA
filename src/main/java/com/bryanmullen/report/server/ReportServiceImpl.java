package com.bryanmullen.report.server;
import com.bryanmullen.reportService.*;
import io.grpc.stub.StreamObserver;

public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {

    /**
     * This method should return a report about a particular cow, including
     * information about the amount of milk that particular cow has produced,
     * it’s current known weight, id etc. This method will be implemented
     * using Client Streaming.
     * @param responseStreamObserver - Stream Observer of Request Objects
     * @return - The Response Object
     */
    @Override
    public StreamObserver<CowReportRequest> cowReport(StreamObserver<CowReportResponse> responseStreamObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(CowReportRequest value) {
                System.out.println("Data Received");
            }

            @Override
            public void onError(Throwable t) {
                // feed error back to response
                responseStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                // build a reply on each on next
                var reply = CowReportResponse.newBuilder().build();

                // feed reply to streamed response observer
                responseStreamObserver.onNext(reply);

                // response complete
                responseStreamObserver.onCompleted();
            }
        };
    }

    /**
     * This method should return a report about the entire herd as a whole,
     * calculating the average milk produced per cow, the average feed consumed
     * per cow, as well as average statistics such as weight etc for a broad
     * overview of the herd.This method will be implemented using Client
     * Streaming.
     * @param responseStreamObserver - Stream Observer of Request Objects
     * @return - The Response Object
     */
    @Override
    public StreamObserver<HerdReportRequest> herdReport(StreamObserver<HerdReportResponse> responseStreamObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(HerdReportRequest value) {
                System.out.println("Data Received");
            }

            @Override
            public void onError(Throwable t) {
                // feed error back to response
                responseStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                // build a reply on each on next
                var reply = HerdReportResponse.newBuilder().build();

                // feed reply to streamed response observer
                responseStreamObserver.onNext(reply);

                // response complete
                responseStreamObserver.onCompleted();
            }
        };
    }
}
