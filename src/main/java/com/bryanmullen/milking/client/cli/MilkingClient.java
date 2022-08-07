package com.bryanmullen.milking.client.cli;

import com.bryanmullen.milkingService.MilkingRequest;
import com.bryanmullen.milkingService.MilkingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MilkingClient {
    public static void main(String[] args) {
        // constants
        final String HOST = "127.0.0.1";
        final int PORT = 5051;

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
            case "milkingMethod" -> doMilkingMethod(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }

    private static void doMilkingMethod(ManagedChannel channel){
        // inform the user of the start of the call
        System.out.println("Starting to do dummy method...");

        var stub =
                MilkingServiceGrpc.newBlockingStub(channel);

        var request = MilkingRequest.newBuilder()
                .setName("Testing from client")
                .build();

        var response = stub.milkingMethod(request);

        // print the response
        System.out.println("Milking response: " + response.getMessage());
    }
}
