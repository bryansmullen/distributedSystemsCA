package com.bryanmullen.services.report.server;

import com.bryanmullen.reportService.*;
import com.bryanmullen.services.milking.server.MilkingServiceImpl;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
    Logger logger = LoggerFactory.getLogger(MilkingServiceImpl.class); //
    Random random = new Random(); // Random number generator - the values we
    // use in this project will be randomly generated as we are not connected
    // to real life sensors. In reality these values would be taken from
    // sensors, and we would not require this random object.

    /**
     * This method should return a report about a particular cow, including information about the amount of milk that
     * particular cow has produced, it’s current known weight, id etc. This method will be implemented using Client
     * Streaming.
     *
     * @param request                - the request object
     * @param responseStreamObserver - the response object
     */
    @Override
    public void cowReport(CowReportRequest request, StreamObserver<CowReportResponse> responseStreamObserver) {
        // log the checker id that is sending the request
        logger.info("Cow Report Request received from checker " + request.getCheckedBy());

        double cowWeight = getCowWeight(request.getCowId()); // get the cow weight
        double milkVolume = getCowMilkVolume(request.getCowId()); // get the cow milk volume
        String cowName = getCowName(request.getCowId()); // get the cow name

        // create the response object and send it to the client
        responseStreamObserver.onNext(CowReportResponse.newBuilder()
                .setCowId(request.getCowId())
                .setWeight(cowWeight)
                .setCowName(cowName)
                .setMilkProducedThisMonth(milkVolume)
                .build());
        responseStreamObserver.onCompleted();
    }

    private double getCowWeight(int cowId) {
        logger.info("Getting cow weight for cow " + cowId + " from the database");
        return random.nextDouble() * 100;
    }

    private double getCowMilkVolume(int cowId) {
        logger.info("Getting cow milk volume for cow " + cowId + " from the database");
        return random.nextDouble() * 100;
    }

    private String getCowName(int cowId) {
        logger.info("Getting cow name for cow " + cowId + " from the database");
        return "Cow " + cowId;
    }

    /**
     * This method should return a report about the entire herd as a whole, calculating the average milk produced per
     * cow, the average feed consumed per cow, as well as average statistics such as weight etc. for a broad overview of
     * the herd.This method will be implemented using Client Streaming.
     *
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
