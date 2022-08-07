package com.bryanmullen.milking.client.cli;

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
            case "milkCollection" -> doMilkCollection(channel);
            case "milkProduction" -> doMilkProduction(channel);
            case "milkCurrentCow" -> doMilkCurrentCow(channel);
            default -> System.out.println("Unknown command: " + args[0]);
        }

        // close the channel
        System.out.println("Closing channel...");
        channel.shutdown();
    }


    private static void doMilkCollection(ManagedChannel channel) {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Collection method...");
    }

    private static void doMilkProduction(ManagedChannel channel) {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Production method...");
    }

    private static void doMilkCurrentCow(ManagedChannel channel) {
        // inform the user of the start of the call
        System.out.println("Starting to do Milk Current Cow method...");
    }


}
