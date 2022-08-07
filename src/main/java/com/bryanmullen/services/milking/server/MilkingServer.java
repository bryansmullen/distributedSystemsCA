package com.bryanmullen.services.milking.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class MilkingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        // constants
        final int PORT = 5051;

        // create the server
        Server server = ServerBuilder
                .forPort(PORT)
                .addService(new MilkingServiceImpl())
                .build();

        // start the server
        server.start();
        System.out.println("Milking Server listening on port " + PORT);

        // shutdown hook to stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down milking server...");
            server.shutdown();
            System.out.println("Server shut down");
        }));

        // block until the server is terminated
        server.awaitTermination();
    }
}
