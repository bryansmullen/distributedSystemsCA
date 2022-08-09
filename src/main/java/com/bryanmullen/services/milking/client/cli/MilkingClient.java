package com.bryanmullen.services.milking.client.cli;

import com.bryanmullen.mdns.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.jmdns.ServiceInfo;

public class MilkingClient {
    public static void main(String[] args) {
        // find service on the network
        final String SERVICE_TYPE = "_milking._tcp.local.";
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        ServiceInfo serviceInfo = ServiceDiscovery.run(SERVICE_TYPE);

        // constants
        final String HOST;

        if (serviceInfo == null) {
            System.out.println("No service found on the network");
        } else {
            HOST = serviceInfo.getHostAddresses()[0];
            final int PORT = serviceInfo.getPort();

            // log that service has been found
            System.out.println("Found service at " + HOST + ":" + PORT);

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
