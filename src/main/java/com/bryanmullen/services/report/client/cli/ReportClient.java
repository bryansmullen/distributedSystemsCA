package com.bryanmullen.services.report.client.cli;
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
            case "cowReport" -> doCowReport(channel);
            case "herdReport" -> doHerdReport(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }



    private static void doCowReport(ManagedChannel channel) {
        System.out.println("Starting to do Cow Report method...");

    }

    private static void doHerdReport(ManagedChannel channel) {
        System.out.println("Starting to do Herd Report method...");

    }

}
