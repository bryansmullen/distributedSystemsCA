package com.bryanmullen.report.client.cli;

import com.bryanmullen.reportService.ReportRequest;
import com.bryanmullen.reportService.ReportServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ReportClient {
    public static void main(String[] args) {

        // constants
        final String HOST = "127.0.0.1";
        final int PORT = 5053;

        // check that command line arguments are not empty
        if (args.length == 0) {
            System.out.println("One argument required: the method to call");
            return;
        }

        // create the channel
        ManagedChannel channel = ManagedChannelBuilder.
                forAddress(HOST, PORT)
                .usePlaintext()
                .build();

        // select which method to call
        switch (args[0]) {
            case "reportMethod" -> doReportMethod(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }

    private static void doReportMethod(ManagedChannel channel) {
        // inform the user of the start of the call
        System.out.println("Starting to do report method...");

        var stub = ReportServiceGrpc.newBlockingStub(channel);

        var request = ReportRequest.newBuilder()
                .setName("Testing from client")
                .build();

        var response = stub.reportMethod(request);

        // print the response
        System.out.println("Report response: " + response.getMessage());
    }
}
