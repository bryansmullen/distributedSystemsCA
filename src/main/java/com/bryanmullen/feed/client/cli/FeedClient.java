package com.bryanmullen.feed.client.cli;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class FeedClient {
    public static void main(String[] args) {
        // constants
        final String HOST = "127.0.0.1";
        final int PORT = 5052;

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
            case "addToFeedAvailable" -> doAddToFeedAvailable(channel);
            case "currentWaterAvailable" -> doCurrentWaterAvailable(channel);
            case "feedConsumption" -> doFeedConsumption(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }

    private static void doAddToFeedAvailable(ManagedChannel channel) {
            System.out.println("Starting to do Add To Feed Available method...");
    }

    private static void doCurrentWaterAvailable(ManagedChannel channel) {
        System.out.println("Starting to do Current Water Available method...");

    }

    private static void doFeedConsumption(ManagedChannel channel) {
        System.out.println("Starting to do Feed Consumption method...");

    }


}
