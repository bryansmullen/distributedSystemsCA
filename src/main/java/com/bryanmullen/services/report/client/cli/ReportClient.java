package com.bryanmullen.services.report.client.cli;

import com.bryanmullen.interceptors.ClientInterceptor;
import com.bryanmullen.reportService.CowReportRequest;
import com.bryanmullen.reportService.HerdReportRequest;
import com.bryanmullen.reportService.HerdReportResponse;
import com.bryanmullen.reportService.ReportServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * ReportClient - A CLI client for the report service.
 */
public class ReportClient extends ClientBase {
    // Logger for this class so we can log messages to the console.
    Logger logger = LoggerFactory.getLogger(ReportClient.class);

    // constructor
    public ReportClient(String propertiesFilePath) throws IOException {
        // call the superclass constructor with the properties file path
        super(propertiesFilePath);
    }

    /**
     * This method should return a report about a particular cow, including information about the amount of milk that
     * particular cow has produced, it’s current known weight, id etc. This method will be implemented using Client
     * Streaming.
     */
    public void cowReport(int cowId, String checkedBy) {
        // log the start of the call
        logger.info("Starting to do Cow Report method...");


        // create the client stub for the service
        var stub = ReportServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.withInterceptors(new ClientInterceptor())
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .cowReport(CowReportRequest
                                .newBuilder()
                                .setCowId(cowId)
                                .setCheckedBy(checkedBy)
                                .build());

        // print the response to the console
        System.out.println("Cow Report Response: " + response);

        // close the channel
        super.closeChannel();

    }

    /**
     * This method should return a report about the entire herd as a whole, calculating the average milk produced per
     * cow, the average feed consumed per cow, as well as average statistics such as weight etc. for a broad overview of
     * the herd.This method will be implemented using Client Streaming.
     */
    public void herdReport(int cowId, String checkedBy) throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Herd Report method...");

        // create the client stub for the service
        var stub = ReportServiceGrpc.newStub(getChannel());

        // initialize a latch to wait for the response from the server
        CountDownLatch latch = new CountDownLatch(1);

        // asynchronous call to the server
        StreamObserver<HerdReportRequest> streamObserver =
                stub
                        .withInterceptors(new ClientInterceptor()) // add the interceptor to the stub to log the
                        // hostname and timestamp
                        .withDeadlineAfter(10, TimeUnit.SECONDS) // set a 10-second deadline - if the server doesn't respond
                        .herdReport(new StreamObserver<>() {
                            // onNext - called when the server sends a response
                            @Override
                            public void onNext(HerdReportResponse response) {
                                // log the response to the console
                                logger.info("Herd Report Response: " + response);
                            }

                            // onError - called when the server sends an error
                            @Override
                            public void onError(Throwable t) {
                                // error handling - log the error to the console
                                System.out.println("Error: " + t.getMessage());
                                latch.countDown();
                            }

                            // onCompleted - called when the server sends a completion message
                            @Override
                            public void onCompleted() {
                                // decrement the latch
                                latch.countDown();
                            }
                        });

        // create multiple requests to the server
        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(HerdReportRequest
                    .newBuilder()
                    .setCowId(cowId + i)
                    .setCheckedBy(checkedBy)
                    .build());

            // wait for 1000 milliseconds before sending the next request
            Thread.sleep(1000);
        }

        // send a completion message to the server
        streamObserver.onCompleted();

        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);

    }

}
