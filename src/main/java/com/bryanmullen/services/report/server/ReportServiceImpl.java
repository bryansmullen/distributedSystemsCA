package com.bryanmullen.services.report.server;

import com.bryanmullen.reportService.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementation of the ReportService. This is the server side implementation of the service. It is responsible for
 * handling the requests from the client. It will be mounted on the server.
 */
public class ReportServiceImpl extends ReportServiceGrpc.ReportServiceImplBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    Random random = new Random(); // Random number generator - the values we
    // use in this project will be randomly generated as we are not connected
    // to real life sensors. In reality these values would be taken from
    // sensors, and we would not require this random object.

    /**
     * This method should return a report about a particular cow, including information about the amount of milk that
     * particular cow has produced, it’s current known weight, id etc. This method will be implemented using Unary
     * Method
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

    /**
     * Helper method to get the cow weight. This method will be implemented using the Random object for the sake of the
     * example.
     *
     * @param cowId The cow id.
     * @return The cow weight.
     */
    private double getCowWeight(int cowId) {
        logger.info("Getting cow weight for cow " + cowId + " from the database");
        return random.nextDouble() * 100;
    }

    /**
     * Helper method to get the cow milk volume. This method will be implemented using the Random object for the sake of the
     * example.
     *
     * @param cowId The cow id.
     * @return The cow milk volume.
     */
    private double getCowMilkVolume(int cowId) {
        logger.info("Getting cow milk volume for cow " + cowId + " from the database");
        return random.nextDouble() * 100;
    }

    /**
     * Helper method to get the cow name. This method will be implemented using the Random object for the sake of the
     * example.
     *
     * @param cowId The cow id.
     * @return The cow name.
     */
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
        List<CowReportResponse> herdReport = new ArrayList<>();

        return new StreamObserver<>() {
            @Override
            public void onNext(HerdReportRequest request) {
                // log the checker id that is sending the request
                logger.info("Herd Report Request received from checker " + request.getCheckedBy());

                double cowWeight = getCowWeight(request.getCowId()); // get the cow weight
                double milkVolume = getCowMilkVolume(request.getCowId()); // get the cow milk volume
                String cowName = getCowName(request.getCowId()); // get the cow name

                // create the response object and send it to the client
                CowReportResponse cowReportResponse = CowReportResponse.newBuilder()
                        .setCowId(request.getCowId())
                        .setWeight(cowWeight)
                        .setCowName(cowName)
                        .setMilkProducedThisMonth(milkVolume)
                        .build();

                herdReport.add(cowReportResponse);
            }

            @Override
            public void onError(Throwable t) {
                // feed error back to response
                responseStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                // build a reply on each on next
                var response = HerdReportResponse.newBuilder();

                double totalCowMilkVolume = 0;
                double totalCowWeight = 0;


                for (CowReportResponse cowReportResponse : herdReport
                ) {
                    response.addCows(cowReportResponse);
                    totalCowMilkVolume += cowReportResponse.getMilkProducedThisMonth();
                    totalCowWeight += cowReportResponse.getWeight();
                }

                // set the average milk volume and weight
                response.setAverageWeight(totalCowWeight / herdReport.size());
                response.setAverageMilkProducedThisMonth(totalCowMilkVolume / herdReport.size());

                // on each next, send the response to the client
                responseStreamObserver.onNext(response.build());

                // response complete
                responseStreamObserver.onCompleted();
            }
        };
    }
}
