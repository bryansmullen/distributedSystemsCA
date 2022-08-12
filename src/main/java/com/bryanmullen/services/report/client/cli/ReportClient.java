package com.bryanmullen.services.report.client.cli;

import com.bryanmullen.reportService.CowReportRequest;
import com.bryanmullen.reportService.HerdReportRequest;
import com.bryanmullen.reportService.HerdReportResponse;
import com.bryanmullen.reportService.ReportServiceGrpc;
import com.bryanmullen.services.shared.ClientBase;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ReportClient extends ClientBase {
    Logger logger = LoggerFactory.getLogger(ReportClient.class); //
    // Logger for this class so we can log messages to the console.


    public ReportClient(String propertiesFilePath) throws IOException {
        super(propertiesFilePath);
    }

    /**
     * This method should return a report about a particular cow, including information about the amount of milk that
     * particular cow has produced, it’s current known weight, id etc. This method will be implemented using Client
     * Streaming.
     */
    public void cowReport() {
        // log the start of the call
        logger.info("Starting to do Cow Report method...");


        // create the client stub for the service
        var stub = ReportServiceGrpc.newBlockingStub(getChannel());

        // get the response from the server by calling the service with a new request
        var response =
                stub.withInterceptors(new ClientInterceptor())
                        .cowReport(CowReportRequest
                        .newBuilder()
                        .setCowId(1)
                        .setCheckedBy("Bryan")
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
    public void herdReport() throws InterruptedException {
        // log the start of the call
        logger.info("Starting to do Herd Report method...");

        var stub = ReportServiceGrpc.newStub(getChannel());


        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<HerdReportRequest> streamObserver = stub.herdReport(new StreamObserver<>() {
            // client side - streamObserver is the client side of the stream
            @Override
            public void onNext(HerdReportResponse response) {
                logger.info("Herd Report Response: " + response);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("Error: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
                latch.countDown();
            }
        });

        // server side - send the request
        for (int i = 0; i < 10; i++) {
            streamObserver.onNext(HerdReportRequest
                    .newBuilder()
                    .setCowId(i)
                    .setCheckedBy("Bryan")
                    .build());
            Thread.sleep(1000);
        }

        streamObserver.onCompleted();
        //noinspection ResultOfMethodCallIgnored
        latch.await(5, TimeUnit.SECONDS);

    }

    static class ClientInterceptor implements io.grpc.ClientInterceptor {
        Logger logger = LoggerFactory.getLogger(ClientInterceptor.class); //

        @Override
        public <ReqT, RespT> io.grpc.ClientCall<ReqT, RespT> interceptCall(io.grpc.MethodDescriptor<ReqT, RespT> method, io.grpc.CallOptions callOptions, io.grpc.Channel next) {
            return new ForwardingClientCall.SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
                @Override
                public void start(Listener<RespT> responseListener, Metadata headers) {
                    logger.info("Adding Metadata");
                    headers.put(Metadata.Key.of("HOSTNAME", Metadata.ASCII_STRING_MARSHALLER), "BRYANS-PC");
                    super.start(responseListener, headers);
                }
            };
        }
    }
}
