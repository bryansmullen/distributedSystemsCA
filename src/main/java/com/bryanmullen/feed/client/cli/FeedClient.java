package com.bryanmullen.feed.client.cli;

import com.bryanmullen.feedService.FeedRequest;
import com.bryanmullen.feedService.FeedServiceGrpc;
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
            case "feedMethod" -> doFeedMethod(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }

    private static void doFeedMethod(ManagedChannel channel){
        // inform the user of the start of the call
        System.out.println("Starting to do feed method...");

        var stub = FeedServiceGrpc.newBlockingStub(channel);

        var request = FeedRequest.newBuilder()
                .setName("Testing from client")
                .build();

        var response = stub.feedMethod(request);

        // print the response
        System.out.println("Feed response: " + response.getMessage());
    }
}
