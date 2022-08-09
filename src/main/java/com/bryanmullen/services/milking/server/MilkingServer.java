package com.bryanmullen.services.milking.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

public class MilkingServer {
    public static void main(String[] args) throws IOException {
        // constants
        final int SERVICE_PORT = 5051;
        final String SERVICE_TYPE = "_milking._tcp.local.";
        final String SERVICE_NAME = "Milking Service";
        final String SERVICE_DESCRIPTION = " Milking Description";

        // create the server
        Server server = ServerBuilder
                .forPort(SERVICE_PORT)
                .addService(new MilkingServiceImpl())
                .build();

        // start the server
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Failed to start server: " + e.getMessage());
        }

        System.out.println("Server started on port " + SERVICE_PORT);

        // register the service with mDNS
        System.out.println("Registering service with mDNS...");

        // get a jMDNS instance - use the localhost as hostname
        JmDNS mdns = JmDNS.create("172.20.10.3");

        // service information
        ServiceInfo serviceInfo = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME
                , SERVICE_PORT, SERVICE_DESCRIPTION);

        // register the service
        mdns.registerService(serviceInfo);

        // log the service registration details
        System.out.printf("Registered service: %s%n", serviceInfo);

        // inform the user that the server is ready
        System.out.println("Milking Server Listening for Requests...");

        // shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down milking server...");
            // unregister the service
            mdns.unregisterService(serviceInfo);
            System.out.println("Service Unregistered");

            // close the jMDNS instance
            try {
                mdns.close();
                System.out.println("jMDNS closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // shutdown the server
            server.shutdown();
            System.out.println("Server shut down");
        }));

        // block until the server is terminated
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            System.out.println("Failed to await termination: " + e.getMessage());
        }
    }
}
